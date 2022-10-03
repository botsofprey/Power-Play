package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class GamepadMathEx extends OpMode {
    @Override
    public void init(){
    }

    @Override
    public void loop(){

        double speedForwardL = -gamepad1.left_stick_y/2.0;
        double speedForwardR = -gamepad1.right_stick_y/2.0;

        telemetry.addData("Right stick x", gamepad1.right_stick_x);
        telemetry.addData("Right stick y", gamepad1.right_stick_y);
        telemetry.addData("B button", gamepad1.b);

        telemetry.addData("Left stick speed forward", speedForwardL);
        telemetry.addData("Right stick speed forward", speedForwardR);
    }
}
