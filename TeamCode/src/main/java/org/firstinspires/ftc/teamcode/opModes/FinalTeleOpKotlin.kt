package org.firstinspires.ftc.teamcode.opModes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms
import org.firstinspires.ftc.teamcode.vars.HeightsList
import org.firstinspires.ftc.teamcode.vars.StaticImu
import kotlin.math.abs
import kotlin.math.sign

/**
 * This is the combination of all of our TeleOp codes into one and then fixed;
 * this is it in Kotlin
 * @author Alex Bryan
 */
@TeleOp
class FinalTeleOpKotlin : OpMode() {
    /**
     * An var used for tracking the target position of the lift during the preset heights mode, which changes between 0, groundJunction, lowJunction, midJunction, and highJunction
     */
    private var targetPositionPresetHeights = 0

    /**
     * An var used to control the lift while under manual control
     */
    private var targetPositionManualControl = 0

    /**
     * A var used to make sure the acceleration isn't too high
     */
    private var limitPowerChange = 0.1

    /**
     * A double used to make sure the acceleration isn't too high, specifically for the speed mode
     */
    private var limitPowerChangeFast = 0.05

    /**
     * The variable used to represent the amount forward, implemented as gamepad1.left_stick_y
     */
    var y = 0.0

    /**
     * The variable used to represent the amount right, implemented as gamepad1.left_stick_x
     */
    var x = 0.0

    /**
     * The variable used to represent turning, implemented as gamepad1.right_stick_x
     */
    private var rx = 0.0

    /**
     * A double used to track the previous x value in order to limit acceleration
     */
    private var lastY = 0.0

    /**
     * A double used to track the previous rx value in order to limit acceleration
     */
    private var lastX = 0.0

    /**
     * A double used to track the previous y value in order to limit acceleration
     */
    private var lastRx = 0.0

    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    private var upPressed = false

    /**
     * A boolean made in order to make it so that up on the dpad on gamepad 2 has to be released before being counted again
     */
    private var downPressed = false

    /**
     * A boolean made in order to make it so that y on gamepad 2 has to be released before being counted again
     */
    private var yPressed = false

    /**
     * A boolean made in order to make it so that x on gamepad 2 has to be released before being counted again
     */
    private var xPressed = false

    /**
     * A boolean used to track whether the lift is manually controlled or uses preset heights
     */
    private var presetLiftHeightsMode = false

    /**
     * A boolean used to track whether the lift uses height limits
     */
    private var liftCorrection = true
    private var board = HardwareMechanisms()
    var heights = HeightsList()
    override fun init() {
        board.init(hardwareMap)
    }

    override fun loop() {
        y = -gamepad1.left_stick_y.toDouble()
        x = gamepad1.left_stick_x.toDouble()
        rx = gamepad1.right_stick_x.toDouble()

        //from about line 80 to 114 is code to limit acceleration
        var changeX = x - lastX
        var changeRX = rx - lastRx
        var changeY = y - lastY
        if (gamepad1.left_bumper) {
            if (abs(changeX) > limitPowerChangeFast) {
                changeX = sign(changeX) * limitPowerChangeFast
            }
            if (abs(changeRX) > limitPowerChangeFast) {
                changeRX = sign(changeRX) * limitPowerChangeFast
            }
            if (abs(changeY) > limitPowerChangeFast) {
                changeY = sign(changeY) * limitPowerChangeFast
            }
        } else {
            if (abs(changeX) > limitPowerChange) {
                changeX = sign(changeX) * limitPowerChange
            }
            if (abs(changeRX) > limitPowerChange) {
                changeRX = sign(changeRX) * limitPowerChange
            }
            if (abs(changeY) > limitPowerChange) {
                changeY = sign(changeY) * limitPowerChange
            }
        }

        x = lastX + changeX
        rx = lastRx + changeRX
        y = lastY + changeY

        lastX = x
        lastRx = rx
        lastY = y

        if (gamepad2.right_trigger > 0 || gamepad2.left_trigger > 0) {
            presetLiftHeightsMode = false
        } else if (gamepad2.dpad_up || gamepad2.dpad_down || gamepad2.dpad_right || gamepad2.dpad_left || gamepad2.right_bumper || gamepad2.left_bumper) {
            presetLiftHeightsMode = true
        }
        if (gamepad2.x && xPressed) {
            board.resetLift()
            targetPositionManualControl = board.getLift().toInt()
            telemetry.addLine("lift reset")
        }
        if (gamepad2.y && !yPressed) {
            liftCorrection = !liftCorrection
        }
        xPressed = gamepad2.x
        yPressed = gamepad2.y
        if (!presetLiftHeightsMode) { //manual lift
            targetPositionManualControl += ((gamepad2.right_trigger - gamepad2.left_trigger) * 100).toInt()
            //the right trigger makes the lift go up and the left trigger makes the lift go down
            if (liftCorrection) {
                targetPositionManualControl.coerceIn(0, heights.highJunction)
            }
            board.setLift(targetPositionManualControl)
            targetPositionPresetHeights = if (abs(board.getLift() - heights.highJunction) < abs(
                    board.getLift() - heights.midJunction
                )
            ) {
                heights.highJunction
            } else if (abs(board.getLift() - heights.midJunction) < abs(
                    board.getLift() - heights.lowJunction
                )
            ) {
                heights.midJunction
            } else if (abs(board.getLift()) < abs(board.getLift())) {
                heights.lowJunction
            } else {
                0
            }
            telemetry.addData("lift mode", "manual control")
            telemetry.addData("target position", "n/a")
            if (liftCorrection) {
                telemetry.addData("is over/undershoot enabled?", "no")
            } else {
                telemetry.addData("is over/undershoot enabled?", "yes")
            }
        } else { //lift uses preset heights
            if (gamepad2.dpad_up) { //dpad up increases the preset's value
                if (targetPositionPresetHeights == 0 || targetPositionPresetHeights == heights.groundJunction) {
                    targetPositionPresetHeights = heights.lowJunction
                } else if (targetPositionPresetHeights == heights.lowJunction && !upPressed) {
                    targetPositionPresetHeights = heights.midJunction
                } else if (targetPositionPresetHeights == heights.midJunction && !upPressed) {
                    targetPositionPresetHeights = heights.highJunction
                }
            } else if (gamepad2.dpad_down) { //dpad down decreases the preset's value
                if (targetPositionPresetHeights == heights.highJunction) {
                    targetPositionPresetHeights = heights.midJunction
                } else if (targetPositionPresetHeights == heights.midJunction && !downPressed) {
                    targetPositionPresetHeights = heights.lowJunction
                } else if ((targetPositionPresetHeights == heights.lowJunction || targetPositionPresetHeights == heights.groundJunction) && !downPressed) {
                    targetPositionPresetHeights = 0
                }
            } else if (gamepad2.left_bumper) { //the left bumper sets the preset to ground junction
                targetPositionPresetHeights = heights.rightAboveACone
            } else if (gamepad2.right_bumper) { //the right bumper sets the preset to just above the cone
                targetPositionPresetHeights = heights.groundJunction
                board.setClaw(0.0)
                telemetry.addData("claw state", "release")
            }
            board.setLift(targetPositionPresetHeights)
            telemetry.addData("lift mode", "preset heights")
            telemetry.addData("target position", targetPositionPresetHeights)
            upPressed = gamepad2.dpad_up
            downPressed = gamepad2.dpad_down
            targetPositionManualControl = board.getLift().toInt()
        }
        telemetry.addData("position", board.getLift())
        if (gamepad2.b) {
            board.setClaw(0.4)
            telemetry.addData("claw state", "grab")
        } else {
            board.setClaw(0.0)
            telemetry.addData("claw state", "release")
        }
        if (gamepad1.a) {
            board.resetHeading()
        }
        if (gamepad1.right_bumper) {
            board.driveFieldRelative(y / 4, x / 4, rx / 4) //slow mode
            telemetry.addData("Mode", "Slow")
        } else if (gamepad1.left_bumper) {
            board.driveFieldRelative(y, x, rx) //speed mode
            telemetry.addData("Mode", "Speed")
        } else {
            board.driveFieldRelative(y * 0.6, x * 0.6, rx * 0.6) //normal mode
            telemetry.addData("Mode", "Normal")
        }
        telemetry.addData("degrees", board.normalizedDegrees)
    }

    override fun stop() {
        StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS)
    }
}
