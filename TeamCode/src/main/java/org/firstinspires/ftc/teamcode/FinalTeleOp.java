package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * This is the combination of all of our TeleOp codes into one
 */
@TeleOp()
public class FinalTeleOp extends OpMode {
    /**
     * A double used to make sure the acceleration isn't too high
     */
    double limitPowerChange = 0.05;
    /**
     * A double used to make sure the acceleration isn't too high, specifically for the speed mode
     */
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
    /**
     * A double used to track the previous x value in order to limit acceleration
     */
    double lastX;
    /**
     * A double used to track the previous rx value in order to limit acceleration
     */
    double lastRx;
    /**
     * A double used to track the previous y value in order to limit acceleration
     */
    double lastY;
    /**
     * An int used for tracking the target position of the lift during the preset heights mode, which changes between 0, groundJunction, lowJunction, midJunction, and highJunction
     */
    int targetPositionPresetHeights = 0;
    /**
     * An int used to control the lift while under manual control
     */
    int targetPositionManualControl;
    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean upPressed = false;
    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean downPressed = false;
    /**
     * A boolean used to track whether the lift is manually controlled or uses preset heights
     */
    boolean presetLiftHeightsMode = false;

    HardwareMechanisms board = new HardwareMechanisms();
    HeightsList heights = new HeightsList();

    public void init() {
        board.init(hardwareMap);
        board.setLift(0);
    }

    @Override
    public void loop() {
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;

        //from about line 79 to 120 is code to limit acceleration
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

        if (gamepad2.right_trigger > 0 || gamepad2.left_trigger > 0) {
            presetLiftHeightsMode = false;
        } else if (gamepad2.dpad_up || gamepad2.dpad_down || gamepad2.dpad_right || gamepad2.dpad_left || gamepad2.right_bumper) {
            presetLiftHeightsMode = true;
        }

        if (!presetLiftHeightsMode) { //manual lift
            //the right trigger makes the lift go up and the left trigger makes the lift go down
            if (board.getLift() <= 0) {
                targetPositionManualControl += (int) gamepad2.right_trigger * 15;
            } else if (board.getLift() <= heights.highJunction) {
                targetPositionManualControl += (int) ((gamepad2.right_trigger - gamepad2.left_trigger) * 15);
            } else {
                targetPositionManualControl -= (int) gamepad2.left_trigger * 15;
            }
            if (targetPositionManualControl < 0) { //keeps the lift from going below 0
                targetPositionManualControl = 0;
            }
            board.setLift(targetPositionManualControl);

            if (Math.abs(board.getLift() - heights.highJunction) < Math.abs(board.getLift() - heights.midJunction)) {
                targetPositionPresetHeights = heights.highJunction;
            } else if (Math.abs(board.getLift() - heights.midJunction) < Math.abs(board.getLift() - heights.lowJunction)) {
                targetPositionPresetHeights = heights.midJunction;
            } else if (Math.abs(board.getLift()) < Math.abs(board.getLift())) {
                targetPositionPresetHeights = heights.lowJunction;
            } else {
                targetPositionPresetHeights = 0;
            }
            telemetry.addData("lift mode", "manual control");
            telemetry.addData("target position", "n/a");
        } else {
            if (gamepad2.dpad_up) { //lift uses preset heights
                if (targetPositionPresetHeights == 0 || targetPositionPresetHeights == heights.groundJunction) {
                    targetPositionPresetHeights = heights.lowJunction;
                } else if ((targetPositionPresetHeights == heights.lowJunction) && !upPressed) {
                    targetPositionPresetHeights = heights.midJunction;
                } else if ((targetPositionPresetHeights == heights.midJunction) && !upPressed) {
                    targetPositionPresetHeights = heights.highJunction;
                }
            } else if (gamepad2.dpad_down) {
                if (targetPositionPresetHeights == heights.highJunction) {
                    targetPositionPresetHeights = heights.midJunction;
                } else if ((targetPositionPresetHeights == heights.midJunction) && !downPressed) {
                    targetPositionPresetHeights = heights.lowJunction;
                } else if ((targetPositionPresetHeights == heights.lowJunction || targetPositionPresetHeights == heights.groundJunction) && !downPressed) {
                    targetPositionPresetHeights = 0;
                }
            } else if (gamepad2.right_bumper) {
                targetPositionPresetHeights = heights.groundJunction;
            }
            board.setLift(targetPositionPresetHeights);
            telemetry.addData("lift mode", "preset heights");
            telemetry.addData("target position", targetPositionPresetHeights);
            upPressed = gamepad2.dpad_up;
            downPressed = gamepad2.dpad_down;
            targetPositionManualControl = (int) board.getLift();
        }
        telemetry.addData("position", board.getLift());

        if (gamepad2.b) {
            board.setClaw(0.4);
            telemetry.addData("claw state", "grab");
        } else {
            board.setClaw(0);
            telemetry.addData("claw state", "release");
        }

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
    public void stop(){
        StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS);
    }
}