package org.firstinspires.ftc.teamcode;

/**
 * A class used to store the heights of the junctions and cone stacks for easier access to the values
 */
public class HeightsList { //heights are all in tics

    //junction heights:
    /**
     * An int used to represent the tic value of the lift at the height of the low junction, it is subject to change based off of the lift
     */
    int lowJunction = 683;
    /**
     * An int used to represent the tic value of the lift at the height of the medium junction, it is subject to change based off of the lift
     */
    int midJunction = 900;
    /**
     * An int used to represent the tic value of the lift at the height of the high junction, it is subject to change based off of the lift
     */
    int highJunction = 1366;
    /**
     * An int used to represent the tic value at the the height of the ground junction, it is subject to change based of the lift
     */
    int groundJunction = 400;

    //cone stack heights:
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with five cones, it is subject to change based of the lift
     */
    int fiveConeStack = 1316;
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with four cones, it is subject to change based of the lift
     */
    int fourConeStack = 1260;
    /**
     * An int used to represent the tic value of the lift at the height of cone stack with three cones, it is subject to change based of the lift
     */
    int threeConeStack = 1148;
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with two cones, it is subject to change based of the lift
     */
    int twoConeStack = 852;
    /**
     * An int used to represent the tic value of the lift at the height of one cone, it is subject to change based of the lift
     */
    int oneConeStack = 723;

    //extra height:
    int rightAboveACone = 0; //replace this number with the real value
}
