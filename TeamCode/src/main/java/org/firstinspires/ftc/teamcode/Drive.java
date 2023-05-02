package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Drive extends OpMode {
    DriveControls controls = new DriveControls();
    @Override
    public void init(){
        controls.getHardware(hardwareMap);
    }
    @Override
    public void loop(){
        double y = gamepad1.left_stick_y * 0.6;
        double x = -gamepad1.left_stick_x * 0.6;
        double rx = gamepad1.right_stick_x * 0.6;
        controls.driveFieldRelative(y, x, rx);
    }
}
