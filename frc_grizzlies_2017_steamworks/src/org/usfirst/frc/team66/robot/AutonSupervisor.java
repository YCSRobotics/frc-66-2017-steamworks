package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonSupervisor {
	
	//Autonomous Routines
	final static int DO_NOTHING        = 0;
	final static int CROSS_BASELINE    = 1;
	final static int PLACE_CENTER_GEAR = 2;
	final static int PLACE_RIGHT_GEAR  = 3;
	final static int PLACE_LEFT_GEAR   = 4;
	
	//Autonomous States
	final static int START        			    = 0;
	final static int MOVE_DISTANCE 			    = 1;
	final static int MOVE_DISTANCE_TRACK_TARGET = 2;
	final static int TURN_TO_TARGET 		    = 3;
	final static int DELAY_AFTER_TURN			= 4;
	final static int DELAY_AFTER_GEAR			= 5;
	final static int BACK_UP					= 6;
	final static int STOP					    = 7;
	
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
				Drivetrain.setMoveDistance(96.0, 0.25);
				currentAutonState = MOVE_DISTANCE;
			}
			else if((selectedAutonRoutine == PLACE_RIGHT_GEAR) ||
					(selectedAutonRoutine == PLACE_LEFT_GEAR)){
				Drivetrain.setMoveDistance(60.0, 0.25);
				currentAutonState = MOVE_DISTANCE;
			}
			else if(selectedAutonRoutine == PLACE_CENTER_GEAR){
				Drivetrain.setMoveToVisionTarget(Constants.AUTON_THROTTLE_VALUE);
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
		if((selectedAutonRoutine == CROSS_BASELINE)  ||
		   (selectedAutonRoutine == PLACE_RIGHT_GEAR)||
		   (selectedAutonRoutine == PLACE_LEFT_GEAR)){
			if(!Drivetrain.isMovingDistance()){
				//Move is complete, go to next state
				if(selectedAutonRoutine == PLACE_RIGHT_GEAR){
					Drivetrain.setTurnToTarget(-0.4);
					currentAutonState = TURN_TO_TARGET;
				}
				else if(selectedAutonRoutine == PLACE_LEFT_GEAR){
					Drivetrain.setTurnToTarget(0.4);
					currentAutonState = TURN_TO_TARGET;
				}
				else
				{
					//CROSS BASELINE
					currentAutonState = STOP;
				}
			}
			else{
				//Do nothing and wait for move to complete
			}
		}
		else{
			//Should never get here
			currentAutonState = STOP;
		}
	}
	
	private void stateActionMoveDistanceTrackTarget(){
		if((selectedAutonRoutine == PLACE_CENTER_GEAR) ||
		   (selectedAutonRoutine == PLACE_LEFT_GEAR)   ||
		   (selectedAutonRoutine == PLACE_RIGHT_GEAR)){
			
			if(!Drivetrain.isMovingToVisionTarget()){
				GearIntake.openIntake();
				setAutonDelay(500);
				currentAutonState = DELAY_AFTER_GEAR;
			}
			else{
				//Do nothing and wait
			}
				
		}
	}
	
	private void stateActionTurnToTarget(){
		if((selectedAutonRoutine == PLACE_LEFT_GEAR)   ||
		   (selectedAutonRoutine == PLACE_RIGHT_GEAR)){
			
			if(!Drivetrain.isTurningToVisionTarget()){
				//Drivetrain.setMoveToVisionTarget(0.20);
				//currentAutonState = MOVE_DISTANCE_TRACK_TARGET;
				setAutonDelay(500);
				currentAutonState = DELAY_AFTER_TURN;
			  }
			else{
				//Do nothing and wait
			}
						
		}
	}
	
	private void stateActionDelayAfterGear()
	{
		if((selectedAutonRoutine == PLACE_CENTER_GEAR) ||
		   (selectedAutonRoutine == PLACE_LEFT_GEAR)   ||
		   (selectedAutonRoutine == PLACE_RIGHT_GEAR))
		{
			
			if(autonDelayCount <= 20){
				autonDelayCount = 0;
				Drivetrain.setMoveDistance(-18, -0.15);
				Drivetrain.zeroGyro();
				currentAutonState = BACK_UP;
			}
			else
			{
				autonDelayCount = autonDelayCount-20; 
			}
		}
		else
		{
			currentAutonState = STOP;
		}
	}
	
	private void stateActionDelayAfterTurn()
	{
		if((selectedAutonRoutine == PLACE_LEFT_GEAR)   ||
		   (selectedAutonRoutine == PLACE_RIGHT_GEAR))
		{
			
			if(autonDelayCount <= 20){
				autonDelayCount = 0;
				Drivetrain.setMoveToVisionTarget(0.20);
				currentAutonState = MOVE_DISTANCE_TRACK_TARGET;
			}
			else
			{
				autonDelayCount = autonDelayCount-20; 
			}
		}
		else
		{
			currentAutonState = STOP;
		}
	}
	
	private void stateActionBackUp(){
		if(!Drivetrain.isMovingDistance()){
			GearIntake.closeIntake();
			currentAutonState = STOP;
		}
	}
	
	private void stateActionStop(){
		Drivetrain.setMoveDistance(0.0, 0.0);
	}
	
	private void setAutonDelay(int delay){
		autonDelayCount = delay;
	}

}
