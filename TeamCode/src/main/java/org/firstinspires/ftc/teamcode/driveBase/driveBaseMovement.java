package org.firstinspires.ftc.teamcode.driveBase;

import org.firstinspires.ftc.teamcode.misc.logger;

public class driveBaseMovement {
    public int driveMode;
    public int driveSpeed;
    public int fastMode;

    //set fastMode to double drive speed;
    public void setFastMode() {
        fastMode = driveSpeed * 2;
    }

    //set drive speed mode
    public void setDriveMode(String mode) {
        if (mode == "normal")
            driveMode = driveSpeed;
        else if (mode == "fast")
            driveMode = fastMode;
    }

    //move robot completely
    public double[] moveRobot(double degrees, double forward,
                                double backward, double strafeRight, double strafeLeft) {
        double frontLeftMotor = 0.0;
        double frontRightMotor = 0.0;
        double backLeftMotor = 0.0;
        double backRightMotor = 0.0;
        double forback, strafe;
        double[] error = new double[0];

        double[] finalCalc = {frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};

        //error checking
        if (forward < 0 || backward < 0 || strafeRight < 0 || strafeLeft < 0) {
            logger.logError("moveRobot1");
            return error;
        }
        if (((forward > 0) && (backward > 0)) || ((strafeRight > 0) && (strafeLeft > 0))) {
            logger.logError("moveRobot2");
            return error;
        }

        forback = (forward == 0) ? backward : forward;
        strafe = (strafeRight == 0) ? strafeLeft : strafeRight;

        //POW in POWturnRobot means 'part of whole'
        double POWturnRobot = degrees < 0 ? -degrees/360 : degrees/360;

        //Counterclockwise turning
        if (degrees < 0) {
            frontRightMotor += (POWturnRobot * driveMode);
            backLeftMotor -= (POWturnRobot * driveMode);
        }
        //Clockwise turning
        else {
            frontLeftMotor -= (POWturnRobot * driveMode);
            backRightMotor += (POWturnRobot * driveMode);
        }

        //Move backward
        if (forback == backward) {
            backLeftMotor -= (forback * driveMode);
            backRightMotor -= (forback * driveMode);
            frontLeftMotor -= (forback * driveMode);
            frontRightMotor -= (forback * driveMode);
        }
        //Move forward
        else {
            frontLeftMotor += (forback * driveMode);
            frontRightMotor += (forback * driveMode);
            backLeftMotor += (forback * driveMode);
            backRightMotor += (forback * driveMode);
        }

        if (strafe < 0) {

        }
        else {

        }

        return finalCalc;
    }
}