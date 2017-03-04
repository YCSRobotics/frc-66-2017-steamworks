package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class GearIntake {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Talon leftGearMotor = Constants.LEFT_GEAR_MOTOR;
	private static Talon rightGearMotor = Constants.RIGHT_GEAR_MOTOR;
	
	private static Solenoid lowerIntakeSolenoid = Constants.LOWER_INTAKE_SOLENOID;
	private static Solenoid openIntakeSolenoid = Constants.OPEN_INTAKE_SOLENOID;
	
	private boolean isIntakeLowered;
	private boolean isRaiseIntakePressed;
	
	private static boolean isIntakeOpen;
	private boolean isOpenIntakePressed;
	
	public GearIntake(){

	}
	
	public void updateGearIntakeTelopPeriodic(){
		
		double intakeSpeed;
		double rotateSpeed;
		
		toggleRaiseIntake();
		toggleOpenIntake();
		
		intakeSpeed = getGearInOutSpeed();
		
		if(Math.abs(intakeSpeed) > 0){
			leftGearMotor.set(intakeSpeed);
			rightGearMotor.set(-1.0*intakeSpeed);
		}
		else{
			leftGearMotor.set(0.0);
			rightGearMotor.set(0.0);
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
	
	private boolean isOpenIntakeButtonPressed(){
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
		    	//openIntakeSolenoid.set(false);
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
	
	private void toggleOpenIntake(){
		if((isOpenIntakeButtonPressed()) &&
		   (!isOpenIntakePressed)){
			
			//Button transitions from false to true, set button active flag	
			isOpenIntakePressed = true;
					
		    if(isIntakeOpen){
		    	//Intake is open, so close intake (solenoid off)
		    	isIntakeOpen = false;
		    	openIntakeSolenoid.set(false);
			} 
			else {
				//Intake is raised, so lower intake (solenoid on)
				isIntakeOpen = true;
				openIntakeSolenoid.set(true);
			}
		}
		else if (!(isOpenIntakeButtonPressed())){
			isOpenIntakePressed = false;
		}
		else{
			//Do nothing, button is still pressed
		}
	}
	
	public static void openIntake(){
		isIntakeOpen = true;
		openIntakeSolenoid.set(true);
	}
	
	public static void closeIntake(){
		isIntakeOpen = false;
		openIntakeSolenoid.set(false);
	}
	
	private static double getGearInOutSpeed(){
		double v;
		v = controller.getRawAxis(Constants.LEFT_STICK_Y);
		return (Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? v : 0.0);
	}

}