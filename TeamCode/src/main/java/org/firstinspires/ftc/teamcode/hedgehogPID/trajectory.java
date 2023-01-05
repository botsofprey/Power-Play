package org.firstinspires.ftc.teamcode.hedgehogPID;

import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS_ODOM;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV_ODOM;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ANG_VEL;

public class trajectory {

    public segment setSegment(double forbackBLOCK, double strafeBLOCK, double turnDEG) {
        double frontLeftMotor = 0.0,
               frontRightMotor = 0.0,
               backLeftMotor = 0.0,
               backRightMotor = 0.0;

        double duration =

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
        return new segment();
    }
}
