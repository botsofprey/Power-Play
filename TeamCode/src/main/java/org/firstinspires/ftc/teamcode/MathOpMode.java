package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class MathOpMode extends OpMode {
    @Override
    public void init(){
    }

    @Override
    public void loop(){
        double speedForward = -gamepad1.left_stick_y / 2.0;
        telemetry.addData("Left stick y", gamepad1.left_stick_y);
        telemetry.addData("Speed forward", speedForward);
    }
}
