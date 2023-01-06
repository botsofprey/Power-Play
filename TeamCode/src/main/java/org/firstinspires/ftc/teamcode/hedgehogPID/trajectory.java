package org.firstinspires.ftc.teamcode.hedgehogPID;

import org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses.driveMode;

import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_VEL;

import java.util.List;

public class trajectory {
    public List<segment> segmentList;

    private driveMode dm;

    public trajectory(driveMode dm) {
        this.dm = dm;
    }

    public void setSegment(double forbackBLOCK, double strafeBLOCK, double turnDEG, Marker marker) {
        segment seg = new segment();

        double frontLeftMotor = 0.0,
               frontRightMotor = 0.0,
               backLeftMotor = 0.0,
               backRightMotor = 0.0;

        double[] motors = new double[]{frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};

        //(MAX_VEL / dm.driveMode) is the proportion of the maximum velocity in ticks to
        //the driveMode (fast, slow, or normal)
        double duration = Math.hypot(strafeBLOCK, forbackBLOCK) / (MAX_VEL / dm.driveMode);

        //convert degrees into parts of asegment whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        //turn robot a certain amount of degrees
        motors[0] = motors[2] -= (turnDEG * dm.driveMode);
        motors[1] = motors[3] += (turnDEG * dm.driveMode);

        //Move forward or backward a certain amount of blocks
        for (int i = 0; i < 3; i++)
            motors[i] += (forbackBLOCK * dm.driveMode);

        //Strafe a certain amount of blocks
        motors[1] = motors[2] += (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);
        motors[0] = motors[3] -= (strafeBLOCK * dm.driveMode * BLOCK_LENGTH);

        //return calculation results from pipeline for use in code
        addToSegmentList(seg);
    }

    public void executeSegmentList() {

    }

    private void addToSegmentList(segment seg) {

    }
}
