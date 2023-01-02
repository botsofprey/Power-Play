package org.firstinspires.ftc.teamcode.mecanumDrive;

import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;

public class driveVariables {
    //amount of ticks in a single full revolution of the wheel
    public static final double TICKS_PER_REV = 537.7;
    //the length of the 24x24 inch foam tiles on the field in ticks
    public static final double BLOCK_LENGTH = 1082.029;
    //the amount of wheel turns required to fully turn the robot in ticks
    //(136.973 cm, the circumference a single wheel travels during a full
    // wheel turn, divided by the wheel circumference 30.159 cm, the robot
    //takes about 4.54 wheel rotations for a full 360 degree turn.)
    public static final double COMPLETE_TURN_ROTS = 4.54;
    //
    public static final double TICKS_PER_REV_ODOM = 0;
    //
    public static final double COMPLETE_TURN_ROTS_ODOM = 0;
    //
    public static final double WHEEL_RADIUS = 0;
    //
    public static final double GEAR_RATIO = 0;
    //
    public static final double TRACK_WIDTH = 0;

    /*
     ** These are NOT true constants
     */
    //
    public static double MAX_VEL = 0;
    //
    public static double MAX_ACCEL = 0;
    //
    public static double MAX_ANG_VEL = 0;
    //
    public static double MAX_ANG_ACCEL = 0;

    //
    public static TrajectoryVelocityConstraint VELOCITY_CONSTRAINT;
    public static TrajectoryAccelerationConstraint ACCELERATION_CONSTRAINT;
    }
}
