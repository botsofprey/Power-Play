package org.firstinspires.ftc.teamcode.driveBase;

import org.firstinspires.ftc.teamcode.misc.logger;

public class driveBaseMovement {
    public int driveMode;
    public int normalMode = 1;
    public int fastMode;
    public int slowMode;

    //amount of ticks in a single full revolution of the wheel
    public final int TICKS_PER_REV = 0;
    //the length of the 24x24 inch foam tiles on the field in ticks
    public final double BLOCK_LENGTH = 0;
    //the amount of wheel turns required to fully turn the robot
    public final double COMPLETE_TURN_ROTS = 0;

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
        double frontLeftMotorTICKS = 0.0;
        double frontRightMotorTICKS = 0.0;
        double backLeftMotorTICKS = 0.0;
        double backRightMotorTICKS = 0.0;

        //convert degrees into parts of a whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        //turn robot a certain amount of degrees
            frontLeftMotorTICKS -= (turnDEG * driveMode);
            frontRightMotorTICKS += (turnDEG * driveMode);
            backLeftMotorTICKS -= (turnDEG * driveMode);
            backRightMotorTICKS += (turnDEG * driveMode);

        //Move forward or backward a certain amount of blocks
            frontLeftMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            frontRightMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            backLeftMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            backRightMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);

        //Strafe a certain amount of blocks
            frontLeftMotorTICKS -= (strafeBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            frontRightMotorTICKS += (strafeBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            backLeftMotorTICKS += (strafeBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);
            backRightMotorTICKS -= (strafeBLOCK * driveMode * BLOCK_LENGTH * TICKS_PER_REV);

        //return calculation results from pipeline for use in code
        return new double[]{frontLeftMotorTICKS, frontRightMotorTICKS, backLeftMotorTICKS, backRightMotorTICKS};
    }
}