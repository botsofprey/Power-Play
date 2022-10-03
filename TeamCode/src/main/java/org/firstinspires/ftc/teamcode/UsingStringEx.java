package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class UsingStringEx extends OpMode {
    @Override
    public void init() {
        String myName = "Natalia Bernardo";
        String myGrade = "10th grade";

        telemetry.addData("The user ", myName, " is in ", myGrade,".");
    }
    @Override
    public void loop() {

    }
}
