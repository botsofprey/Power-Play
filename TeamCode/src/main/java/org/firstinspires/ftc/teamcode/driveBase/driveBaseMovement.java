package org.firstinspires.ftc.teamcode.driveBase;

import org.firstinspires.ftc.teamcode.misc.logger;

public class driveBaseMovement {
    public int driveMode;
    public int normalMode;
    public int fastMode;

    //set fastMode to double drive speed;
    public void setFastMode() {
        fastMode = normalMode * 2;
    }

    //set drive speed mode
    public void setDriveMode(String mode) {
        if (mode == "normal")
            driveMode = normalMode;
        else if (mode == "fast")
            driveMode = fastMode;
    }

    //move robot completely
    public double[] moveRobot(double degrees, double forback, double strafe) {
        double frontLeftMotor = 0.0;
        double frontRightMotor = 0.0;
        double backLeftMotor = 0.0;
        double backRightMotor = 0.0;

        //POW in POWturnRobot means 'part of whole'
        double POWturnRobot = degrees/360;

        //turn robot
            frontLeftMotor -= (POWturnRobot * driveMode);
            frontRightMotor += (POWturnRobot * driveMode);
            backLeftMotor -= (POWturnRobot * driveMode);
            backRightMotor += (POWturnRobot * driveMode);

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