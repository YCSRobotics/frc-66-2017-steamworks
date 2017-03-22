package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class GearIntake {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Talon gearIntakeMotor = Constants.GEAR_INTAKE_MOTOR;
	
	private static Solenoid lowerIntakeSolenoid = Constants.LOWER_INTAKE_SOLENOID;
	
	private boolean isIntakeLowered;
	private boolean isRaiseIntakePressed;
	
	public GearIntake(){

	}
	
	public void updateGearIntakeTelopPeriodic(){
		
		double intakeSpeed;
		
		toggleRaiseIntake();
		
		//intakeSpeed = getGearInOutSpeed();
		
		if(controller.getRawButton(Constants.A_BUTTON)){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
		}
		else if(isIntakeGearButtonPressed()){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_INTAKE_SPEED);
		}
		else{
			gearIntakeMotor.set(0.0);
		}
	}
	
	private boolean isRaiseIntakeButtonPressed(){
		if(controller.getRawAxis(Constants.LEFT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean isIntakeGearButtonPressed(){
		if(controller.getRawAxis(Constants.RIGHT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD){
			return true;
		}
		else{
			return false;
		}
	}
	private void toggleRaiseIntake(){
		if((isRaiseIntakeButtonPressed()) &&
		   (!isRaiseIntakePressed)){
			
			//Button transitions from false to true, set button active flag	
			isRaiseIntakePressed = true;
					
		    if(isIntakeLowered){
		    	//Intake is lowered, so close and raise intake (solenoid off)
		    	isIntakeLowered = false;
		    	lowerIntakeSolenoid.set(false);
			} 
			else {
				//Intake is raised, so lower intake (solenoid on)
				isIntakeLowered = true;
				lowerIntakeSolenoid.set(true);
			}
		}
		else if (!(isRaiseIntakeButtonPressed())){
			isRaiseIntakePressed = false;
		}
		else{
			//Do nothing, button is still pressed
		}
	}
	
	public static void commandGearEject(boolean command){
		if(command){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
		}
		else
		{
			gearIntakeMotor.set(0.0);
		}
	}
	
	private static double getGearInOutSpeed(){
		double v;
		v = controller.getRawAxis(Constants.LEFT_STICK_Y);
		return (Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? v : 0.0);
	}

}