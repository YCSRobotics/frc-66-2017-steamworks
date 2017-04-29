package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearIntake {
	
	private static Joystick controller = Constants.OP_CONTROLLER;
	
	private static Talon gearIntakeMotor = Constants.GEAR_INTAKE_MOTOR;
	
	private static Solenoid lowerIntakeSolenoid = Constants.LOWER_INTAKE_SOLENOID;
	private static Solenoid gearSensorSupply = Constants.GEAR_SENSOR_SUPPLY;
	
	private static DigitalInput gearSensor1 = Constants.GEAR_SENSOR_1;
	private static DigitalInput gearSensor2 = Constants.GEAR_SENSOR_2;
	
	private static Relay lights = Constants.LIGHT_RELAY;
	
	private boolean isIntakeLowered;
	private boolean isRaiseIntakePressed;
	
	private boolean isAutoIntakePressed;
	
	private int autoIntakeLoadDelayCount = 0;

	//Auto Intake States
	final static int IDLE 	    = 0;
	final static int LOADING    = 1;
	final static int LOAD_DELAY = 2;
	final static int LOADED     = 3;
	
	//Auto Intake Events
	final static int NONE 				  = 0;
	final static int AUTO_INTAKE_PRESSED  = 1;
	final static int AUTO_INTAKE_RELEASED = 2;
	final static int EJECT_GEAR			  = 3;
			
	private int autoIntakeState = IDLE;
	private int autoIntakeEvent = NONE;
	
	public GearIntake(){

	}
	
	public void updateGearIntakeTelopPeriodic(){
		
		double intakeSpeed;
		
		toggleRaiseIntake();
		
		//intakeSpeed = getGearInOutSpeed();
		
		if(isEjectGearButtonPressed()){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
			isAutoIntakePressed = false;
			processAutoIntakeEvent(EJECT_GEAR);
		}
		else if(isIntakeGearButtonPressed()){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_INTAKE_SPEED);
			isAutoIntakePressed = false;
			processAutoIntakeEvent(AUTO_INTAKE_RELEASED);
		}
		else if(controller.getRawButton(Constants.Y_BUTTON)){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.FUEL_ROLL_OUT_SPEED);
			isAutoIntakePressed = false;
			processAutoIntakeEvent(AUTO_INTAKE_RELEASED);
		}
		else if(isIntakeAutoLoadButtonPressed())
		{
			if(!isAutoIntakePressed){
				//Inactive to active transition
				isAutoIntakePressed = true;
				processAutoIntakeEvent(AUTO_INTAKE_PRESSED);
			}
			else{
				//Do nothing
			}
		}
		else{
			gearIntakeMotor.set(0.0);
			isAutoIntakePressed = false;
			processAutoIntakeEvent(AUTO_INTAKE_RELEASED);
		}
		
		//updateLights();
		//debounceGearSensor();
		
		//updateAutoIntakeState();
		
		SmartDashboard.putBoolean("Raw Gear Sensor 1", gearSensor1.get());
		SmartDashboard.putBoolean("Raw Gear Sensor 2", gearSensor2.get());
	}
	
	private void processAutoIntakeEvent(int event){
		autoIntakeEvent = event;
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
	
	private boolean isEjectGearButtonPressed(){
		return controller.getRawButton(Constants.B_BUTTON); 
	}
	
	private boolean isIntakeAutoLoadButtonPressed(){
		return controller.getRawButton(Constants.LEFT_BUMPER);
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
	

	
	public static void commandGearEject(boolean command){
		if(command){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_EJECT_SPEED);
			//gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * -0.40);
		}
		else
		{
			gearIntakeMotor.set(0.0);
		}
	}
	
	//Gear Sensor Logic
	public static void enableGearSensor(boolean enable){
		gearSensorSupply.set(enable);
	}
	
	public static boolean getGearSensorInput(){
		return !gearSensor1.get();
	}

	// Light logic
	private void updateLights(){
		boolean sensor;
		
		sensor = GearIntake.getGearSensorInput();
		
		if(sensor){
			lights.set(Value.kForward);
		}
		else
		{
			lights.set(Value.kOff);
		}
	}
	
	//Auto Intake Logic
	
	private void updateAutoIntakeState(){
		switch(autoIntakeState){
		
		case LOADING:
			autoIntakeLoadingStateAction();
			break;
		case LOAD_DELAY:
			autoIntakeLoadDelayStateAction();
			break;
		case LOADED:
			autoIntakeLoadedStateAction();
			break;
		case IDLE:
		default:
			autoIntakeIdleStateAction();
			break;
		}
	}
	private void autoIntakeIdleStateAction(){
		if(autoIntakeEvent == AUTO_INTAKE_PRESSED){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_INTAKE_SPEED);
			lowerIntakeSolenoid.set(true);
			autoIntakeState = LOADING;
		}
		else
		{
			//Do nothing and wait for button press
		}
	}
	
	private void autoIntakeLoadingStateAction(){
		if(autoIntakeEvent == AUTO_INTAKE_RELEASED){
			gearIntakeMotor.set(Constants.GEAR_INTAKE_MOTOR_DIRECTION * Constants.GEAR_INTAKE_SPEED);
			lowerIntakeSolenoid.set(false);
			autoIntakeState = IDLE;
		}
		else if(getGearSensorInput()){
			autoIntakeLoadDelayCount = 250;
			autoIntakeState = LOAD_DELAY;
		}
		else{
			//Wait for next event
		}
	}
	
	private void autoIntakeLoadDelayStateAction(){
		if(autoIntakeLoadDelayCount <= 20){
			autoIntakeLoadDelayCount = 0;
			gearIntakeMotor.set(0.0);
			lowerIntakeSolenoid.set(false);
			autoIntakeState = LOADED;
		}
		else{
			autoIntakeLoadDelayCount = autoIntakeLoadDelayCount-20;
		}
	}
	
	private void autoIntakeLoadedStateAction(){
		if((autoIntakeEvent == EJECT_GEAR)||
		   (!getGearSensorInput())){
			autoIntakeState = IDLE;
		}
		else{
			//Wait for gear to be ejected
		}
	}


}