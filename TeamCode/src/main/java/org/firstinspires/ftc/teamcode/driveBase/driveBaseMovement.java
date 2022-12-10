package org.firstinspires.ftc.teamcode.driveBase;

import org.firstinspires.ftc.teamcode.misc.logger;

public class driveBaseMovement {
    public int driveMode;
    public int normalMode = 1;
    public int fastMode;
    public int slowMode;

    //set fastMode to double normalMode;
    public void setFastMode() {
        fastMode = normalMode * 2;
    }

    //set slowMode to half normalMode
    public void setSlowMode() {
        slowMode = normalMode / 2;
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

    //move robot completely
    public double[] moveRobot(double turn, double forback, double strafe) {
        double frontLeftMotor = 0.0;
        double frontRightMotor = 0.0;
        double backLeftMotor = 0.0;
        double backRightMotor = 0.0;

        //turn robot
            frontLeftMotor -= (turn * driveMode);
            frontRightMotor += (turn * driveMode);
            backLeftMotor -= (turn * driveMode);
            backRightMotor += (turn * driveMode);

        //Move forward or backward
            frontLeftMotor += (forback * driveMode);
            frontRightMotor += (forback * driveMode);
            backLeftMotor += (forback * driveMode);
            backRightMotor += (forback * driveMode);

        //Strafe
            frontLeftMotor -= (strafe * driveMode);
            frontRightMotor += (strafe * driveMode);
            backLeftMotor += (strafe * driveMode);
            backRightMotor -= (strafe * driveMode);

        //return calculation results from pipeline for use in code
        return new double[]{frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};
    }
}