package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class LearnJava extends OpMode {
    @Override
    public void init() {
        String myname = "Alex Bryan";

        telemetry.addData("Hello", myname);
    }
    @Override
    public void loop(){

    }
}
