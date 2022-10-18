package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class LiftClass extends OpMode {
    DcMotor lift;

    @Override
    public void init() {
        lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            resetStartTime();
            while (getRuntime() < 5) {
                lift.setPower(0.5);
            }
            lift.setPower(0);
        }
    }
}
