package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class ninetyDegreeTurns extends OpMode {
    double limitPowerChange = 0.05;
    double limitPowerChangeFast = 0.01;
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
        board.y = -gamepad1.left_stick_y;
        board.x = gamepad1.left_stick_x;
        board.rx = gamepad1.right_stick_x;

        double changeX = board.x - lastX;
        if (gamepad1.left_bumper) {
            if (Math.abs(changeX) > limitPowerChangeFast) {
                changeX = Math.signum(changeX) * limitPowerChangeFast;
            }
            board.x = lastX + changeX;
            lastX = board.x;

            double changeRX = board.rx - lastRx;
            if (Math.abs(changeRX) > limitPowerChangeFast) {
                changeRX = Math.signum(changeRX) * limitPowerChangeFast;
            }
            board.rx = lastRx + changeRX;
            lastRx = board.rx;

            double changeY = board.y - lastY;
            if (Math.abs(changeY) > limitPowerChangeFast) {
                changeY = Math.signum(changeY) * limitPowerChangeFast;
            }
            board.y = lastY + changeY;
            lastY = board.y;
        } else {
            if (Math.abs(changeX) > limitPowerChange) {
                changeX = Math.signum(changeX) * limitPowerChange;
            }
            board.x = lastX + changeX;
            lastX = board.x;

            double changeRX = board.rx - lastRx;
            if (Math.abs(changeRX) > limitPowerChange) {
                changeRX = Math.signum(changeRX) * limitPowerChange;
            }
            board.rx = lastRx + changeRX;
            lastRx = board.rx;

            double changeY = board.y - lastY;
            if (Math.abs(changeY) > limitPowerChange) {
                changeY = Math.signum(changeY) * limitPowerChange;
            }
            board.y = lastY + changeY;
            lastY = board.y;
        }
        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_right || gamepad1.dpad_left) {
            if (gamepad1.right_bumper) {
                board.driveFieldRelative(board.y / 4, board.x / 4, turningRx / 4); //slow mode
                telemetry.addData("Mode", "Slow");
            } else if (gamepad1.left_bumper) {
                board.driveFieldRelative(board.y, board.x, turningRx); //speed mode
                telemetry.addData("Mode", "Speed");
            } else {
                board.driveFieldRelative(board.y / 2, board.x / 2, turningRx / 2); //normal mode
                telemetry.addData("Mode", "Normal");
            }
        } else {
            if (gamepad1.right_bumper) {
                board.driveFieldRelative(board.y / 4, board.x / 4, board.rx / 4); //slow mode
                telemetry.addData("Mode", "Slow");
            } else if (gamepad1.left_bumper) {
                board.driveFieldRelative(board.y, board.x, board.rx); //speed mode
                telemetry.addData("Mode", "Speed");
            } else {
                board.driveFieldRelative(board.y / 2, board.x / 2, board.rx / 2); //normal mode
                telemetry.addData("Mode", "Normal");
            }
        }
    }
}
