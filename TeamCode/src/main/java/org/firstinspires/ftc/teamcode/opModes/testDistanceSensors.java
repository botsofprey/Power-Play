package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DistanceSensor.angleFromDistance;

public class testDistanceSensors extends LinearOpMode {

    angleFromDistance afd = new angleFromDistance(hardwareMap, telemetry);

    @Override
    public void runOpMode() throws InterruptedException {
        while(!isStopRequested()) {
            telemetry.addData("Current Angle:", afd.getAngleOfRobot());
        }
    }
}
