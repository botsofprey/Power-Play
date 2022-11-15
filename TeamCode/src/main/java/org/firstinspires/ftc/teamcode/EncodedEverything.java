package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This is the combination of all of our TeleOp codes into one
 */
@TeleOp()
public class EncodedEverything extends OpMode {
    HardwareMechanisms board = new HardwareMechanisms();
    /**
     * Used so the driver doesn't have to hold the right bumper for slow mode
     */
    boolean slowModeAlreadyOn = false;
    /**
     * Used so the driver doesn't have to hold the left bumper for speed mode
     */
    boolean speedModeAlreadyOn = false;
    /**
     * Used as a way to alter the servo's position
     */
    double clawPosition;

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        if (1650 >= board.getLift()) {
            board.setLift((gamepad2.right_trigger - gamepad2.left_trigger) / 2);
        } else if (board.getLift() >= 0) {
            board.setLift(gamepad2.right_trigger / 2);
        } else {
            board.setLift(-gamepad2.left_trigger / 2);
        }

        if (gamepad2.a) {
            clawPosition += 0.001;
        } else if (gamepad2.b) {
            clawPosition -= 0.001;
        }
        board.setClaw(clawPosition);


        if (((gamepad1.right_bumper) || (slowModeAlreadyOn)) && (!gamepad1.left_bumper) && (!gamepad1.y)) {
            board.motorFrontLeft.setPower(frontLeftPower / 4);
            board.motorBackLeft.setPower(backLeftPower / 4);
            board.motorFrontRight.setPower(frontRightPower / 4);
            board.motorBackRight.setPower(backRightPower / 4);
            telemetry.addData("Mode", "Slow");
            slowModeAlreadyOn = true;
            if (speedModeAlreadyOn) {
                speedModeAlreadyOn = false;
            }
        } else if (((gamepad1.left_bumper) || (speedModeAlreadyOn)) && (!gamepad1.y)) {
            board.motorFrontLeft.setPower(frontLeftPower);
            board.motorBackLeft.setPower(backLeftPower);
            board.motorFrontRight.setPower(frontRightPower);
            board.motorBackRight.setPower(backRightPower);
            telemetry.addData("Mode", "Speed");
            speedModeAlreadyOn = true;
            if (slowModeAlreadyOn) {
                slowModeAlreadyOn = false;
            }
        } else {
            board.motorFrontLeft.setPower(frontLeftPower / 2);
            board.motorBackLeft.setPower(backLeftPower / 2);
            board.motorFrontRight.setPower(frontRightPower / 2);
            board.motorBackRight.setPower(backRightPower / 2);
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