package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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
	
	private static ADXRS450_Gyro gyro = Constants.GYRO;
	
	private static double targetThrottle = 0.0;
	private static double targetTurn = 0.0;
	private double leftMotorCommand = 0.0;
	private double rightMotorCommand = 0.0;
	
	private boolean isDriverControl = true;
	private boolean isInverted = false;
	private boolean isInvertPressed = false;
	
	private static boolean isGyroZeroed = false;
	private boolean isDriveStraight = false;
	
	private static double targetDistance = 0.0;
	private static boolean isMovingDistance = false;
	
	private static boolean isMovingToVisionTarget = false;
	private static boolean isVisionTargetReached = false;
	
	private static boolean isTurningToVisionTarget	= false;
	private static boolean isTargetFound = false;
	
	private static boolean isCenteringVisionTarget = false;
	private static boolean isTargetCentered = false;
	
	private static int invalidTargetCount = 0;
	
	public Drivetrain() {		
		
		leftMasterMotor.enableBrakeMode(Constants.ENABLE_DRIVE_BRAKE);
		leftSlaveMotor.enableBrakeMode(Constants.ENABLE_DRIVE_BRAKE);
		leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftMasterMotor.setVoltageRampRate(Constants.DRIVE_RAMP_RATE);
		leftSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlaveMotor.set(leftMasterMotor.getDeviceID());
		
		leftEncoder.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		
		rightMasterMotor.enableBrakeMode(Constants.ENABLE_DRIVE_BRAKE);
		rightSlaveMotor.enableBrakeMode(Constants.ENABLE_DRIVE_BRAKE);
		rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightMasterMotor.setVoltageRampRate(Constants.DRIVE_RAMP_RATE);
		rightSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlaveMotor.set(rightMasterMotor.getDeviceID());
		
		rightEncoder.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
		
		gyro.calibrate();
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	public void updateDrivetrainAuton(){
		double distance_error;
		double vision_target_angle;
		
		if(isMovingDistance){
			//Move distance without tracking vision target
			distance_error = targetDistance - getAverageDistance();
			
			if(Math.abs(distance_error) <= Constants.TARGET_DISTANCE_THRESHOLD){
				//Robot has reached target
				targetThrottle = 0.0;
				targetTurn     = 0.0;
				isMovingDistance = false;
			}
			else
			{
				targetTurn = -1*(gyro.getAngle()*Constants.GYRO_GAIN);
			}
		}
		else if(isMovingToVisionTarget){
			//Move forward while tracking vision target
			distance_error = targetDistance - getAverageDistance();
			
			if((PiMath.getTargetDistance() <= Constants.VISION_TARGET_THRESHOLD) ||
			   (Math.abs(distance_error) <= Constants.TARGET_DISTANCE_THRESHOLD)){
			//Reached minimum target distance so stop
				targetThrottle = 0.0;
				targetTurn     = 0.0;
				isVisionTargetReached = true;
				isMovingToVisionTarget = false;
			}
			else
			{
				targetTurn = -1*(-1.0 * (PiMath.angleToTarget() + Constants.TARGET_ANGLE_OFFSET)*Constants.GYRO_GAIN);
			}
		}
		else if(isTurningToVisionTarget)
		{
			
			//turn_error = Constants.MAX_TURN_TO_TARGET_ANGLE - Math.abs(gyro.getAngle());
					
			if(Math.abs(gyro.getAngle()) >= Constants.MAX_TURN_TO_TARGET_ANGLE){
				targetThrottle = 0.0;
				targetTurn = 0.0;
				isTurningToVisionTarget = false;
			}else
			{
				//Do Nothing while turning
			}
			/*//Turn until vision target centered
			if(Math.abs(gyro.getAngle()) <= Constants.MAX_TURN_TO_TARGET_ANGLE){
				if((PiMath.isValidTargetPresent() &&
				   (!isTargetFound))){
					//Set a flag the first time we see the target
					isTargetFound = true;
				}
				else{
					//Do nothing if we never see target
				}
				
				if((isTargetFound) &&
				   (Math.abs(gyro.getAngle()) >= Constants.MIN_TURN_TO_TARGET_ANGLE)){		   	
					
					if(Math.abs(PiMath.angleToTarget()) <= Constants.TARGET_ANGLE_THRESHOLD){
						//Target is centered so stop turning
						targetThrottle = 0.0;
						targetTurn = 0.0;
						isTargetCentered = true;
						isTurningToVisionTarget = false;
					}
					else
					{
						//Target not centered, so keep turning
					}
				}
				else{
					//Keep turning until max turn reached or target found
				}
					
			}
			else{
				//Never saw target
				targetThrottle = 0.0;
				targetTurn = 0.0;
				isTurningToVisionTarget = false;
			}*/
		}
		else if(isCenteringVisionTarget){
			vision_target_angle = PiMath.angleToTarget();
			
			if((PiMath.isValidTargetPresent()) &&
			   (Math.abs(vision_target_angle) >= Constants.TARGET_ANGLE_THRESHOLD)){			
				if(vision_target_angle >= 0){
					targetTurn = 0.4;
				}
				else{
					targetTurn = -0.4;
				}
			}
			else if((PiMath.isValidTargetPresent()) &&
			        (Math.abs(PiMath.angleToTarget()) < Constants.TARGET_ANGLE_THRESHOLD)){
				//Target found and is centered
				targetTurn = 0.0;
				isCenteringVisionTarget = false;
				isTargetCentered = true;
			}
			else
			{
				//Target is not found
				targetTurn = 0.0;
				isCenteringVisionTarget = false;
				isTargetCentered = false;
			}
			
		}
		else{
			targetThrottle = 0.0;
			targetTurn = 0.0;
		}
		
		setTargetSpeeds(targetThrottle, targetTurn);
		
		leftMasterMotor.set((Constants.LEFT_DRIVE_REVERSED ? -1:1) * leftMotorCommand);
		rightMasterMotor.set((Constants.RIGHT_DRIVE_REVERSED ? -1:1) * rightMotorCommand);
		
		updateDashboard();
	}
	
	public void updateDrivetrainTeleop() {
		//To be called from TeleopPeriodic every 20ms
		
		double driveGain;
		
		//Handle invert toggle
		setInvert();
		
		//Check if finesse button pressed and set drive gain
		if (controller.getRawAxis(Constants.LEFT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD) {
			driveGain = Constants.FINESSE_GAIN;
		}
		else {
			driveGain = 1.0;
		}
		
		isDriveStraight = isDriveStraightConditionPresent();
		
		if((isDriveStraight) &&
		   (!isGyroZeroed)){
			//Gyro not zeroed, reset zero and wait one loop
			zeroGyro();
		}
		else if(isDriveStraight){
			//Drive Straight pressed and gyro zeored
			goStraight();
			isDriveStraight = true;
		}
		else{
			//Do regular teleop control
			targetThrottle = getThrottleInput();
			targetTurn = getTurnInput();
			isGyroZeroed = false;
			//isDriveStraight = false;
		}

		setTargetSpeeds(targetThrottle, targetTurn);
		
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
	
	public static void zeroGyro(){
		gyro.reset();
		isGyroZeroed = true;
	}
	
	private void updateDashboard(){
		SmartDashboard.putNumber("Left Wheel Distance", leftEncoder.getDistance());
		SmartDashboard.putNumber("Right Wheel Distance", rightEncoder.getDistance());
		SmartDashboard.putBoolean("Inverted", isInverted);
		SmartDashboard.putBoolean("Invert Button Pressed", isInvertPressed);
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
		SmartDashboard.putNumber("Left Motor Command", leftMotorCommand);
		SmartDashboard.putNumber("Right Motor Command", rightMotorCommand);
		SmartDashboard.putBoolean("Is Gyro Zeroed", isGyroZeroed);
		SmartDashboard.putBoolean("Is Driving Straight", isDriveStraight);
		SmartDashboard.putBoolean("Is Moving To Vision Target", isMovingToVisionTarget);
		SmartDashboard.putBoolean("Is Target Reached", isVisionTargetReached);
		SmartDashboard.putBoolean("Is Turning To Target", isTurningToVisionTarget);
		SmartDashboard.putBoolean("Is Target Found", isTargetFound);
		SmartDashboard.putBoolean("Is Target Centered", isTargetCentered);
		SmartDashboard.putNumber("Raw Distance to Vision Target", PiMath.getTargetDistance());
		SmartDashboard.putNumber("Raw Angle to Vision Target", PiMath.angleToTarget());
		SmartDashboard.putBoolean("Is Target Valid", PiMath.isValidTargetPresent());
		SmartDashboard.putNumber("Target Throttle", targetThrottle);
		SmartDashboard.putNumber("Target Turn", targetTurn);
	}
	
	private double getThrottleInput()
	{
		double v;
		v = controller.getRawAxis(Constants.LEFT_STICK_Y);
		//Thumbstick increasing value is toward operator!
		return (Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? -(v) : 0.0);
	}

	private double getTurnInput()
	{
		double v;
		v = controller.getRawAxis(Constants.RIGHT_STICK_X);
		
		return(v);
		/*if(getThrottleInput() < 0){
			return(Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? -(v) : 0.0);
		}else{
			return(Math.abs(v) > Constants.DEAD_ZONE_LIMIT ? v : 0.0);
		}*/
	}
		
	
	private double skim(double v) {
		if (v > 1.0)
		{
			return -((v-1.0)*Constants.SKIM_GAIN);
		}
		else if (v < -1.0)
		{
			return -((v+1.0)*Constants.SKIM_GAIN); 
		}
		else
		{
			return 0;
		}
	}
	
	private void setTargetSpeeds(double throttle, double turn){
		
		double t_left;
		double t_right;
		
		//Amp up turn only for large amounts of throttle, already dead zoned
		if(throttle > 0){
			turn = turn * (Constants.TURN_GAIN * Math.abs(throttle));
		}else
		{
			turn = turn * Constants.FINESSE_GAIN;
		}

		
		t_left = throttle + turn;
		t_right = throttle - turn;
		
		leftMotorCommand = t_left + skim(t_right);
		rightMotorCommand = t_right + skim(t_left);
		
		leftMotorCommand = Math.max(-1.0, (Math.min(leftMotorCommand, 1.0)));
		rightMotorCommand = Math.max(-1.0, (Math.min(rightMotorCommand, 1.0)));

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
	
	private void goStraight(){
		targetThrottle = getThrottleInput();		
		targetTurn = -1*(gyro.getAngle()*Constants.GYRO_GAIN);
	}
	
	public static void setMoveDistance(double distance, double power){
		//Positive distance and power is forward, negative is rearward
		
		//Reset Encoders because we want to measure distance from start
		leftEncoder.reset();
		rightEncoder.reset();
		
		targetDistance = distance;
		targetTurn = 0.0;
		
		if(Math.abs(targetDistance) > Constants.TARGET_DISTANCE_THRESHOLD){
			isMovingDistance = true;
			targetThrottle = power;
		}
		else
		{
			isMovingDistance = false;
			targetThrottle = 0.0;
		}
	}
	
	public static void setMoveToVisionTarget(double distance, double power){	
			
		//Reset Encoders because we want to measure distance from start
				leftEncoder.reset();
				rightEncoder.reset();
				
				targetDistance = distance;
				targetTurn = 0.0;
				
			if(PiMath.getTargetDistance() > Constants.VISION_TARGET_THRESHOLD){
				isMovingToVisionTarget = true;
				targetThrottle = power;
			}
			else{
				isMovingToVisionTarget = false;
				targetThrottle = 0.0;
			}
	}
	
	public static void setCenterVisionTarget(){
		isCenteringVisionTarget = true;
		isTargetCentered = false;
	}
	
	public static void setTurnToTarget(double turn){
		isTurningToVisionTarget	= true;
		//isTargetFound = false;
		//isTargetCentered = false;
		targetTurn = turn;
	}
	
	public static boolean isMovingDistance(){
		return isMovingDistance;
	}
	
	public static boolean isMovingToVisionTarget(){
		return isMovingToVisionTarget;
	}
	
	public static boolean isVisionTargetReached(){
		return isVisionTargetReached;
	}
	
	public static boolean isTurningToVisionTarget(){
		return isTurningToVisionTarget;
	}
	
	public static boolean isCenteringVisionTarget(){
		return isCenteringVisionTarget;
	}
	
	public static boolean isVisionTargetCentered(){
		return isTargetCentered;
	}
	
	private double getAverageDistance(){
		double ave;		
		ave = (leftEncoder.getDistance() + rightEncoder.getDistance())/2;
		return ave;
	}
	
	private boolean isDriveStraightConditionPresent(){
		/*if((Math.abs(getThrottleInput()) > 0) && 
		   (getTurnInput() == 0)){
			return true;
		}
		else
		{
			return false;
		}*/
		return controller.getRawButton(Constants.A_BUTTON);
	}
	
}
