package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 *This is the combination of all of our TeleOp codes into one
 */
@TeleOp()
public class EncodedEverything extends OpMode {
    /**
     *A double used to make sure the acceleration isn't too high
     */
    double limitPowerChange = 0.05;
    /**
     *A double used to make sure the acceleration isn't too high, specifically for the speed mode
     */
    double limitPowerChangeFast = 0.01;
    /**
     *A double used to track the previous x value in order to limit acceleration
     */
    double lastX;
    /**
     *A double used to track the previous rx value in order to limit acceleration
     */
    double lastRx;
    /**
     *A double used to track the previous y value in order to limit acceleration
     */
    double lastY;
    /**
     *An int used for tracking the target position of the lift, which changes between 0, lowJunction, midJunction, and highJunction
     */
    int targetPosition = 0;
    /**
     *An int used to represent the tic value of the lift at the height of the low junction, it is subject to change based off of the lift
     */
    int lowJunction = 2500; //dummy numbers to be replaced
    /**
     *An int used to represent the tic value of the lift at the height of the medium junction, it is subject to change based off of the lift
     */
    int midJunction = 5000;
    /**
     *An int used to represent the tic value of the lift at the height of the high junction, it is subject to change based off of the lift
     */
    int highJunction = 10100;
    /**
     *A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean upPressed = false;
    /**
     *A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean downPressed = false;
    /**
     *A boolean used to track whether the lift is manually controlled or uses preset heights
     */
    boolean presetLift = false;

    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
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

        if (gamepad2.right_trigger > 0 || gamepad2.left_trigger > 0) {
            presetLift = false;
        } else if (gamepad2.dpad_up || gamepad2.dpad_down) {
            presetLift = true;
        }

        if (!presetLift) {
            //right trigger goes up, left trigger goes down
            if (board.getLift() <= 0) {
                board.setLift(gamepad2.right_trigger / 1.5);
            } else if (board.getLift() <= 10100) {
                board.setLift((gamepad2.right_trigger - gamepad2.left_trigger) / 1.5);
            } else {
                board.setLift(-gamepad2.left_trigger / 1.5);
            }
            if (board.getLift() < 0) {
                board.lift.setTargetPosition(0);
                board.lift.setPower(0.05);
            }
            telemetry.addData("lift mode", "manual");
            telemetry.addData("target position", "null");
        } else {
            if (gamepad2.dpad_up) {
                if (targetPosition == 0) {
                    targetPosition = lowJunction;
                } else if ((targetPosition == lowJunction) && !upPressed) {
                    targetPosition = midJunction;
                } else if ((targetPosition == midJunction) && !upPressed) {
                    targetPosition = highJunction;
                }
            } else if (gamepad2.dpad_down) {
                if (targetPosition == highJunction) {
                    targetPosition = midJunction;
                } else if ((targetPosition == midJunction) && !downPressed) {
                    targetPosition = lowJunction;
                } else if ((targetPosition == lowJunction) && !downPressed) {
                    targetPosition = 0;
                }
            }
            board.lift.setTargetPosition(targetPosition);
            board.lift.setPower(0.05);
            telemetry.addData("lift mode", "preset heights");
            telemetry.addData("target position", targetPosition);
            upPressed = gamepad2.dpad_up;
            downPressed = gamepad2.dpad_down;
        }
        telemetry.addData("position", board.getLift());

        if (gamepad2.a) {
            board.setClaw(0);
        } else if (gamepad2.b) {
            board.setClaw(0.43);
        }

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