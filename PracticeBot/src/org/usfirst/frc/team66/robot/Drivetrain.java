package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
	
	private static Joystick controller = new Joystick(0);
	private static Solenoid shiftSolenoid;
	
	private static CANTalon leftMasterMotor = new CANTalon(0);
	private static CANTalon leftSlaveMotor = new CANTalon(1);
	private static CANTalon rightMasterMotor = new CANTalon(2);
	private static CANTalon rightSlaveMotor = new CANTalon(3);
	
	private static CANTalon LEFT_MASTER_MOTOR;
	private static CANTalon LEFT_SLAVE_MOTOR;
	private static double LEFT_MOTOR_SCALER;
	private static CANTalon RIGHT_MASTER_MOTOR;
	private static CANTalon RIGHT_SLAVE_MOTOR;
	private static double RIGHT_MOTOR_SCALER;
	
	//static Scaler leftSide;
	//static Scaler rightSide;

	public Drivetrain() {
		
		/*Drivetrain.LEFT_MASTER_MOTOR = Constants.LEFT_MASTER_MOTOR;
		Drivetrain.LEFT_SLAVE_MOTOR = Constants.LEFT_SLAVE_MOTOR;
		Drivetrain.LEFT_MOTOR_SCALER = Constants.LEFT_MOTOR_SCALER;
		Drivetrain.RIGHT_MASTER_MOTOR = Constants.RIGHT_MASTER_MOTOR;
		Drivetrain.RIGHT_SLAVE_MOTOR = Constants.LEFT_MASTER_MOTOR;*/
		
		Drivetrain.shiftSolenoid = Constants.SHIFTSOLENOID;
		
		/*Drivetrain.RIGHT_MOTOR_SCALER = Constants.RIGHT_MOTOR_SCALER;
		Drivetrain.leftSide = new Scaler(LEFT_MASTER_MOTOR, LEFT_SLAVE_MOTOR, LEFT_MOTOR_SCALER);
		Drivetrain.rightSide = new Scaler(RIGHT_MASTER_MOTOR, RIGHT_SLAVE_MOTOR, RIGHT_MOTOR_SCALER);*/
		
		leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		leftSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftSlaveMotor.set(leftMasterMotor.getDeviceID());
		
		rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightSlaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightSlaveMotor.set(rightMasterMotor.getDeviceID());
		
	}
	
	public void updateDrivetrainTeleop() {
		
		double driveGain = Constants.TURBO_SCALER;
		double t_left;
		double t_right;
		
		if (Math.abs(controller.getRawAxis(1)) < 0.3) {
			t_left = 0;
		} else {
			t_left = controller.getRawAxis(1);
		}
		
		if (Math.abs(controller.getRawAxis(5)) < 0.3) {
			t_right = 0;
		} else {
			t_right = controller.getRawAxis(5);
		}
		
		leftMasterMotor.set(t_left);
		rightMasterMotor.set(t_right * -1.0);
		
		if(controller.getRawAxis(3) >= 0.5) {
			
			shiftSolenoid.set(true);
			
		} else {
			
			shiftSolenoid.set(false);
			
		}
		
	}
}
