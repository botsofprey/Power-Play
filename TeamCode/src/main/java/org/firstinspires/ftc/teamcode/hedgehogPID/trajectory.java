package org.firstinspires.ftc.teamcode.hedgehogPID;

import org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses.driveMode;

import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS_ODOM;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV_ODOM;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_ANG_VEL;

import java.util.List;

public class trajectory {
    public List<segment> segmentList;

    public driveMode dm;

    public trajectory(driveMode dm) {
        this.dm = dm;
    }

    public void setSegment(double forbackBLOCK, double strafeBLOCK, double turnDEG, Marker marker) {
        segment seg = new segment();

        double frontLeftMotor = 0.0,
               frontRightMotor = 0.0,
               backLeftMotor = 0.0,
               backRightMotor = 0.0;

        //(MAX_VEL / dm.driveMode) is the proportion of the maximum velocity in ticks to
        //the driveMode (fast, slow, or normal)
        double duration = Math.hypot(strafeBLOCK, forbackBLOCK) / (MAX_VEL / dm.driveMode);

        //convert degrees into parts of asegment whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        //turn robot a certain amount of degrees
        frontLeftMotor -= (turnDEG * dm.driveMode);
        frontRightMotor += (turnDEG * dm.driveMode);
        backLeftMotor -= (turnDEG * dm.driveMode);
        backRightMotor += (turnDEG * dm.driveMode);

        //Move forward or backward a certain amount of blocks


        //Strafe a certain amount of blocks
        frontLeftMotor -= (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);
        frontRightMotor += (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);
        backLeftMotor += (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);
        backRightMotor -= (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);

        //return calculation results from pipeline for use in code
        addToSegmentList(seg);
    }

    public void executeSegmentList() {

    }

    private void addToSegmentList(segment seg) {

    }
}
