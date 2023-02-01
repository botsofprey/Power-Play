package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;
import org.firstinspires.ftc.teamcode.vars.HeightsList;
import org.firstinspires.ftc.teamcode.vars.StaticImu;

/**
 * This is the combination of all of our TeleOp codes into one
 *
 * @author Alex Bryan
 */
@TeleOp()
public class FinalTeleOpRewritten extends OpMode {
    /**
     * An int used for tracking the target position of the lift during the preset heights mode, which changes between 0, groundJunction, lowJunction, midJunction, and highJunction
     */
    int targetPositionPresetHeights = 0;
    /**
     * An int used to control the lift while under manual control
     */
    int targetPositionManualControl;
    /**
     * A double used to make sure the acceleration isn't too high
     */
    double limitPowerChange = 0.1;
    /**
     * A double used to make sure the acceleration isn't too high, specifically for the speed mode
     */
    double limitPowerChangeFast = 0.05;
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
    double lastY;
    /**
     * A double used to track the previous rx value in order to limit acceleration
     */
    double lastX;
    /**
     * A double used to track the previous y value in order to limit acceleration
     */
    double lastRx;
    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean upPressed = false;
    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    boolean downPressed = false;
    /**
     * A boolean made in order to make it so that y on gamepad 2 has to be released before being counted again
     */
    boolean yPressed = false;
    /**
     * A boolean made in order to make it so that x on gamepad 2 has to be released before being counted again
     */
    boolean xPressed = false;
    /**
     * A boolean used to track whether the lift is manually controlled or uses preset heights
     */
    boolean presetLiftHeightsMode = false;
    /**
     * A boolean used to track whether the lift uses height limits
     */
    boolean liftCorrection = true;

    HardwareMechanisms board = new HardwareMechanisms();
    HeightsList heights = new HeightsList();

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;

        //from about line 80 to 114 is code to limit acceleration
        double changeX = x - lastX;
        double changeRX = rx - lastRx;
        double changeY = y - lastY;
        if (gamepad1.left_bumper) {
            if (Math.abs(changeX) > limitPowerChangeFast) {
                changeX = Math.signum(changeX) * limitPowerChangeFast;
            }

            if (Math.abs(changeRX) > limitPowerChangeFast) {
                changeRX = Math.signum(changeRX) * limitPowerChangeFast;
            }

            if (Math.abs(changeY) > limitPowerChangeFast) {
                changeY = Math.signum(changeY) * limitPowerChangeFast;
            }

        } else {
            if (Math.abs(changeX) > limitPowerChange) {
                changeX = Math.signum(changeX) * limitPowerChange;
            }

            if (Math.abs(changeRX) > limitPowerChange) {
                changeRX = Math.signum(changeRX) * limitPowerChange;
            }

            if (Math.abs(changeY) > limitPowerChange) {
                changeY = Math.signum(changeY) * limitPowerChange;
            }
        }
        x = lastX + changeX;
        lastX = x;
        rx = lastRx + changeRX;
        lastRx = rx;
        y = lastY + changeY;
        lastY = y;

        if (gamepad2.right_trigger > 0 || gamepad2.left_trigger > 0) {
            presetLiftHeightsMode = false;
        } else if (gamepad2.dpad_up || gamepad2.dpad_down || gamepad2.dpad_right || gamepad2.dpad_left || gamepad2.right_bumper || gamepad2.left_bumper) {
            presetLiftHeightsMode = true;
        }
        if (gamepad2.x && xPressed) {
            board.resetLift();
            targetPositionManualControl = (int) board.getLift();
        }
        if (gamepad2.y && !yPressed) {
            liftCorrection = !liftCorrection;
        }
        xPressed = gamepad2.x;
        yPressed = gamepad2.y;
        if (!presetLiftHeightsMode) { //manual lift
            targetPositionManualControl += (gamepad2.right_trigger - gamepad2.left_trigger) * 100;
            //the right trigger makes the lift go up and the left trigger makes the lift go down
            if (liftCorrection) {
                targetPositionManualControl = Math.min(heights.highJunction, targetPositionManualControl);
                targetPositionManualControl = Math.max(0, targetPositionManualControl);
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
            if (liftCorrection) {
                telemetry.addData("is over/undershoot enabled?", "no");
            } else {
                telemetry.addData("is over/undershoot enabled?", "yes");
            }
        } else { //lift uses preset heights
            if (gamepad2.dpad_up) { //dpad up increases the preset's value
                if (targetPositionPresetHeights == 0 || targetPositionPresetHeights == heights.groundJunction) {
                    targetPositionPresetHeights = heights.lowJunction;
                } else if ((targetPositionPresetHeights == heights.lowJunction) && !upPressed) {
                    targetPositionPresetHeights = heights.midJunction;
                } else if ((targetPositionPresetHeights == heights.midJunction) && !upPressed) {
                    targetPositionPresetHeights = heights.highJunction;
                }
            } else if (gamepad2.dpad_down) {//dpad down decreases the preset's value
                if (targetPositionPresetHeights == heights.highJunction) {
                    targetPositionPresetHeights = heights.midJunction;
                } else if ((targetPositionPresetHeights == heights.midJunction) && !downPressed) {
                    targetPositionPresetHeights = heights.lowJunction;
                } else if ((targetPositionPresetHeights == heights.lowJunction || targetPositionPresetHeights == heights.groundJunction) && !downPressed) {
                    targetPositionPresetHeights = 0;
                }
            } else if (gamepad2.left_bumper) {//the left bumper sets the preset to ground junction
                targetPositionPresetHeights = heights.groundJunction;
            } else if (gamepad2.right_bumper) { //the right bumper sets the preset to just above the cone
                targetPositionPresetHeights = heights.rightAboveACone;
                board.setClaw(0);
                telemetry.addData("claw state", "release");
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
        if (gamepad1.a) {
            board.resetHeading();
        }
        if (gamepad1.right_bumper) {
            board.driveFieldRelative(y / 4, x / 4, rx / 4); //slow mode
            telemetry.addData("Mode", "Slow");
        } else if (gamepad1.left_bumper) {
            board.driveFieldRelative(y, x, rx); //speed mode
            telemetry.addData("Mode", "Speed");
        } else {
            board.driveFieldRelative(y * 0.6, x * 0.6, rx * 0.6); //normal mode
            telemetry.addData("Mode", "Normal");
        }
        telemetry.addData("degrees", board.getNormalizedDegrees());
    }

    @Override
    public void stop() {
        StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS);
    }
}