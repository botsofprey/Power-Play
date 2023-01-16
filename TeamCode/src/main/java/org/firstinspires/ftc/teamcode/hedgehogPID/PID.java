package org.firstinspires.ftc.teamcode.hedgehogPID;

public class PID {
    private double[] odom;
    private double[] motors;
    private double   targetVelocity;

    private double checkTurn() {
        double error = 0;
        return error;
    }

    private double checkTranslation() {
        double error = 0;
        return error;
    }

    private double checkVelocity() {
        double error = 0;
        return error;
    }

    public double[] checkPID() {
        double turnError = checkTurn();
        double translationError = checkTranslation();
        double velocityError = checkVelocity();
        return new double[]{turnError, translationError, velocityError};
    }
}
