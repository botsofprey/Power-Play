package org.firstinspires.ftc.teamcode.vars;

/**
 * A class used to store the heights of the junctions and cone stacks for easier access to the values
 */
public class HeightsList { //heights are all in tics
    //calculation values:
    double currentSpoolDiameter = 47.2,
            currentMotorRatio = 312,
            motorRatio = 1150 / currentMotorRatio,
            spoolRatio = 47.2 / currentSpoolDiameter,
            ratio = spoolRatio * motorRatio;

    //junction heights:
    /**
     * An int used to represent the tic value of the lift at the height of the low junction, it is subject to change based off of the lift
     */
    public int lowJunction = (int) Math.round(430 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of the medium junction, it is subject to change based off of the lift
     */
    public int midJunction = (int) Math.round(685 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of the high junction, it is subject to change based off of the lift
     */
    public int highJunction = (int) Math.round(950 * ratio);
    /**
     * An int used to represent the tic value at the the height of the ground junction, it is subject to change based of the lift
     */
    public int groundJunction = (int) Math.round(190 * ratio);

    //cone stack heights:
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with five cones, it is subject to change based of the lift
     */
    int fiveConeStack = (int) Math.round(160 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with four cones, it is subject to change based of the lift
     */
    int fourConeStack = (int) Math.round(100 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of cone stack with three cones, it is subject to change based of the lift
     */
    int threeConeStack = (int) Math.round(95 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of the cone stack with two cones, it is subject to change based of the lift
     */
    int twoConeStack = (int) Math.round(60 * ratio);
    /**
     * An int used to represent the tic value of the lift at the height of one cone, it is subject to change based of the lift
     */
    int oneConeStack = (int) Math.round(0 * ratio);

    public int[] heights = {fiveConeStack, fourConeStack, threeConeStack, twoConeStack, oneConeStack};

    //extra height:
    public int rightAboveACone = 0; //replace this number with the real value
}
