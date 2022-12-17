package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This is the combination of all of our TeleOp codes into one
 */

@TeleOp()
public class EncodedEverything extends OpMode {

    double limitPowerChange = 0.05;
    double limitPowerChangeFast = 0.01;
    double lastX;
    double lastRx;
    double lastY;

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

        //right trigger goes up, left trigger goes down
        if (board.getLift() <= 0) {
            board.setLift(gamepad2.right_trigger / 1.5);
        }else if (board.getLift() <= 10000) {
            board.setLift((gamepad2.right_trigger - gamepad2.left_trigger) / 1.5);
        } else {
            board.setLift(-gamepad2.left_trigger / 1.5);
        }
        if(board.getLift() < 0){
            board.lift.setTargetPosition(0);
            board.lift.setPower(0.05);
        }

        if(gamepad2.a) {
            board.setClaw(0);
        } else if(gamepad2.b){
            board.setClaw(0.25);
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