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

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        board.y = -gamepad1.left_stick_y;
        board.x = gamepad1.left_stick_x;
        board.rx = gamepad1.right_stick_x;

        if (board.getLift() <= 1650) {
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

        if (gamepad1.right_bumper) {
          board.driveFieldRelative(board.y / 4, board.x / 4, board.rx /4);
            telemetry.addData("Mode", "Slow");
        } else if (gamepad1.left_bumper) {
            board.driveFieldRelative(board.y, board.x, board.rx);
            telemetry.addData("Mode", "Speed");
        } else {
            board.driveFieldRelative(board.y / 2, board.x / 2, board.rx / 2);
            telemetry.addData("Mode", "Normal");
        }
    }
}