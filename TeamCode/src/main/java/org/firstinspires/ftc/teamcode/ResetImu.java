package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class ResetImu extends OpMode {
    public void init() {
        Static.imuValue = 0;
    }

    public void loop() {
        Static.imuValue = 0;
    }
}
