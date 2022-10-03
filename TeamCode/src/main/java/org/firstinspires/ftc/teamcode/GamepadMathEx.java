package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class GamepadMathEx extends OpMode {
    @Override
    public void init(){
    }

    @Override
    public void loop(){
        telemetry.addData("Right stick x", gamepad1.right_stick_x);
        telemetry.addData("Right stick y", gamepad1.right_stick_y);
        telemetry.addData("B button", gamepad1.b);
        telemetry.addData("sum triggers",
                gamepad1.right_trigger+gamepad1.right_trigger);

        telemetry.addData("Difference of left y and right y",
                gamepad1.left_stick_y - gamepad1.right_stick_y);
    }
}
