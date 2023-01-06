package org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses;

public class robotMovement {
    public driveMode dm;

    public robotMovement(driveMode dm) {
        this.dm = dm;
    }

    public double[] forback(double[] motorVals, double forback) {
        motorVals[0] += (forback * dm.driveMode);
        motorVals[1] += (forback * dm.driveMode);
        motorVals[2] += (forback * dm.driveMode);
        motorVals[3] += (forback * dm.driveMode);
        return new double[] = motorVals;
    }
}
