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
			fuelLiftMotor.set(Constants.LIFT_MOTOR_DIRECTION * -1.0);
			fuelHopperMotor.set(Constants.HOPPER_MOTOR_DIRECTION * -1.0);
			fuelUnloadSolenoid.set(false);
		}
		else if(isUnloadButtonPressed())
		{
			fuelLiftMotor.set(Constants.LIFT_MOTOR_DIRECTION * -1.0);
			fuelHopperMotor.set(Constants.HOPPER_MOTOR_DIRECTION * 1.0);
			fuelUnloadSolenoid.set(true);
		}
		else
		{
			fuelLiftMotor.set(getFuelUpDownSpeed());
			fuelHopperMotor.set(0.0);
			fuelUnloadSolenoid.set(false);
		}
	}
	
	private boolean isLoadButtonPressed(){
		return(controller.getRawButton(Constants.A_BUTTON));
	}
	
	private boolean isUnloadButtonPressed(){
		return(controller.getRawButton(Constants.Y_BUTTON));
	}
	
	private static double getFuelUpDownSpeed(){
		double v;
		v = controller.getRawAxis(Constants.RIGHT_STICK_Y);
		return (Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? v : 0.0);
	}

}
