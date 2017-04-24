package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Fuel {
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Solenoid fuelUnloadSolenoid = Constants.FUEL_UNLOAD_SOLENOID;
	
	public Fuel(){
		
	}
	
	public void updateFuelTelop(){
		if(isUnloadButtonPressed())
		{
			fuelUnloadSolenoid.set(true);
		}
		else
		{
			fuelUnloadSolenoid.set(false);
		}
	}
	
	public static void commandFuelDumpSolenoid(boolean command){
		fuelUnloadSolenoid.set(command);
	}
	
	private boolean isUnloadButtonPressed(){
		return(controller.getRawButton(Constants.X_BUTTON));
	}
	
}
