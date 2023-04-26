package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class DriveControls {
    private static final String[] MOTOR_NAMES = {
            "frontLeftDriveMotor",
            "backLeftDriveMotor",
            "frontRightDriveMotor",
            "backRightDriveMotor"
    };
    private final DcMotor[] motors = new DcMotor[4];
    private final DcMotorSimple.Direction[] directions = {
            DcMotorSimple.Direction.REVERSE,
            DcMotorSimple.Direction.REVERSE,
            DcMotorSimple.Direction.FORWARD,
            DcMotorSimple.Direction.FORWARD
    };
    IMU imu;

    public void getHardware(HardwareMap hw) {
        imu = hw.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;
        myIMUparameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );
        imu.initialize(myIMUparameters);
        for (int i = 0; i < motors.length; i++) {
            motors[i] = hw.get(DcMotor.class, MOTOR_NAMES[i]);
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[i].setDirection(directions[i]);
        }
    }

    public double getAngle(AngleUnit unit) {
        YawPitchRollAngles robotOrientation;
        robotOrientation = imu.getRobotYawPitchRollAngles();
        return robotOrientation.getYaw(unit);
    }

    public void drive(double y, double x, double rx) {
        double rotX = x * Math.cos(-getAngle(AngleUnit.RADIANS)) - y * Math.sin(-getAngle(AngleUnit.RADIANS));
        double rotY = x * Math.sin(-getAngle(AngleUnit.RADIANS)) + y * Math.cos(-getAngle(AngleUnit.RADIANS));
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        motors[1].setPower((rotY + rotX + rx) / denominator);
        motors[2].setPower((rotY - rotX + rx) / denominator);
        motors[3].setPower((rotY - rotX - rx) / denominator);
        motors[4].setPower((rotY + rotX - rx) / denominator);
    }
}
