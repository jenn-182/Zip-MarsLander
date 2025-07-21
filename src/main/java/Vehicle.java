public class Vehicle {


    // this is initial vehicle setup
    int Altitude= 8000;
    int PrevAltitude= 8000;
    int Velocity= 0; // initial velocity (descent)
    int Fuel = 12000; // initial fuel in tank


    int Gravity = 100;

    /* The rate in which the spaceship descents in free fall (in ten seconds) */

    // Various end-of-game messages and status result codes.
    String dead = "\nCRASH!!\n\tThere were no survivors.\n\n";
    public static final int DEAD = -3;
    String crashed = "\nThe Starship crashed. Good luck getting back home. Elon is pissed.\n\n";
    public static final int CRASHED = -2;
    String emptyfuel = "\nThere is no fuel left. You're floating around like Major Tom.\n\n";
    public static final int EMPTYFUEL = -1;
    String success = "\nYou made it! Good job!\n\n";
    public static final int SUCCESS = 0;
    public static final int FLYING = 1;


    int Burn = 0; //last burn amount
    int Flying = FLYING; // status of the vehicle, default is flying


  public Vehicle(int InitialAltitude) {

        // initialize the altitude AND previous altitude to initialAltitude
        this.Altitude = InitialAltitude;
        this.PrevAltitude = InitialAltitude;
        this.Velocity = 0; // initial velocity (descent)
    }

    
    public Vehicle() {}

    public String checkFinalStatus() {
        String s = "";
        if (this.Altitude <= 0) { // Vehicle has landed
            if (this.Velocity > 10) {
                s = dead; // Vehicle crashed - > high impact
                Flying = DEAD;
            }
            else if (this.Velocity > 2) { //Impact velocity between 2 and 10 m/s
                s = crashed;
                Flying = CRASHED;
            } else { // Safe landing -> impact velocity 1 or 2 m/s
                s = success;
                Flying = SUCCESS;
            }
        } else { // Vehicle is still flying
            s = emptyfuel;
            Flying = EMPTYFUEL;
        }
        return s;
    } 

    public int computeDeltaV(int burnAmount) {
        
        //computes the change in velocity based on the burn amount
        return 100 - burnAmount;
    }

    public void adjustForBurn(int burnAmount) {

        //set burn to burnamount requested
        this.Burn = burnAmount;
        
        // save previousAltitude with current Altitude
        this.PrevAltitude = this.Altitude;

        // set new velocity to result of computeDeltaV function.
        this.Velocity = this.Velocity + computeDeltaV(burnAmount);

        // don't allow negative velocity
        if (this.Velocity < 0) {
            this.Velocity = 0; 
        }

        // subtract speed from Altitude
        this.Altitude = this.Altitude - this.Velocity;

        // subtract burn amount fuel used from tank
        this.Fuel = this.Fuel - burnAmount;
        if (this.Fuel < 0) {
            this.Fuel = 0; // don't allow negative fuel
        }
    }

    // check if the vehicle is still flying
    public boolean stillFlying() {
        // return true if altitude is positive
        return this.Altitude > 0;
    }

    // check if the vehicle is out of fuel
    public boolean outOfFuel() {
        // return true if fuel is less than or equal to zero
        return this.Fuel <= 0;
    }

    //get the current status of the vehicle
    public DescentEvent getStatus(int tick) {
        // create a return a new DescentEvent object
        // filled in with the state of the vehicle.
        return new DescentEvent(tick, this.Velocity, this.Fuel, this.Altitude, this.Flying);
    }

}
