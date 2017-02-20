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
	final static int STOP					    = 4;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = 0;
	
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
		case TURN_TO_TARGET:
		case STOP:
		default:
			stateActionStop();	
		}
		
		updateAutonDashboard();
	}
	
	private void updateAutonDashboard(){
		SmartDashboard.putNumber("Selected Routine", selectedAutonRoutine);
	}
	
	//State Action Methods
	private void stateActionStart(){
		if(selectedAutonRoutine != DO_NOTHING){
			if(selectedAutonRoutine == CROSS_BASELINE){
				Drivetrain.setMoveDistance(96.0, 0.15);
				currentAutonState = MOVE_DISTANCE;
			}
			else{
				currentAutonState = STOP;
			}
		}else{
			//Selected Routine is DO_NOTHING
			currentAutonState = STOP;
		}
	}
	
	private void stateActionMoveDistance(){
		if(selectedAutonRoutine == CROSS_BASELINE){
			if(!Drivetrain.isMovingDistance()){
				//Move is complete, go to STOP state
				currentAutonState = STOP;
			}
			else{
				//Do nothing and wait for move to complete
			}
		}else
		{
			currentAutonState = STOP;
		}
	}
	
	private void stateActionMoveDistanceTrackCamera(){
		//TODO
	}
	
	private void stateActionTurnToTarget(){
		//
	}
	
	private void stateActionStop(){
		Drivetrain.setMoveDistance(0.0, 0.0);
	}

}
