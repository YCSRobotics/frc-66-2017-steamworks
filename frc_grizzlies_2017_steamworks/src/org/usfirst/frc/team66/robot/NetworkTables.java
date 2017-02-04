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
	
	public void getPiValues() {
		
		
		NetworkTable table = NetworkTable.getTable("table");
		
		//set default array
		double[] arrayDefault = {0.0,0.0,0.0,0.0};
		double[] piData;
		
		//if tables contains key else
		if(table.containsKey("x") == true) {
			
			//getNumberArray Values
			piData = table.getNumberArray("arrayValue", arrayDefault);
			
			//debug
			System.out.println(piData[0] + "," + piData[1]);
			
		} else {
			//no key found
			System.out.println("No Key Found");
			
		}
		
	}
	
}
