package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard4;

@TeleOp
public class MotorOpModeEX2 extends OpMode {
    ProgrammingBoard4 board = new ProgrammingBoard4();

    @Override
    public void init() {
        board.init(hardwareMap);
    }
    double squareInputWithSign(double input) {
        double output = input * input;
        if (input < 0) {
            output = output * -1;
        }
        return output;
    }
    @Override
    public void loop() {
        double motorSpeed = squareInputWithSign(gamepad1.left_stick_y);

        board.setMotorSpeed(motorSpeed);
    }
}
