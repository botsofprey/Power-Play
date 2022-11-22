package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class DriveTrain extends OpMode {
    boolean slowModeAlreadyOn = false;
    boolean speedModeAlreadyOn = false;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;

    @Override
    public void init() {
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        if (gamepad1.right_bumper) {
            motorFrontLeft.setPower(frontLeftPower / 4);
            motorBackLeft.setPower(backLeftPower / 4);
            motorFrontRight.setPower(frontRightPower / 4);
            motorBackRight.setPower(backRightPower / 4);
            telemetry.addData("Mode", "Slow");
            slowModeAlreadyOn = true;
            if (speedModeAlreadyOn) {
                speedModeAlreadyOn = false;
            }
        } else if (gamepad1.left_bumper) {
            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            telemetry.addData("Mode", "Speed");
            speedModeAlreadyOn = true;
            if (slowModeAlreadyOn) {
                slowModeAlreadyOn = false;
            }
        } else {
            motorFrontLeft.setPower(frontLeftPower / 2);
            motorBackLeft.setPower(backLeftPower / 2);
            motorFrontRight.setPower(frontRightPower / 2);
            motorBackRight.setPower(backRightPower / 2);
            telemetry.addData("Mode", "Normal");
            if (slowModeAlreadyOn) {
                slowModeAlreadyOn = false;
            }
            if (speedModeAlreadyOn) {
                speedModeAlreadyOn = false;
            }
        }
    }
}