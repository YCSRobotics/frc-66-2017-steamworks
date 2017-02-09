import cv2
import numpy as np
import time
import argparse
import array
from collections import deque
from networktables import NetworkTables

NetworkTables.initialize(server='10.4.70.78')
Table = NetworkTables.getTable("table")
array.array('i')

#create arguments/set frame buffer 
ap = argparse.ArgumentParser()
ap.add_argument("-b", "--buffer", type=int, default=64, help="max buffer size")
args = vars(ap.parse_args())

while True:

	try:
		cap = cv2.VideoCapture('http://10.4.70.87/mjpg/video.mjpg')	
		_, im = cap.read()
		
		gray = cv2.cvtColor(im,cv2.COLOR_BGR2HSV);
	
		greenLower = (29, 86, 6)
		greenUpper = (64, 255, 255)
	
		bin = cv2.inRange(gray, greenLower, greenUpper)
	
		bin = cv2.dilate(bin, None)  # fill some holes
		bin = cv2.dilate(bin, None)
		bin = cv2.erode(bin, None)   # dilate made our shape larger, revert that
		bin = cv2.erode(bin, None)
		bin, contours, hierarchy = cv2.findContours(bin, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
	
		rc = cv2.minAreaRect(contours[0])
		box = cv2.boxPoints(rc)
		for p in box:
	          pt = (p[0],p[1])
	          print(pt)

	except IndexError:
		#do nothing
		print("No contours found")

Table.putNumberArray("arrayValue", pt)
cap.release()
