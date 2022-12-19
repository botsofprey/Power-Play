package org.firstinspires.ftc.teamcode.driveBase;

import org.firstinspires.ftc.teamcode.misc.logger;

public class driveBaseMovement {
    public int driveMode;
    public int normalMode = 1;
    public int fastMode;
    public int slowMode;

    public int TICKS_PER_REV;
    public double BLOCK_LENGTH;

    public driveBaseMovement() {
        //set the ticks per revolution
        final int TICKS_PER_REV = this.TICKS_PER_REV;
        //set the block length in centimeters (should be 60.69 or 24 inches
        final double BLOCK_LENGTH = this.BLOCK_LENGTH;
    }

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

    public double[] moveRobotAUTO(double turnDEG, double forbackBLOCK, double strafeBLOCK) {
        double frontLeftMotor = 0.0;
        double frontRightMotor = 0.0;
        double backLeftMotor = 0.0;
        double backRightMotor = 0.0;

        turnDEG =

        //turn robot a certain amount of degrees
            frontLeftMotor -= (turnDEG * BLOCK_LENGTH * TICKS_PER_REV);
            frontRightMotor += (turnDEG * BLOCK_LENGTH * TICKS_PER_REV);
            backLeftMotor -= (turnDEG * BLOCK_LENGTH * TICKS_PER_REV);
            backRightMotor += (turnDEG * driveMode * BLOCK_LENGTH * TICKS_PER_REV);

        //Move forward or backward a certain amount of blocks
            frontLeftMotor += (forbackBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            frontRightMotor += (forbackBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            backLeftMotor += (forbackBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            backRightMotor += (forbackBLOCK * BLOCK_LENGTH * TICKS_PER_REV);

        //Strafe a certain amount of blocks
            frontLeftMotor -= (strafeBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            frontRightMotor += (strafeBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            backLeftMotor += (strafeBLOCK * BLOCK_LENGTH * TICKS_PER_REV);
            backRightMotor -= (strafeBLOCK * BLOCK_LENGTH * TICKS_PER_REV);

        //return calculation results from pipeline for use in code
        return new double[]{frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};
    }
}