package org.firstinspires.ftc.teamcode.hedgehogPID;

import org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses.driveMode;

import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_VELOCITY;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV;

import java.util.List;

public class trajectory {
    public List<segment> segmentList;

    private driveMode dm;
    private PID pid;

    public trajectory(driveMode dm) {
        this.dm = dm;
    }

    public trajectory setSegment(double forbackBLOCK, double strafeBLOCK, double turnDEG,
                           double targetVelocityBLOCK, marker marker) {
        double frontLeftMotor  = 0.0,
               frontRightMotor = 0.0,
               backLeftMotor   = 0.0,
               backRightMotor  = 0.0;

        double targetVelocity = Math.min((targetVelocityBLOCK * TICKS_PER_REV), MAX_VELOCITY);

        double[] motors = new double[]{frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};

        double duration = Math.hypot(strafeBLOCK * TICKS_PER_REV, forbackBLOCK * TICKS_PER_REV)
                          / (targetVelocity / dm.driveMode);

        //convert degrees into parts of a whole turn
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

        //return calculation results and
        segment seg = new segment(motors, duration, targetVelocity);
        segmentList.add(seg);
        return this;
    }

    public void executeSegmentList() {
        pid.checkPID();
    }
}
