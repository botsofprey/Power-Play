package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MecanumDrivetrain extends OpMode {

    HardwareMechanisms board = new HardwareMechanisms();

    @Override
    public void init() {
        board.init(hardwareMap);
    }


    public void loop() {
        board.y = -gamepad1.left_stick_y;
        board.x = gamepad1.left_stick_x;
        board.rx = gamepad1.right_stick_x;

        if (gamepad1.right_bumper) {
            board.driveFieldRelative(board.y / 4, board.x / 4, board.rx / 4);
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
