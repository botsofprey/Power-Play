package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This is the combination of all of our TeleOp codes into one
 */

@TeleOp()
public class EncodedEverything extends OpMode {

    HardwareMechanisms board = new HardwareMechanisms();

    double clawPosition;
    double limitPowerChange = 0.05;
    double limitPowerChangeFast = 0.01;
    double lastX;
    double lastRx;
    double lastY;

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;
        double changeX = x - lastX;
        if (gamepad1.left_bumper) {
            if (Math.abs(changeX) > limitPowerChangeFast) {
                changeX = Math.signum(changeX) * limitPowerChangeFast;
            }
            x = lastX + changeX;
            lastX = x;

            double changeRX = rx - lastRx;
            if (Math.abs(changeRX) > limitPowerChangeFast) {
                changeRX = Math.signum(changeRX) * limitPowerChangeFast;
            }
            rx = lastRx + changeRX;
            lastRx = rx;

            double changeY = y - lastY;
            if (Math.abs(changeY) > limitPowerChangeFast) {
                changeY = Math.signum(changeY) * limitPowerChangeFast;
            }
            y = lastY + changeY;
            lastY = y;
        } else {
            if (Math.abs(changeX) > limitPowerChange) {
                changeX = Math.signum(changeX) * limitPowerChange;
            }
            x = lastX + changeX;
            lastX = x;

            double changeRX = rx - lastRx;
            if (Math.abs(changeRX) > limitPowerChange) {
                changeRX = Math.signum(changeRX) * limitPowerChange;
            }
            rx = lastRx + changeRX;
            lastRx = rx;

            double changeY = y - lastY;
            if (Math.abs(changeY) > limitPowerChange) {
                changeY = Math.signum(changeY) * limitPowerChange;
            }
            y = lastY + changeY;
            lastY = y;
        }
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        double maxSpeed = 1.0;
         maxSpeed = Math.max(maxSpeed, Math.abs(frontLeftPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(frontRightPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(backLeftPower));
         maxSpeed = Math.max(maxSpeed, Math.abs(backRightPower));

         frontLeftPower /= maxSpeed;
         frontRightPower /= maxSpeed;
         backLeftPower /= maxSpeed;
         backRightPower /= maxSpeed;

//        if (board.getLift() <= 1650) {
//            board.setLift((gamepad2.right_trigger - gamepad2.left_trigger) / 2);
//        } else if (board.getLift() >= 0) {
//            board.setLift(gamepad2.right_trigger / 2);
//        } else {
//            board.setLift(-gamepad2.left_trigger / 2);
//        }
//
//        if (gamepad2.a) {
//            clawPosition += 0.001;
//        } else if (gamepad2.b) {
//            clawPosition -= 0.001;
//        }
//        board.setClaw(clawPosition);

        if (gamepad1.right_bumper) {
            board.drive.motorFrontLeft.setPower(frontLeftPower / 4);
            board.drive.motorBackLeft.setPower(backLeftPower / 4);
            board.drive.motorFrontRight.setPower(frontRightPower / 4);
            board.drive.motorBackRight.setPower(backRightPower / 4);
            telemetry.addData("Mode", "Slow");
        } else if (gamepad1.left_bumper) {
            board.drive.motorFrontLeft.setPower(frontLeftPower);
            board.drive.motorBackLeft.setPower(backLeftPower);
            board.drive.motorFrontRight.setPower(frontRightPower);
            board.drive.motorBackRight.setPower(backRightPower);
            telemetry.addData("Mode", "Speed");
        } else {
            board.drive.motorFrontLeft.setPower(frontLeftPower / 2);
            board.drive.motorBackLeft.setPower(backLeftPower / 2);
            board.drive.motorFrontRight.setPower(frontRightPower / 2);
            board.drive.motorBackRight.setPower(backRightPower / 2);
            telemetry.addData("Mode", "Normal");
        }
    }
}