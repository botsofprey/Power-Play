package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class ninetyDegreeTurns extends OpMode {
    double limitPowerChange = 0.05;
    double limitPowerChangeFast = 0.01;
    /**
     * The variable used to represent the amount forward, implemented as gamepad1.left_stick_y
     */
    double y;
    /**
     * The variable used to represent the amount right, implemented as gamepad1.left_stick_x
     */
    double x;
    /**
     * The variable used to represent turning, implemented as gamepad1.right_stick_x
     */
    double rx;
    double lastX;
    double lastY;
    double lastRx;
    double turningRx;

    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            if (!(board.getHeading(AngleUnit.DEGREES) == 0)) {
                if (board.getHeading(AngleUnit.DEGREES) >= 180) {
                    turningRx = 0.5;
                } else if (board.getHeading(AngleUnit.DEGREES) <= 180) {
                    turningRx = -0.5;
                }
            } else {
                turningRx = 0;
            }
            telemetry.addData("turning to", 0);
        } else if (gamepad1.dpad_right) {
            if (!(board.getHeading(AngleUnit.DEGREES) == 90)) {
                if (board.getHeading(AngleUnit.DEGREES) > 90) {
                    turningRx = -0.5;
                } else if (board.getHeading(AngleUnit.DEGREES) < 90) {
                    turningRx = 0.5;
                }
            } else {
                turningRx = 0;
            }
            telemetry.addData("turning to", 90);
        } else if (gamepad1.dpad_down) {
            if (!(board.getHeading(AngleUnit.DEGREES) == 180)) {
                if (board.getHeading(AngleUnit.DEGREES) > 180) {
                    turningRx = -0.5;
                } else if (board.getHeading(AngleUnit.DEGREES) < 180) {
                    turningRx = 0.5;
                }
                telemetry.addData("turning to", 180);
            } else {
                turningRx = 0;
            }
        } else if (gamepad1.dpad_left) {
            if (!(board.getHeading(AngleUnit.DEGREES) == 270)) {
                if (board.getHeading(AngleUnit.DEGREES) > 270) {
                    turningRx = -0.5;
                } else if (board.getHeading(AngleUnit.DEGREES) < 270) turningRx = 0.5;
            } else {
                turningRx = 0;
            }
            telemetry.addData("turning to", 270);
        }
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;

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
        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_right || gamepad1.dpad_left) {
            if (gamepad1.right_bumper) {
                board.driveFieldRelative(y / 4, x / 4, turningRx / 4); //slow mode
                telemetry.addData("Mode", "Slow");
            } else if (gamepad1.left_bumper) {
                board.driveFieldRelative(y, x, turningRx); //speed mode
                telemetry.addData("Mode", "Speed");
            } else {
                board.driveFieldRelative(y / 2, x / 2, turningRx / 2); //normal mode
                telemetry.addData("Mode", "Normal");
            }
        } else {
            if (gamepad1.right_bumper) {
                board.driveFieldRelative(y / 4, x / 4, rx / 4); //slow mode
                telemetry.addData("Mode", "Slow");
            } else if (gamepad1.left_bumper) {
                board.driveFieldRelative(y, x, rx); //speed mode
                telemetry.addData("Mode", "Speed");
            } else {
                board.driveFieldRelative(y / 2, x / 2, rx / 2); //normal mode
                telemetry.addData("Mode", "Normal");
            }
        }
    }
}
