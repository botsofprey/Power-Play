package org.firstinspires.ftc.teamcode.hedgehogPID;

public class PID {
    private double[] odom;
    private double[] motors;
    private double targetVelocity;

    public double[] checkPID(segment seg, ) {
        double turnError = checkTurn(seg);
        double translationError = checkTranslation(seg);
        double velocityError = checkVelocity(seg);
        return new double[]{turnError, translationError, velocityError};
    }

    private double checkTurn(segment seg) {
        double error = 0;
        return error;
    }

    private double checkTranslation(segment seg) {
        double error = 0;
        return error;
    }

    private double checkVelocity(segment seg) {
        double error = 0;
        return error;
    }
}