package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Fuel {
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Talon fuelLiftMotor = Constants.FUEL_LIFT_MOTOR;
	private static Talon fuelHopperMotor = Constants.FUEL_HOPPER_MOTOR;
	
	private static Solenoid fuelUnloadSolenoid = Constants.FUEL_UNLOAD_SOLENOID;
	
	public Fuel(){
		
	}
	
	public void updateFuelTelop(){
		if(isLoadButtonPressed())
		{
			fuelLiftMotor.set(Constants.LIFT_MOTOR_DIRECTION * 1.0);
			fuelHopperMotor.set(Constants.HOPPER_MOTOR_DIRECTION * 1.0);
			fuelUnloadSolenoid.set(false);
		}
		else if(isUnloadButtonPressed())
		{
			fuelLiftMotor.set(Constants.LIFT_MOTOR_DIRECTION * 1.0);
			fuelHopperMotor.set(Constants.HOPPER_MOTOR_DIRECTION * -1.0);
			fuelUnloadSolenoid.set(true);
		}
		else
		{
			fuelLiftMotor.set(0.0);
			fuelHopperMotor.set(0.0);
			fuelUnloadSolenoid.set(false);
		}
	}
	
	private boolean isLoadButtonPressed(){
		if(controller.getRawAxis(Constants.LEFT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean isUnloadButtonPressed(){
		if(controller.getRawAxis(Constants.RIGHT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD){
			return true;
		}
		else{
			return false;
		}
	}

}
