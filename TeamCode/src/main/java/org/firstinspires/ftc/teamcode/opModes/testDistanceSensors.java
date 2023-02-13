package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DistanceSensor.angleFromDistance;

@TeleOp
public class testDistanceSensors extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        angleFromDistance afd = new angleFromDistance(hardwareMap, telemetry);

        while(!isStopRequested()) {
            telemetry.addData("Current Angle:", afd.getAngleOfRobot());
        }
    }
}
