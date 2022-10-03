package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CrazyMode extends OpMode {
    @Override
    public void init(){
    }
    @Override
    public void loop(){
        double SpeedX = gamepad1.left_stick_x;
        double SpeedY = gamepad1.left_stick_y;
        //b button is the crazy mode button too i guess
        if (gamepad1.b) {
            SpeedX = gamepad1.left_stick_x;
            SpeedY = gamepad1.left_stick_y;
        }
        else {
            telemetry.addData("joystick is"," normal");
        }
        telemetry.addData("speed of x", SpeedX);
        telemetry.addData("speed of y", SpeedY);
    }
}
