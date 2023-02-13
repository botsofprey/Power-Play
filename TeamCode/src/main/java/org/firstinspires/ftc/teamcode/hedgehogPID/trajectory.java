package org.firstinspires.ftc.teamcode.hedgehogPID;

import org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses.driveMode;

import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.BLOCK_LENGTH;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.COMPLETE_TURN_ROTS;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.MAX_VELOCITY;
import static org.firstinspires.ftc.teamcode.hedgehogPID.driveConstants.TICKS_PER_REV;

import androidx.annotation.Nullable;

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
        double[] motors = initMotors();

        double targetVelocity = Math.min((targetVelocityBLOCK * TICKS_PER_REV), MAX_VELOCITY);

        double duration = Math.hypot(strafeBLOCK * TICKS_PER_REV, forbackBLOCK * TICKS_PER_REV)
                          / (targetVelocity / dm.driveMode);

        //convert degrees into parts of a whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        addTurn(turnDEG, motors);
        addStrafe(strafeBLOCK, motors);
        addForback(forbackBLOCK, motors);

        //return calculation results
        segment seg = new segment(motors, duration, targetVelocity);
        segmentList.add(seg);
        return this;
    }

    public double[] addTurn(double degrees, double motors[]) {
        motors[0] = motors[2] -= (degrees * dm.driveMode);
        motors[1] = motors[3] += (degrees * dm.driveMode);
        return motors;
    }

    public double[] addStrafe(double strafe, double motors[]) {
        motors[1] = motors[2] += (strafe * dm.driveMode * BLOCK_LENGTH);
        motors[0] = motors[3] -= (strafe * dm.driveMode * BLOCK_LENGTH);
        return motors;
    }

    public double[] addForback(double forback, double motors[]) {
        for (int i = 0; i < 3; i++)
            motors[i] += (forback * dm.driveMode);
        return motors;
    }

    public

    public double[] initMotors() {
        double frontLeft  = 0.0,
               frontRight = 0.0,
               backLeft   = 0.0,
               backRight  = 0.0;
        return new double[]{frontLeft, frontRight,
                            backLeft,  backRight };
    }

    public void executeSegmentList() {
        for (int i = 0; i <= segmentList.size(); ++i) {
            segment currentSeg = segmentList.get(i);
            pid.checkPID(currentSeg);
        }
    }
}
