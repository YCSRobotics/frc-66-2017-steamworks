package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Constants {
	
	//Controllers
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0); //Drive
	
	//Drivetrain
	//Slave motor should be master(id) plus 1
	public static final CANTalon LEFT_MASTER_MOTOR = new CANTalon(0);
	public static final CANTalon LEFT_SLAVE_MOTOR = new CANTalon(1);
	public static final CANTalon RIGHT_MASTER_MOTOR = new CANTalon(2);
	public static final CANTalon RIGHT_SLAVE_MOTOR = new CANTalon(3);
	public static final double LEFT_MOTOR_SCALER = -1.0;
	public static final double RIGHT_MOTOR_SCALER = 1.0;
	public static final int DRIVETRAIN_RAMPING_FACTOR = 10;
	
	//Scalers
	public static final double TURBO_SCALER = 1.0;
	 
	//Solenoids
	public final static Solenoid SHIFTSOLENOID = new Solenoid(0);
	
}
