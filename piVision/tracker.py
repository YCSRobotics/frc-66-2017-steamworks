import numpy as np
import cv2
import logging
from networktables import NetworkTables

logging.basicConfig(level=logging.DEBUG)
NetworkTables.setClientMode()
NetworkTables.initialize(server='10.4.70.78')
Table = NetworkTables.getTable("Table")

camera = cv2.VideoCapture('http://10.4.70.87/mjpg/video.mjpg')
#camera = cv2.VideoCapture(0)

count = 1

while (True):
    (grabbed, frame) = camera.read()

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    green_lower = np.array([29, 105, 0],np.uint8)
    green_upper = np.array([67, 255, 255],np.uint8)

    green_range = cv2.inRange(hsv, green_lower, green_upper)

    areaArray = []
    count = 1
    try:
        b, contours, _ = cv2.findContours(green_range, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
 
        for i, c in enumerate(contours):
            area = cv2.contourArea(c)
            areaArray.append(area)
        
        #first sort the array by area
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
             ratioMin = 0.20
             
             #print(aspect_ratio1) #for debug
             #print(aspect_ratio2)
             
             #only run if contour is within ratioValues
             if (aspect_ratio1 and aspect_ratio2 <= ratioMax and aspect_ratio1 and aspect_ratio2 >= ratioMin):
                 cv2.rectangle(frame, (x, y), (x+w, y+h), (0,255,0), 2)
                 cv2.rectangle(frame, (xg,yg), (xg+wg, yg+hg), (0,255,0), 2)
                 
                 #create arrays
                 Rect1 = [xg, yg, wg+wg, yg-hg]
                 Rect2 = [x, y, x+w, y-h]
                 
                 #calculate information
                 OverallWidth = abs((x+w)-(xg+wg))
                 CenterOfTarget = [OverallWidth/2]
                 CenterOfTargetCoords = ((x+w)+(xg+wg)/2)

                 #put values to networktable
                 Table.putNumber("OverallWidth", OverallWidth)
                 Table.putNumber("CenterOfTargetCoords", CenterOfTargetCoords)
                 Table.putNumberArray("CenterOfTarget", CenterOfTarget)
                 Table.putNumberArray("Rect1", Rect1)
                 Table.putNumberArray("Rect2", Rect2)
                 Table.putBoolean("NoContoursFound", False)

             else:
                 Table.putBoolean("NoContoursFound", True)

    except IndexError:
        Table.putBoolean("NoContoursFound", True)
    
    #cv2.imshow("Frame", frame) #enable for debug
    #key = cv2.waitKey(1) & 0xFF
    
    #if key ==ord("q"):
        #break
    
camera.release()
cv2.destroyAllWindows()
