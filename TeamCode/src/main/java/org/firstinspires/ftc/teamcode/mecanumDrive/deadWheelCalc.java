package org.firstinspires.ftc.teamcode.mecanumDrive;

public class deadWheelCalc {

    public double driveWheeltoDeadWheelTICKS(double ticks) {
        ticks = ticks * (driveVariables.TICKS_PER_REV_ODOM / driveVariables.TICKS_PER_REV);
        return ticks;
    }

}
