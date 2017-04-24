package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearIntake {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Talon gearIntakeMotor = Constants.GEAR_INTAKE_MOTOR;
	
	private static Solenoid lowerIntakeSolenoid = Constants.LOWER_INTAKE_SOLENOID;
	private static Solenoid gearSensorSupply = Constants.GEAR_SENSOR_SUPPLY;
	
	private static DigitalInput gearSensor1 = Constants.GEAR_SENSOR_1;
	private static DigitalInput gearSensor2 = Constants.GEAR_SENSOR_2;
	
	private static boolean gearSensorPrev;
	private static boolean gearSensorDebounced;
	
	private static int gearSensorDebounceCount = 0;
	
	private boolean isIntakeLowered;
	private boolean isRaiseIntakePressed;

	//Auto Intake States
	final static int IDLE 	  = 0;
	final static int LOADING  = 1;
	final static int LOADED   = 2;
			
	private int autoIntakeState = IDLE;
	
	public GearIntake(){

	}
	
	public void updateGearIntakeTelopPeriodic(){
		
		double intakeSpeed;
		
		toggleRaiseIntake();
		
		//intakeSpeed = getGearInOutSpeed();
		
		if(controller.getRawButton(Constants.A_BUTTON)){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
		}
		else if(isIntakeGearButtonPressed()){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_INTAKE_SPEED);
		}
		else if(controller.getRawButton(Constants.Y_BUTTON)){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.FUEL_ROLL_OUT_SPEED);
		}
		else{
			gearIntakeMotor.set(0.0);
		}
		
		debounceGearSensor();
		
		SmartDashboard.putBoolean("Raw Gear Sensor 1", gearSensor1.get());
		SmartDashboard.putBoolean("Raw Gear Sensor 2", gearSensor2.get());
		SmartDashboard.putBoolean("Debounced Gear Sensor", gearSensorDebounced);
	}
	
	private void processAutoIntakeEvent(int event){
		
	}
	
	private boolean isRaiseIntakeButtonPressed(){
		if(controller.getRawAxis(Constants.LEFT_TRIGGER) >= Constants.TRIGGER_ACTIVE_THRESHOLD){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean isIntakeGearButtonPressed(){
		if(controller.getRawButton(Constants.A_BUTTON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean isIntakeAutoLoadButtonPressed(){
		//Stub
		return false;
	}
	
	private void toggleRaiseIntake(){
		if((isRaiseIntakeButtonPressed()) &&
		   (!isRaiseIntakePressed)){
			
			//Button transitions from false to true, set button active flag	
			isRaiseIntakePressed = true;
					
		    if(isIntakeLowered){
		    	//Intake is lowered, so close and raise intake (solenoid off)
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
	
	private void debounceGearSensor(){	
		boolean raw_sensor_input;
		
		raw_sensor_input = gearSensor1.get();
		
		if(raw_sensor_input != gearSensorPrev){
			//Detect change in sensor state		
			gearSensorPrev = raw_sensor_input;
			
			gearSensorDebounceCount = Constants.GEAR_SENSOR_DEBOUNCE_TIME;
		}
		else if(gearSensorDebounceCount >= 20){
			//Decrement the counter
			gearSensorDebounceCount--;
		}
		else{
			//Timer expired
			gearSensorDebounced = raw_sensor_input;
		}	
	}
	
	public static boolean getDebouncedGearSensor(){
		return gearSensorDebounced;
	}
	
	public static void commandGearEject(boolean command){
		if(command){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
		}
		else
		{
			gearIntakeMotor.set(0.0);
		}
	}
	
	public static void enableGearSensor(boolean enable){
		gearSensorSupply.set(enable);
	}
	


}