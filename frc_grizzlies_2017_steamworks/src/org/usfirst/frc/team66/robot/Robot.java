package org.usfirst.frc.team66.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	final String defaultAuto = "Default";
	final String crossBaselineAuto = "Cross Baseline";
	final String placeCenterGearAuto = "Place Center Gear";
	final String placeRightGearAuto = "Place Right Gear";
	final String placeLeftGearAuto = "Place Left Gear";
	final String redGearAndFuel = "Red Gear and Fuel";
	final String blueGearAndFuel = "Blue Gear and Fuel";
	
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	public static Drivetrain DRIVETRAIN;
	public static GearIntake GEAR_INTAKE;
	public static Fuel FUEL;
	public static Climber CLIMBER;
	public static AutonSupervisor AUTON_SUPERVISOR;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Do Nothing", defaultAuto);
		chooser.addObject("Cross Baseline", crossBaselineAuto);
		chooser.addObject("Place Center Gear", placeCenterGearAuto);
		chooser.addObject("Place Right Gear", placeRightGearAuto);
		chooser.addObject("Place Left Gear", placeLeftGearAuto);
		chooser.addObject("Red Side Gear & Fuel", redGearAndFuel);
		chooser.addObject("Blue Side Gear & Fuel", blueGearAndFuel);
		
		SmartDashboard.putData("Auto choices", chooser);
		
		//set up USB camera server
		try{
			//UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("cam1", 1);
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setFPS(7);
			camera.setResolution(320, 280);
		}
		catch(Exception e){
			
		}
		
		AUTON_SUPERVISOR = new AutonSupervisor();
		DRIVETRAIN = new Drivetrain();
		GEAR_INTAKE = new GearIntake();
		CLIMBER = new Climber();
		FUEL = new Fuel();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		
		//Select Autonomous Routine from Dashboard
		switch (autoSelected) {
		case crossBaselineAuto:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.CROSS_BASELINE);
			break;
		case placeCenterGearAuto:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.PLACE_CENTER_GEAR);
			break;
		case placeRightGearAuto:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.PLACE_RIGHT_GEAR);
			break;
		case placeLeftGearAuto:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.PLACE_LEFT_GEAR);
			break;
		case redGearAndFuel:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.RED_GEAR_AND_BOILER);
			break;
		case blueGearAndFuel:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.BLUE_GEAR_AND_BOILER);
			break;
		case defaultAuto:
		default:
			AUTON_SUPERVISOR.setSelectedAutonRoutine(AUTON_SUPERVISOR.DO_NOTHING);
			break;
		}
		
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		AUTON_SUPERVISOR.updateAutonomous();
		DRIVETRAIN.updateDrivetrainAuton();
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		DRIVETRAIN.updateDrivetrainTeleop();
		GEAR_INTAKE.updateGearIntakeTelopPeriodic();
		FUEL.updateFuelTelop();
		CLIMBER.updateClimberTeleop();
		//NetworkTables.getPiValues();
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

