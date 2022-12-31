package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class ResetImu extends OpMode {
    public void init() {
        StaticImu.imuStatic = 0;
    }

    public void loop() {
        StaticImu.imuStatic = 0;
    }

    public void stop() {
        StaticImu.imuStatic = 0;
    }
}
