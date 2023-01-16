package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vars.StaticImu;

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
