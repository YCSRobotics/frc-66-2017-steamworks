package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static CANTalon climbMotor = Constants.CLIMB_MOTOR;
	
	private static Solenoid climberLatch = Constants.CLIMB_LATCH_SOLENOID;
	
	public Climber(){
		climbMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		climbMotor.enableBrakeMode(true);
	}
	
	public void updateClimberTeleop(){
		
		if(controller.getRawButton(Constants.START_BUTTON)){
			climberLatch.set(true);
		}
		else{
			climberLatch.set(false);
		}
		
		if(controller.getRawButton(Constants.RIGHT_BUMPER)){
			climbMotor.set(1.0);
		}
		else{
			climbMotor.set(0.0);
		}
	}

}
