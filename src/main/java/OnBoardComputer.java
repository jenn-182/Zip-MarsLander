public class OnBoardComputer implements BurnStream {

    //target velocities for different altitudes
    // Final target landing velocity (within 1-2 m/s range)

    private static final double FINAL_TARGET_VELOCITY = 1.5; // Aim for 1.5 m/s

        // Buffer for minimum stopping altitude calculation, to give some margin for error
    private static final double ALTITUDE_BUFFER = 1.2; // Require 20% more altitude than theoretically needed

    // Altitude at which we start more aggressive braking, regardless of velocity
    private static final double CRITICAL_BRAKING_ALTITUDE = 300.0; // meters

    // Altitude for final, precise velocity control
    private static final double FINAL_CONTROL_ALTITUDE = 15.0; // meters


    //computes the burn amount based on the current status of the vehicle
    @Override
    public int getNextBurn(DescentEvent status) {

        double altitude = status.getAltitude();
        double velocity = status.getVelocity();
        int burn = 0; // default burn amount
        
        //if the vehicle has landed, no burn is needed
        if (altitude <= 0) {
            return 0;
        }

        double minAltitudeToStop = Math.max(0, (velocity * velocity - FINAL_TARGET_VELOCITY * FINAL_TARGET_VELOCITY) / 20.0);

        double bufferedMinAltitudeToStop = minAltitudeToStop * ALTITUDE_BUFFER;


        // if altitude is high and velocity is low, free fall
        if (altitude > bufferedMinAltitudeToStop * 1.5 && velocity < 60) {
            burn = 0; //free fall to gain speed
        }

        // if high altitude but velocity is high, start breaking
        else if (altitude > FINAL_CONTROL_ALTITUDE) {
              // Ensure altitude is never zero for division, and add a small buffer for stability
            double effectiveAltitude = Math.max(0.1, altitude);

            // Calculate required acceleration (m/s^2) to reach FINAL_TARGET_VELOCITY at ground.
            double a_required = (FINAL_TARGET_VELOCITY * FINAL_TARGET_VELOCITY - velocity * velocity) / (2.0 * effectiveAltitude);

            // Convert required acceleration to DeltaV per 10 seconds.
            double deltaV_required_per_10s = a_required * 10.0;

            // Convert required DeltaV to burn amount: burn = 100 - DeltaV
            burn = (int) Math.round(100 - deltaV_required_per_10s);

            // Override: If we are in the critical braking zone and still too fast, ensure max burn.
            if (altitude < CRITICAL_BRAKING_ALTITUDE && velocity > FINAL_TARGET_VELOCITY + 5) {
                burn = 200; // Force max burn to slow down quickly
            }
            // Override: If we are high up but moving very slowly, free fall to save fuel and descend.
            else if (altitude > 100 && velocity < 5) {
                burn = 0; // Free fall to gain speed and save fuel
            }
        }

        else {
            if (velocity > FINAL_TARGET_VELOCITY + 0.5) {
                burn = 200; // Max burn to slow down
            } else if (velocity < FINAL_TARGET_VELOCITY - 0.5) {
                burn = 0; // Free fall to slow down
            } else {
                burn = 100; // Maintain current speed
            }
        }

        //ensure burn is within the valid range (0,200)
        burn = Math.max(0, Math.min(200,burn)); // ensure burn is between 0 and 200

        //print the burn amount for debugging
        System.out.println(burn); /*hack!*/
        return burn; 
    }      
}

