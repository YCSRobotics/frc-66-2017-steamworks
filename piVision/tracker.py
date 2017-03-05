import numpy as np
import cv2
import logging
from networktables import NetworkTables

#set networktable info
logging.basicConfig(level=logging.DEBUG)
NetworkTables.setClientMode()
NetworkTables.initialize(server='10.4.70.2')
Table = NetworkTables.getTable("Table")

camera = cv2.VideoCapture('http://10.4.70.11/mjpg/video.mjpg') 
#camera = cv2.VideoCapture(0) #switch from IP to USB

while (True):
    #read the current frame from camera
    (grabbed, frame) = camera.read()

    #convert to HSV
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    #set min and max colour values
    green_lower = np.array([72, 114, 169],np.uint8)
    green_upper = np.array([255, 255, 255],np.uint8)

    #create the range of colour min/max
    green_range = cv2.inRange(hsv, green_lower, green_upper)

    #create blank area for sort
    areaArray = []
    try:
        #grab all contours based on colour range
        b, contours, _ = cv2.findContours(green_range, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        #order contours into an array by area
        for i, c in enumerate(contours):
            area = cv2.contourArea(c)
            areaArray.append(area)
        
        #sort the array by greatest to smallest
        sorteddata = sorted(zip(areaArray, contours), key=lambda x: x[0], reverse=True)
        
        #find the nth largest contour [n-1][1], in this case 2
        secondlargestcontour = sorteddata[1][1]
        
        if len(contours) > 0: 
        #draw it #find second biggest contour, mark it.
             x, y, w, h = cv2.boundingRect(secondlargestcontour)
             cv2.drawContours(frame, secondlargestcontour, -1, (0, 0, 255), 0)
    
             #find biggest contour, mark it
             green=max(contours, key=cv2.contourArea)
             (xg,yg,wg,hg) = cv2.boundingRect(green)
             
             #find aspect ratio of contour
             aspect_ratio1 = float(wg)/hg
             aspect_ratio2 = float(w)/h

             #set min and max ratios
             ratioMax = 0.75
             ratioMin = 0.30
             
             #only run if contour is within ratioValues
             if (aspect_ratio1 and aspect_ratio2 <= ratioMax and aspect_ratio1 and aspect_ratio2 >= ratioMin):
                 
                 #create arrays
                 Rect1 = [xg, yg, xg+wg, yg-hg]
                 Rect2 = [x, y, x+w, y-h]

                 #make the largest values always right rect
                 #this prevents negative values when not wanted
                 if (xg+wg) > x:
                    CenterOfTarget = [(xg+wg-x)/2]
                 else:
                    CenterOfTarget = [(x-xg+wg)/2]

                 if x < (xg+w):
                    CenterOfTargetCoords = (x+CenterOfTarget[0])
                 else:
                    CenterOfTargetCoords = (xg+w+CenterOfTarget[0])

                 #put values to networktable
                 Table.putNumber("CenterOfTargetCoords", CenterOfTargetCoords)
                 Table.putNumberArray("CenterOfTarget", CenterOfTarget)
                 Table.putNumberArray("Rect1", Rect1)
                 Table.putNumberArray("Rect2", Rect2)
                 Table.putBoolean("NoContoursFound", False)
                 
             else: #contour not in aspect ratio
                 Table.putBoolean("NoContoursFound", True)

    except IndexError: #no contours found
        Table.putBoolean("NoContoursFound", True)
    
camera.release()
cv2.destroyAllWindows()
