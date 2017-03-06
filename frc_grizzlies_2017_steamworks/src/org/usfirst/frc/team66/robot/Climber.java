package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class Climber {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static CANTalon climbMotor = Constants.CLIMB_MOTOR;
	
	public Climber(){
		climbMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		climbMotor.enableBrakeMode(true);
	}
	
	public void updateClimberTeleop(){
		if(controller.getRawButton(Constants.RIGHT_BUMPER)){
			climbMotor.set(1.0);
		}
		else{
			climbMotor.set(0.0);
		}
	}

}
