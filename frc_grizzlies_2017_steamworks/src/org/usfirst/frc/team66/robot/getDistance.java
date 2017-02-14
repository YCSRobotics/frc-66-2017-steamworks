package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.AnalogInput;




public class getDistance {

    // Distance values from sensor.
	private  final int kDistancePoints = 32;
	private final double kDistArray [] = {
    		260,   290,   295,   300,   310,   320,   340,   355,   360,    365,     370,    390,    410,    430,    440,    460,
    		490,   500,   540,   580,   620,   660,   710,   760,   820,    900,    1010,   1130,   1200,   1480,   1810,   2260};
    // Distances in inches.
    private final double kDistInches [] = {
    		 40,    38,    36,    34,    32,    30,    28,    27,    26,     25,      24,     23,     22,     21,     20,     19,
    		 18,    17,    16,    15,    14,    13,    12,    11,    10,      9,       8,      7,      6,      5,      4,      3};
    double distance = 0;
	AnalogInput aI = null;
	
	
	// RobotInit
    //aI = new AnalogInput(1);
	
	// Use: take 3 reading and average them.
    // double z = (aI.getAverageValue() + aI.getAverageValue() + aI.getAverageValue()) / 3.0; 
	// double distance = getDist(z);

	
	
	/**
     * Function: getDist
     * Converts the analog input value to whole inches.
	 * The argument d is the raw value from the analog distance sensor. 
	 *    getDist(aI.getAverageValue())
	 */
    private double getDist (double d) {
        int low;                                    // Low end of binary search.
        int high;                                   // High end of binary search.
        int mid;                                    // Bisection of current section.
        int old_mid;

        low = 0;                                    // Set initial record numbers for the binary search.
        high = kDistancePoints;                     // The actual number of records.

        mid = 0;
        old_mid = -1;                               // These must be different/

        while ((high != low) && (mid != old_mid)) { // Loop until found.

            old_mid = mid;
            mid = (high + low) / 2;                 // Find the middle record.

            if (d >= kDistArray [mid]) {
                low = mid;
            }
            else if (d < kDistArray [mid]) {
                high = mid;
            }
        }   // while

        return kDistInches [mid];
    } // GetDist  
	

} //public class getDistance {
