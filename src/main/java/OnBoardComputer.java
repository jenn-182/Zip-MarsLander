public class OnBoardComputer implements BurnStream {

    //target velocities for different altitudes
    // Final target landing velocity (within 1-2 m/s range)

    //computes the burn amount based on the current status of the vehicle
    @Override
    public int getNextBurn(DescentEvent status) {
        double altitude = status.getAltitude();
        double velocity = status.getVelocity();
        int burn = 0;

        if (altitude <= 0) {
            return 0;
        }

        // Above 3000m -> only burn if velocity > 150
        if (altitude > 3000) {
            if (velocity > 150) {
                burn = 150;
            } else if (velocity > 100) {
                burn = 100;
            } else {
                burn = 0;
            }
        }
        // 3000m to 1000m -> burn if velocity > 100
        else if (altitude > 1000) {
            if (velocity > 100) {
                burn = 150;
            } else if (velocity > 50) {
                burn = 100;
            } else {
                burn = 0;
            }
        }
        // 1000m to 500m -> burn if velocity > 50
        else if (altitude > 500) {
            if (velocity > 50) {
                burn = 150;
            } else if (velocity > 30) {
                burn = 100;
            } else {
                burn = 0;
            }
        }
        // 500m to 100m -> burn if velocity > 20
        else if (altitude > 100) {
            if (velocity > 20) {
                burn = 150;
            } else if (velocity > 10) {
                burn = 100;
            } else {
                burn = 0;
            }
        }
        // 100m to 20m -> burn if velocity > 5
        else if (altitude > 20) {
            if (velocity > 5) {
                burn = 200;
            } else if (velocity > 2) {
                burn = 175;
            } else {
                burn = 0;
            }
        }
        // Final 20m -> burn if velocity > 2
        else {
            if (velocity > 2) {
                burn = 200;
            } else {
                burn = 0;
            }
        }

        burn = Math.max(0, Math.min(200, burn));
        return burn;
    }      
}

