package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class LearnJava extends OpMode {
    @Override
    public void init() {
    }
    @Override
    public void loop() {
        telemetry.addData("Gamepad 1 Right Stick", gamepad1.right_stick_y);
        telemetry.addData("Gamepad 1 B button", gamepad1.b);
        double rightleft = gamepad1.left_stick_y - gamepad1.right_stick_y;
        telemetry.addData("rightleft", rightleft);
        double triggersum = gamepad1.right_trigger + gamepad1.left_trigger;
        telemetry.addData("triggersum", triggersum);
    }
}
