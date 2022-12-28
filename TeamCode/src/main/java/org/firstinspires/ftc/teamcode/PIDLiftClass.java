package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp()
public class PIDLiftClass extends OpMode {
    HardwareMechanisms board = new HardwareMechanisms();
    HeightsList heights = new HeightsList();
    int targetPosition = 0;

    public void init() {
        board.init(hardwareMap);
        board.setLift(targetPosition);
    }

    public void loop() {

        if (board.getLift() <= 0) {
            targetPosition += (int) gamepad2.right_trigger * 15;
        } else if (board.getLift() <= heights.highJunction) {
            targetPosition += (int) ((gamepad2.right_trigger - gamepad2.left_trigger) * 15);
        } else {
            targetPosition -= (int) gamepad2.left_trigger * 15;
        }
        if (board.getLift() < 0) { //keeps the lift from going below 0
            targetPosition = 0;
        }
        board.setLift(targetPosition);
        telemetry.addData("target position", targetPosition);
        telemetry.addData("current position", board.getLift());
    }
}