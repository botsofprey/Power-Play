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
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        controls.drive(y, x, rx);
    }
}
