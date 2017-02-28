package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTables {
	
	static double[] defaultArray = {0.0}; //double array will default this value
	
	public NetworkTables() {
		
		//set table as client and connect to server
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.4.70.12");
		NetworkTable.setTeam(470);
		NetworkTable.initialize();
			
	}
	
	public static double[] Rect1(){
		while(true){ //for some reason, code won't loop when called without while loop
			
			NetworkTable ntable = NetworkTable.getTable("Table");
			double[] Rect1 = ntable.getNumberArray("Rect1", defaultArray); //rect 1 coords {x1,y1,x2,y2}
			return Rect1;
			
		}
	}
	
	public static double[] Rect2(){
		while(true){
			
			NetworkTable ntable = NetworkTable.getTable("Table");
			double[] Rect2 = ntable.getNumberArray("Rect2", defaultArray); //rect 2 coords {x1,y1,x2,y2}
			return Rect2;
			
		}
	}
	
	public static boolean NoContoursFound(){
		while(true){
			
			NetworkTable ntable = NetworkTable.getTable("Table");	
			boolean NoContoursFound = ntable.getBoolean("NoContoursFound", false); //check if No Contours are found, defaults to true
			return NoContoursFound;
			
		}
	}
	
	public static double[] CenterOfTarget(){
		while(true){
			
			NetworkTable ntable = NetworkTable.getTable("Table");
			double[] CenterOfTarget = ntable.getNumberArray("CenterOfTarget", defaultArray); //center of target x,y
			return CenterOfTarget;
			
		}
	}

	public static double[] OverallWidth(){
		while(true){
			
			NetworkTable ntable = NetworkTable.getTable("Table");
			double[] OverallWidth = ntable.getNumberArray("OverallWidth", defaultArray); //center of target absolute x,y
			return OverallWidth;
			
		}
	}
	
	public static double CenterOfTargetCoords(){
		while(true){
			
			NetworkTable ntable = NetworkTable.getTable("Table");
			double CenterOfTargetCoords = ntable.getNumber("CenterOfTargetCoords", 0.0); //center of target x,y
			return CenterOfTargetCoords;
			
		}
	}
}
