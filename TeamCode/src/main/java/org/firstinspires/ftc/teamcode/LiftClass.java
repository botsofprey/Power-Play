package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp()
public class LiftClass extends Opmode {
    DcMotor lift;

    @Override
    public void init() {
        lift = hardwareMap.DcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        if (gamepad1.x) {
            resetRunTime;
            while (getRunTime() < 5) {
                lift.setPower(0.5);
            }
            lift.setPower(0);
        }
    }
}
