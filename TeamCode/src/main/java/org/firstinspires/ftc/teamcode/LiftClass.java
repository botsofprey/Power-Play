package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class LiftClass extends OpMode {
    DcMotor lift;
    public void init(){
        lift = hardwareMap.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void loop(){
        //right trigger goes up, left trigger goes down
        if (lift.getCurrentPosition() <= 0) {
            lift.setPower(gamepad2.right_trigger);
        }else if (lift.getCurrentPosition() <= 10000) {
            lift.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        } else {
            lift.setPower(-gamepad2.left_trigger);
        }
        if(lift.getCurrentPosition() < 0){
            lift.setTargetPosition(0);
            lift.setPower(0.05);
        }
        telemetry.addData("position", lift.getCurrentPosition());
    }
}
