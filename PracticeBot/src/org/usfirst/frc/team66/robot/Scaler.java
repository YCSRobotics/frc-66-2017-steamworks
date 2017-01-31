package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

public class Scaler {

		private CANTalon masterMotor;
		private CANTalon slaveMotor;
		
		private double motorScaler;
		
		private double speed = 0.0;
		private int rampingFactor = Constants.DRIVETRAIN_RAMPING_FACTOR;
		
		public Scaler(CANTalon masterMotor, CANTalon slaveMotor, double motorScaler) {
			this.masterMotor = masterMotor;
			this.slaveMotor = slaveMotor;
			
			masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			slaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
			
			slaveMotor.set(masterMotor.getDeviceID());
			
			this.motorScaler = motorScaler;
			
		}

		public void set(double input) {
			if (Math.abs(input) < 0.25) {
				input = 0;
			}
			if (input > 0) {
				speedRamping(input * input);
			} else if (input < 0) {
				speedRamping(-(input * input));
			} else {
				speedRamping(0);
			}
			masterMotor.set(speed * motorScaler);
		}
		
		private void speedRamping(double input) {
			double tempSpeed = ((speed * (rampingFactor - 1)) + input) / rampingFactor;
			if (Math.abs(tempSpeed) < 0.01) {
				tempSpeed = 0;
			}
			speed = tempSpeed;
		}
}
