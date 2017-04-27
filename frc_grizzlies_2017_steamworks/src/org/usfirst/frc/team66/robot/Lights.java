package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Lights {
	
	private static Relay lights = Constants.LIGHT_RELAY;
	
	public Lights(){
		
	}
	
	public void updateLights(){
		boolean sensor;
		
		sensor = GearIntake.getGearSensorInput();
		
		if(sensor){
			lights.set(Value.kForward);
		}
		else
		{
			lights.set(Value.kOff);
		}
	}
	

}
