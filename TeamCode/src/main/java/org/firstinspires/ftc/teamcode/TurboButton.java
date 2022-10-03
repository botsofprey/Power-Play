package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TurboButton extends OpMode {

    @Override
    public void init(){
    }

    @Override
    public void loop(){
        double fwdSpeed = gamepad1.left_stick_y;
        //b button is the turbo button i guess
        if (!gamepad1.b) {
            fwdSpeed *= 0.5;
        }
        telemetry.addData("Forward speed", fwdSpeed);
    }
}

