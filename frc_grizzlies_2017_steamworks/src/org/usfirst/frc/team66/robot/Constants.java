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
	
	public static final double MIN_TALON_RAMP_RATE = 1.173;//This is the min ramp rate per CTRE documentation
	
	public static final boolean RAMP_ENABLED       = true;
	public static final double DRIVE_RAMP_TIME     = 0.25;//This is the time to ramp from 0V to +/-12V in sec
	public static final double DRIVE_RAMP_RATE     = (RAMP_ENABLED ? (Math.max(MIN_TALON_RAMP_RATE, 
													 	(12/DRIVE_RAMP_TIME))) : 0.0);
	
	public static final double DEAD_ZONE_LIMIT = 0.3;
	public static final double TRIGGER_ACTIVE_THRESHOLD = 0.5;
	
	//Scalers
	public static final double FINESSE_GAIN = 0.6;
	public static final double TURN_GAIN    = 1.5;
	 
	//Solenoids
	public final static Solenoid SHIFT_SOLENOID = new Solenoid(0);
	
}
