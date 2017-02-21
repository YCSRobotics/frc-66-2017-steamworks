package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTables {
	
	public NetworkTables() {
		
		//set table as client and connect to server
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.4.70.24");
		NetworkTable.setTeam(470);
		NetworkTable.initialize();
		
	}
	
	public static void getPiValues() {
		
		NetworkTable ntable = NetworkTable.getTable("Table");
		
		String Rect1 = ntable.getString("Rect1", "0"); //rect 1 coords (x1,y1,x2,y2)
		String Rect2 = ntable.getString("Rect2", "0"); //rect 2 coords (x1,y1,x2,y2)
		Boolean NoContoursFound = ntable.getBoolean("NoContoursFound", false); //Is Contours Found? Default to False
		
	}
	
}
