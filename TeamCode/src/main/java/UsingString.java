package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class UsingString extends OpMode {
    @Override
    public void init() {
        String myName = "Natalia Bernardo";

        telemetry.addData("Hi", myName);
    }
    @Override
    public void loop() {

    }
}