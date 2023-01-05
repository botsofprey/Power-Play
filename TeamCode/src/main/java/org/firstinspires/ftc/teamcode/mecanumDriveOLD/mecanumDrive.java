package org.firstinspires.ftc.teamcode.mecanumDriveOLD;

import static org.firstinspires.ftc.teamcode.mecanumDriveOLD.driveVariables.TICKS_PER_REV;
import static org.firstinspires.ftc.teamcode.mecanumDriveOLD.driveVariables.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.mecanumDriveOLD.driveVariables.COMPLETE_TURN_ROTS;

import org.firstinspires.ftc.teamcode.mecanumDriveOLD.pathSegment.legitPathSegment;
import org.firstinspires.ftc.teamcode.mecanumDriveOLD.pathSegment.pathSegment;

/*
* This is the driveBaseMovement class, which is responsible for auto and teleop
* robot movement calculations, and calculations only. This class is restricted to
* only calculations so that all active control flow is handled by the opmodes,
* so as to avoid issues with control flow and memory allocation (This class's
* functions must be initialized in an external object with its own address space,
* because we want to avoid side effects of previous operations being applied to other
* opmodes or any other instances of usage).
*
* **THIS CLASS HANDLES:**
* Odometry functions
* all auto movement functions excluding claw movement
* all teleop movement functions excluding claw movement
*
* **ANOTHER THING TO NOTE**
* The constants (or final variables, because Java is stupid) are specifically set
* satisfy a VERY SPECIFIC robot design. It is highly recommended to measure your own robot
* to determine these variables. The BLOCK_LENGTH and TICKS_PER_REV should be specifically
* noted to only work for the GoBilda Yellow Jacket 5202/3/4 series motors, however, the
* BLOCK_LENGTH is based on a conversion from (inches > centimeters > ticks) based on the
* notion that an FTC foam tile is 24 x 24 inches, or 60.69 x 60.69 centimeters. Simply
* convert centimeters to ticks and you are good to go!
*
* **FURTHER READING**
* All robot movements that are used in this class are based on GM0's mecanum drive
* documentation at
* https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
*/
public class mecanumDrive {
    //initialize temporary motor values
    private double
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor;
    //place motor values in an array
    private double[] motors = {
            frontLeftMotor,
            frontRightMotor,
            backLeftMotor,
            backRightMotor
    };
    //the main variable that drive speed is stored in
    public float driveMode;
    //speed variables that are set to driveMode
    public int normalMode = 1;
    public int fastMode;
    public float slowMode;

    public static double LATERAL_MULTIPLIER = 1;

    //like moveRobot but for auto only
    public legitPathSegment legitimizePathSeg(pathSegment pathseg) {
        //set path segment values equal to local equivalents
        double turnDEG = pathseg.turnDEG;
        double strafeBLOCK = pathseg.strafe;
        double forbackBLOCK = pathseg.forback;

        //set all motor values to 0
        for (double motor: motors) {
            motor = 0.0;
        }
        //convert degrees into parts of a whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        //turn robot a certain amount of degrees
            frontLeftMotor -= (turnDEG * driveMode);
            frontRightMotor += (turnDEG * driveMode);
            backLeftMotor -= (turnDEG * driveMode);
            backRightMotor += (turnDEG * driveMode);

        //Move forward or backward a certain amount of blocks
            for (double motor: motors) {
                 motor += (forbackBLOCK * driveMode * BLOCK_LENGTH);
            }

        //Strafe a certain amount of blocks
            frontLeftMotor -= (strafeBLOCK * driveMode * BLOCK_LENGTH);
            frontRightMotor += (strafeBLOCK * driveMode * BLOCK_LENGTH);
            backLeftMotor += (strafeBLOCK * driveMode * BLOCK_LENGTH);
            backRightMotor -= (strafeBLOCK * driveMode * BLOCK_LENGTH);

        //return calculation results from pipeline for use in code
        return new legitPathSegment(pathseg.targetDuration, pathseg.targetEndPosition,
                                    frontLeftMotor, frontRightMotor, backLeftMotor,
                                    backRightMotor, pathseg.markers);
    }

    /*public TrajectoryBuilder trajectoryBuilder(List<legitPathSegment> path) {
        for (legitPathSegment pathsegment : path) {
            ;
        }
        return trajectory = new TrajectoryBuilder();
    }*/

    public void setSlowMode() {
        slowMode = normalMode / 2;
    }

    public void setFastMode() {
        fastMode = normalMode * 2;
    }

    //set drive speed mode
    public void setDriveMode(String mode) {
        if (mode == "normal")
            driveMode = normalMode;
        else if (mode == "fast")
            driveMode = fastMode;
        else if (mode == "slow")
            driveMode = slowMode;
    }
}