package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Constants {
	
	//Controllers
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0); //Drive
	public static final Joystick OP_CONTROLLER = new Joystick(1); //Operator
	
	public static final int LEFT_STICK_Y  = 1;
	public static final int RIGHT_STICK_X = 4;
	public static final int RIGHT_TRIGGER = 3;
	public static final int LEFT_TRIGGER  = 2;
	public static final int BACK_BUTTON   = 7;
	public static final int A_BUTTON      = 1;
	
	//DIO
	
	public static final int LEFT_ENCODER_CHANNEL_A  = 0;
	public static final int LEFT_ENCODER_CHANNEL_B  = 1;
	public static final int RIGHT_ENCODER_CHANNEL_A = 2;
	public static final int RIGHT_ENCODER_CHANNEL_B = 3;	
	
	//Drivetrain
	//Slave motor should be master(id) plus 1
	public static final CANTalon LEFT_MASTER_MOTOR  = new CANTalon(0);
	public static final CANTalon LEFT_SLAVE_MOTOR   = new CANTalon(1);
	public static final CANTalon RIGHT_MASTER_MOTOR = new CANTalon(2);
	public static final CANTalon RIGHT_SLAVE_MOTOR  = new CANTalon(3);
	
	public static final boolean LEFT_DRIVE_REVERSED   = false;
	public static final boolean LEFT_ENCODER_REVERSED  = true;
	public static final boolean RIGHT_DRIVE_REVERSED  = true;
	public static final boolean RIGHT_ENCODER_REVERSED = false;
	
	public static final Encoder LEFT_WHEEL_ENCODER = new Encoder(LEFT_ENCODER_CHANNEL_A, 
			                                                     LEFT_ENCODER_CHANNEL_B,
			                                                     LEFT_ENCODER_REVERSED,
			                                                     CounterBase.EncodingType.k4X);
	public static final Encoder RIGHT_WHEEL_ENCODER = new Encoder(RIGHT_ENCODER_CHANNEL_A,
			                                                      RIGHT_ENCODER_CHANNEL_B,
			                                                      RIGHT_ENCODER_REVERSED,
			                                                      CounterBase.EncodingType.k4X);
	
	//Drivetrain Constants
	public static final double LEFT_MOTOR_SCALER = -1.0;
	public static final double RIGHT_MOTOR_SCALER = 1.0;
	
	public static final int DRIVETRAIN_RAMPING_FACTOR = 10;
	
	public static final double DEAD_ZONE_LIMIT = 0.3;
	public static final double TRIGGER_ACTIVE_THRESHOLD = 0.5;
	
	//Scalers
	public static final double FINESSE_GAIN = 0.6;
	public static final double TURN_GAIN    = 1.5;
	 
	//Solenoids
	public final static Solenoid SHIFT_SOLENOID = new Solenoid(0);
	
}
