package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(group = "Robot Speech")
public class RobotSpeech extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        telemetry.speak("hi");
    }
}
