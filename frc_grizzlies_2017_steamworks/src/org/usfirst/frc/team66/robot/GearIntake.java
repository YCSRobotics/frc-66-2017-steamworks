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
	
	private boolean isIntakeOpen;
	private boolean isOpenIntakePressed;
	
	public GearIntake(){

	}
	
	public void updateGearIntakeTelopPeriodic(){
		
		double intakeSpeed;
		
		toggleRaiseIntake();
		toggleOpenIntake();
		
		intakeSpeed = controller.getRawAxis(Constants.LEFT_STICK_Y);
		
		leftGearMotor.set(intakeSpeed);
		rightGearMotor.set(-1.0*intakeSpeed);
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
		    	//Intake is lowered, so raise intake (solenoid off)
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
	
	private void toggleOpenIntake(){
		if((isOpenIntakeButtonPressed()) &&
		   (!isOpenIntakePressed)){
			
			//Button transitions from false to true, set button active flag	
			isOpenIntakePressed = true;
					
		    if(isIntakeOpen){
		    	//Intake is lowered, so raise intake (solenoid off)
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

}