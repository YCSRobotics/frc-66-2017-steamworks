package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
	
	private static Joystick controller = Constants.DRIVE_CONTROLLER;
	
	private static Solenoid shiftSolenoid = Constants.SHIFT_SOLENOID;
	
	private static CANTalon leftMasterMotor = Constants.LEFT_MASTER_MOTOR;
	private static CANTalon leftSlaveMotor = Constants.LEFT_SLAVE_MOTOR;
	private static CANTalon rightMasterMotor = Constants.RIGHT_MASTER_MOTOR;
	private static CANTalon rightSlaveMotor = Constants.RIGHT_SLAVE_MOTOR;
	
	private static Encoder leftEncoder = Constants.LEFT_WHEEL_ENCODER;
	private static Encoder rightEncoder = Constants.RIGHT_WHEEL_ENCODER;
	
	private double leftMotorCommand = 0.0;
	private double rightMotorCommand = 0.0;
	private boolean isInverted = false;
	private boolean isInvertPressed = false;
	
	public Drivetrain() {		
		
		leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlaveMotor.set(leftMasterMotor.getDeviceID());
		
		rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlaveMotor.set(rightMasterMotor.getDeviceID());
		
	}
	
	public void updateDrivetrainTeleop() {
		//To be called from TeleopPeriodic every 20ms
		
		double driveGain;
		double throttle;
		double turn;
		
		//Check if finesse button pressed and set drive gain
		if (controller.getRawAxis(Constants.LEFT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD) {
			driveGain = Constants.FINESSE_GAIN;
		}
		else {
			driveGain = 1.0;
		}
		
		//Check deadzone on controller thumbsticks
		if (Math.abs(controller.getRawAxis(Constants.LEFT_STICK_Y)) < Constants.DEAD_ZONE_LIMIT) {
			throttle = 0;
		} else {
			throttle = controller.getRawAxis(Constants.LEFT_STICK_Y);
		}	
		if (Math.abs(controller.getRawAxis(Constants.RIGHT_STICK_X)) < Constants.DEAD_ZONE_LIMIT) {
			turn = 0;
		} else {
			turn = controller.getRawAxis(Constants.RIGHT_STICK_X);
		}
		
		//Handle invert toggle
		setInvert();
		
		setTargetSpeeds(throttle, turn);
		
		//Set motor outputs
		if(!isInverted){
			leftMasterMotor.set((Constants.LEFT_DRIVE_REVERSED ? -1:1) * leftMotorCommand * driveGain);
			rightMasterMotor.set((Constants.RIGHT_DRIVE_REVERSED ? -1:1) * rightMotorCommand * driveGain);
		}
		else{
			leftMasterMotor.set(((Constants.RIGHT_DRIVE_REVERSED ? -1:1) * rightMotorCommand * driveGain));
			rightMasterMotor.set(((Constants.LEFT_DRIVE_REVERSED ? -1:1) * leftMotorCommand * driveGain));
		}
		
		//Update shift solenoid state
		if(controller.getRawAxis(Constants.RIGHT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD) {
			
			shiftSolenoid.set(true);
			
		} else {
			
			shiftSolenoid.set(false);
			
		}
		
		updateDashboard();
		
	}
	
	private void updateDashboard(){
		SmartDashboard.putNumber("Left Encoder Counts", leftEncoder.getRaw());
		SmartDashboard.putNumber("Right Encoder Counts", rightEncoder.getRaw());
		SmartDashboard.putBoolean("Inverted", isInverted);
		SmartDashboard.putBoolean("Invert Button Pressed", isInvertPressed);
		
	}
	
	private double skim(double v) {
		if (v > 1.0)
		{
			return (v - 1.0);
		}
		else if (v < -1.0)
		{
			return -(v + 1.0); 
		}
		else
		{
			return 0;
		}
	}
	
	private void setTargetSpeeds(double throttle, double turn){
		double t_left;
		double t_right;
		
		turn = turn * Constants.TURN_GAIN;
		
		t_left = throttle - turn;
		t_right = throttle + turn;
		
		leftMotorCommand = t_left + skim(t_right);
		rightMotorCommand = t_right + skim(t_left);
	}
	
	private void setInvert(){
		
		if((controller.getRawButton(Constants.BACK_BUTTON)) &&
		   (!isInvertPressed)){
					
			isInvertPressed = true;
					
		    if(isInverted){
		    	isInverted = false;
			} 
			else {
				isInverted = true;
			}
		}
		else if (!controller.getRawButton(Constants.BACK_BUTTON)){
			isInvertPressed = false;
		}
		else{
			//Do nothing, button is still pressed
		}
	}
}
