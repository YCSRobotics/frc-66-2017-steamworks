package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonSupervisor {
	
	//Autonomous Routines
	final static int DO_NOTHING           = 0;
	final static int CROSS_BASELINE       = 1;
	final static int PLACE_CENTER_GEAR    = 2;
	final static int PLACE_RIGHT_GEAR     = 3;
	final static int PLACE_LEFT_GEAR   	  = 4;
	final static int RED_GEAR_AND_BOILER  = 5;
	final static int BLUE_GEAR_AND_BOILER = 6;
	
	//Autonomous States
	final static int START        			    = 0;
	final static int MOVE_DISTANCE 			    = 1;
	final static int MOVE_DISTANCE_TRACK_TARGET = 2;
	final static int TURN_TO_TARGET 		    = 3;
	final static int DELAY_AFTER_TURN			= 4;
	final static int DELAY_AFTER_GEAR			= 5;
	final static int BACK_UP					= 6;
	final static int TURN_TO_BOILER				= 7;
	final static int TURN_TO_BOILER_DELAY		= 8;
	final static int MOVE_TO_BOILER				= 9;
	final static int STOP					    = 10;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = 0;
	
	public static int autonDelayCount = 0;
	
	public AutonSupervisor(){		
									
	}
	
	public void setSelectedAutonRoutine(int routine){
		selectedAutonRoutine = routine;
	}
	
	public void updateAutonomous(){
		
		switch(currentAutonState){
		
		case START:
			stateActionStart();
			break;
		case MOVE_DISTANCE:
			stateActionMoveDistance();
			break;
		case MOVE_DISTANCE_TRACK_TARGET:
			stateActionMoveDistanceTrackTarget();
			break;
		case TURN_TO_TARGET:
			stateActionTurnToTarget();
			break;
		case DELAY_AFTER_TURN:
			stateActionDelayAfterTurn();
			break;
		case DELAY_AFTER_GEAR:
			stateActionDelayAfterGear();
			break;
		case BACK_UP:
			stateActionBackUp();
			break;
		case TURN_TO_BOILER:
			stateActionTurnToBoiler();
			break;
		case TURN_TO_BOILER_DELAY:
			stateActionTurnToBoilerDelay();
			break;
		case MOVE_TO_BOILER:
			stateActionMoveToBoiler();
			break;
		case STOP:
		default:
			stateActionStop();	
		}
		
		updateAutonDashboard();
	}
	
	private void updateAutonDashboard(){
		SmartDashboard.putNumber("Selected Routine", selectedAutonRoutine);
		SmartDashboard.putNumber("Current Auton State", currentAutonState);
	}
	
	//State Action Methods
	private void stateActionStart(){
		if(selectedAutonRoutine != DO_NOTHING){
			if(selectedAutonRoutine == CROSS_BASELINE){
				Drivetrain.zeroGyro();
				Drivetrain.setMoveDistance(96.0, 0.25);
				currentAutonState = MOVE_DISTANCE;
			}
			else if((selectedAutonRoutine == PLACE_RIGHT_GEAR)    ||
					(selectedAutonRoutine == PLACE_LEFT_GEAR)     ||
					(selectedAutonRoutine == RED_GEAR_AND_BOILER) ||
					(selectedAutonRoutine == BLUE_GEAR_AND_BOILER)){
				Drivetrain.zeroGyro();
				Drivetrain.setDrivetrainBraking(true);
				Drivetrain.setMoveDistance(72.0, 0.45);
				currentAutonState = MOVE_DISTANCE;
			}
			else if(selectedAutonRoutine == PLACE_CENTER_GEAR){
				Drivetrain.zeroGyro();
				Drivetrain.setMoveToVisionTarget(110.0, Constants.AUTON_THROTTLE_VALUE);
				currentAutonState = MOVE_DISTANCE_TRACK_TARGET;
			}
			else{
				currentAutonState = STOP;
			}
		}
		else{
			//Selected Routine is DO_NOTHING
			currentAutonState = STOP;
		}
	}
	
	private void stateActionMoveDistance(){
		if(!Drivetrain.isMovingDistance()){
			//Move is complete, go to next state
			if((selectedAutonRoutine == PLACE_RIGHT_GEAR) ||
			   (selectedAutonRoutine == RED_GEAR_AND_BOILER))
			{
				Drivetrain.setTurnToTarget(-0.4, Constants.TURN_TO_TARGET_ANGLE);
				currentAutonState = TURN_TO_TARGET;
			}
			else if((selectedAutonRoutine == PLACE_LEFT_GEAR) ||
			        (selectedAutonRoutine == BLUE_GEAR_AND_BOILER))
			{
				Drivetrain.setTurnToTarget(0.4, Constants.TURN_TO_TARGET_ANGLE);
				currentAutonState = TURN_TO_TARGET;
			}
			else
			{
				//CROSS BASELINE
				currentAutonState = STOP;
			}
		}else{
				//Do nothing and wait for move to complete
		}
	}
	
	private void stateActionMoveDistanceTrackTarget(){
		if(!Drivetrain.isMovingToVisionTarget()){
			GearIntake.commandGearEject(true);
			setAutonDelay(500);
			currentAutonState = DELAY_AFTER_GEAR;
		}
		else{
			//Do nothing and wait
		}			
	}
	
	private void stateActionTurnToTarget(){
		if(!Drivetrain.isTurningToVisionTarget()){
			setAutonDelay(500);
			currentAutonState = DELAY_AFTER_TURN;
		}
		else{
			//Do nothing and wait
		}
	}
	
	private void stateActionDelayAfterGear()
	{
		if(autonDelayCount <= 20){
			autonDelayCount = 0;
			GearIntake.commandGearEject(false);
			Drivetrain.zeroGyro();
			Drivetrain.setMoveDistance(-30, -0.45);
			currentAutonState = BACK_UP;
		}
		else
		{
			autonDelayCount = autonDelayCount-20; 
		}
	}
	
	private void stateActionDelayAfterTurn()
	{
		if(autonDelayCount <= 20){
			autonDelayCount = 0;
			Drivetrain.setMoveToVisionTarget(65.0, 0.30);
			currentAutonState = MOVE_DISTANCE_TRACK_TARGET;
		}
		else
		{
			autonDelayCount = autonDelayCount-20; 
		}
	}
	
	private void stateActionBackUp(){
		if(!Drivetrain.isMovingDistance()){
			
			GearIntake.commandGearEject(false);
			
			if (selectedAutonRoutine == RED_GEAR_AND_BOILER)
			{
				Drivetrain.setTurnToTarget(0.4, Constants.TURN_TO_BOILER_ANGLE);
				currentAutonState = TURN_TO_BOILER;
			}
			else if (selectedAutonRoutine == BLUE_GEAR_AND_BOILER)
			{			
				Drivetrain.setTurnToTarget(-0.4, Constants.TURN_TO_BOILER_ANGLE);
				currentAutonState = TURN_TO_BOILER;
			}
			else
			{
				//Center Gear, Left Gear, or Right Gear
				Drivetrain.setDrivetrainBraking(false);
				currentAutonState = STOP;
			}
			
		}
		else
		{
			//Wait for move to complete
		}
	}
	
	private void stateActionTurnToBoiler(){
		if(!Drivetrain.isTurningToVisionTarget())
		{
			setAutonDelay(500);
			Drivetrain.zeroGyro();
			currentAutonState = TURN_TO_BOILER_DELAY;
		}
		else{
			//Do nothing and wait
		}							
	}
	
	private void stateActionTurnToBoilerDelay()
	{
		if(autonDelayCount <= 20){
			autonDelayCount = 0;
			Drivetrain.setMoveDistance(-48.0, -0.35);
			currentAutonState = MOVE_TO_BOILER;
		}
		else
		{
			autonDelayCount = autonDelayCount-20; 
		}
	}
	
	private void stateActionMoveToBoiler(){
		if(!Drivetrain.isMovingDistance()){
			Fuel.commandFuelDumpSolenoid(true);
			Drivetrain.setDrivetrainBraking(false);
			currentAutonState = STOP;
		}
		else{
			//Wait for move to complete
		}
		
	}
	
	private void stateActionStop(){
		Drivetrain.setMoveDistance(0.0, 0.0);
	}
	
	private void setAutonDelay(int delay){
		autonDelayCount = delay;
	}

}
