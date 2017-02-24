package org.usfirst.frc.team66.robot;

public class PiMath {
	
	static double getImageSizeInDeg() {
		double nPixels = NetworkTables.CenterOfTarget()[0];
		
		return (nPixels * Constants.DEG_PER_PIXEL);
	}
	
	public static double getTargetDistance() {
		double distance;
		double radians = Math.toRadians(getImageSizeInDeg());
		distance = ((5.25)/(Math.tan(radians)));
		
		return distance; //returns distance to target in inches
	}
	
	public static double angleToTarget() {
		double degAngle = ((NetworkTables.CenterOfTargetCoords() - 
				(Constants.CAMERA_WIDTH/2)) * Constants.DEG_PER_PIXEL);
		
		return degAngle; //returns the angle to target
	}
}

//how to use, simply call the method and you'll have your value