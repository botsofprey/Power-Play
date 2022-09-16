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
        if(gamepad1.a) {
                telemetry.addData("gamepad1 right", gamepad1.right_stick_x);
                telemetry.addData("gamepad1 left", gamepad1.left_stick_x);
            }
        else if(gamepad1.a == false){
            telemetry.addData("gamepad1 left", gamepad1.right_stick_x);
            telemetry.addData("gamepad1 right", gamepad1.left_stick_x);
        }
    }
}
