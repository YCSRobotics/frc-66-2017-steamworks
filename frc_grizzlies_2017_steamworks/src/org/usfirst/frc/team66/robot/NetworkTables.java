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
		
		NetworkTable ntable = NetworkTable.getTable("table");
		
		//set default array
		String[] arrayDefault = {"0.0"};
		
		//Checks if connected, otherwise prints not connected.
		if(ntable.isConnected() == true) {
			
			ntable.getStringArray("Rect1", arrayDefault); //rect 1 coords (x1,y1,x2,y2)
			ntable.getStringArray("Rect2", arrayDefault); //rect 2 coords (x1,y1,x2,y2)
			ntable.getBoolean("NoContoursFound", false); //Is Contours Found? Default to False
			
		} else {
			
			System.out.println("NetworkTable not Connected!");
			
		}
		
	}
	
}
