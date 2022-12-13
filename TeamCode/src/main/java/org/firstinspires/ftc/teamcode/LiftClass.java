package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class LiftClass extends OpMode {
    DcMotor lift;
    int targetPosition = 0;
    int lowJunction = 2500; //dummy numbers to be replaced
    int midJunction = 5000;
    int highJunction = 10000;

    @Override
    public void init() {
        lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(targetPosition);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
            } else if (targetPosition == lowJunction) {
                targetPosition = 0;
            }
        }
        lift.setTargetPosition(targetPosition);
        lift.setPower(0.08);
        telemetry.addData("position", lift.getCurrentPosition());
        telemetry.addData("target position", targetPosition);
        }
    }