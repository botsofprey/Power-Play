package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This class is for our four drivetrain motors and provides the basis to drive the robot
 */
public class MecanumDrive {

    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;

    public void init(HardwareMap hwMap) {
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * The method used to set the motors' powers, it is used in the drive method and acts as the basis of all of our driving
     *
     * @param frontLeftPower  The power the front left motor is set to
     * @param frontRightPower The power the front right motor is set to
     * @param backLeftPower   The power the back left motor is set to
     * @param backRightPower  The power the back right motor is set to
     */
    private void setPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        double maxSpeed = 1.0;
        maxSpeed = Math.max(maxSpeed, Math.abs(frontLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(frontRightPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backRightPower));

        frontLeftPower /= maxSpeed;
        frontRightPower /= maxSpeed;
        backLeftPower /= maxSpeed;
        backRightPower /= maxSpeed;

        motorFrontLeft.setPower(frontLeftPower);
        motorFrontRight.setPower(frontRightPower);
        motorBackLeft.setPower(backLeftPower);
        motorBackRight.setPower(backRightPower);
    }

    /**
     * A method to calculate the motors' powers and implement them using the setPowers method
     *
     * @param forward Controls forwards and backwards movement
     * @param right   Controls strafing, which is the robot moving left or right without turning
     * @param rotate  Controls turning
     */
    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        setPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }
}
