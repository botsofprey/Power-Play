package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class LiftClass extends OpMode {
    DcMotor lift;
    double targetPosition;
    double lowJunction = 0; //dummy numbers
    double midJunction = 1;
    double highJunction = 2;

    @Override
    public void init() {
        lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        if (gamepad2.a) {
            if (targetPosition == 0) {
                targetPosition = lowJunction;
            } else if (targetPosition == lowJunction) {
                targetPosition = midJunction;
            } else if (targetPosition == midJunction) {
                targetPosition = highJunction;
            }
        } else if (gamepad2.b) {
            if (targetPosition == highJunction) {
                targetPosition = midJunction;
            } else if (targetPosition == midJunction) {
                targetPosition = lowJunction;
            } else if(targetPosition == lowJunction){
                targetPosition = 0;
            }
        }
        if(lift.getCurrentPosition() < targetPosition){
            lift.setPower(0.5);
        } else if(lift.getCurrentPosition() > targetPosition){
            lift.setPower(-0.5);
        }
    }
}