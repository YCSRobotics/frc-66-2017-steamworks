package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Constants {
	
	//Real World Constants
	public static final double PI = 3.14159;
	public static final double G  = 9.81; //acceleration due to Earth's gravity in m/s^2
	
	//Controllers
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0); //Drive
	public static final Joystick OP_CONTROLLER = new Joystick(1); //Operator
	
	public static final int LEFT_STICK_Y  = 1;
	public static final int RIGHT_STICK_X = 4;
	public static final int RIGHT_TRIGGER = 3;//Shift Button
	public static final int LEFT_TRIGGER  = 2;//Finesse Mode
	public static final int BACK_BUTTON   = 7;//Invert
	public static final int A_BUTTON      = 1;//Drive Straight
	
	public static final double DEAD_ZONE_LIMIT = 0.3;
	public static final double TRIGGER_ACTIVE_THRESHOLD = 0.5;
	
	//DIO	
	
	//Drivetrain Config
	public static final CANTalon LEFT_MASTER_MOTOR  = new CANTalon(0);
	public static final CANTalon LEFT_SLAVE_MOTOR   = new CANTalon(1);
	public static final CANTalon RIGHT_MASTER_MOTOR = new CANTalon(2);
	public static final CANTalon RIGHT_SLAVE_MOTOR  = new CANTalon(3);
	
	public static final boolean LEFT_DRIVE_REVERSED   = true;
	public static final boolean RIGHT_DRIVE_REVERSED  = false;
	
	public static final double MIN_TALON_RAMP_RATE = 1.173;//This is the min ramp rate per CTRE documentation
	public static final boolean RAMP_ENABLED       = true;
	public static final double DRIVE_RAMP_TIME     = 0.25;//This is the time to ramp from 0V to +/-12V in sec
	public static final double DRIVE_RAMP_RATE     = (RAMP_ENABLED ? (Math.max(MIN_TALON_RAMP_RATE, 
													 	(12/DRIVE_RAMP_TIME))) : 0.0);
	
	public static final double FINESSE_GAIN = 0.6;
	public static final double TURN_GAIN    = 1.0;
	
	//Encoder Config
	public static final int LEFT_ENCODER_CHANNEL_A  = 0;
	public static final int LEFT_ENCODER_CHANNEL_B  = 1;
	public static final int RIGHT_ENCODER_CHANNEL_A = 2;
	public static final int RIGHT_ENCODER_CHANNEL_B = 3;
	
	public static final boolean LEFT_ENCODER_REVERSED  = true;
	public static final boolean RIGHT_ENCODER_REVERSED = false;
	
	public static final double ENCODER_PULSES_PER_REV     = 360; //This comes from the datasheet.
	public static final double WHEEL_DIAMETER             = 6.0; //diameter of wheels in inches. Practice bot 6", Comp bot
	public static final double DRIVETRAIN_MULTIPLIER      = 5.4; // Ratio of encoder revs to wheel revs, 54:30 x 36:12
	public static final double ENCODER_DISTANCE_PER_PULSE = (WHEEL_DIAMETER*PI)/(ENCODER_PULSES_PER_REV*DRIVETRAIN_MULTIPLIER);
	
	public static final Encoder LEFT_WHEEL_ENCODER = new Encoder(LEFT_ENCODER_CHANNEL_A, 
			                                                     LEFT_ENCODER_CHANNEL_B,
			                                                     LEFT_ENCODER_REVERSED,
			                                                     CounterBase.EncodingType.k4X);
	public static final Encoder RIGHT_WHEEL_ENCODER = new Encoder(RIGHT_ENCODER_CHANNEL_A,
			                                                      RIGHT_ENCODER_CHANNEL_B,
			                                                      RIGHT_ENCODER_REVERSED,
			                                                      CounterBase.EncodingType.k4X);
	 
	//Solenoids
	public final static Solenoid SHIFT_SOLENOID = new Solenoid(0);
	
	//Gyro
	public final static ADXRS450_Gyro GYRO = new ADXRS450_Gyro();
}
