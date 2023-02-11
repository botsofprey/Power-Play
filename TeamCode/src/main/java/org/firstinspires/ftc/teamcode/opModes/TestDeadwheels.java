package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;

@TeleOp
public class TestDeadwheels extends OpMode {
    Double[] wheelValues = new Double[3];
    SampleMecanumDrive drive;
    StandardTrackingWheelLocalizer wheels;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        wheels = new StandardTrackingWheelLocalizer(hardwareMap);
    }

    @Override
    public void loop() {
        wheels.getWheelPositions().toArray(wheelValues);
        telemetry.addData("Left Encoder", wheelValues[0]);
        telemetry.addData("Right Encoder", wheelValues[1]);
        telemetry.addData("Center Encoder", wheelValues[2]);
    }
}
