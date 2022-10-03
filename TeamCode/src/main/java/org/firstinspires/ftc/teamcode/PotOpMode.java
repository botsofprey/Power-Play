package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ProgrammingBoard1;

@TeleOp()
public class PotOpMode extends OpMode {
    ProgrammingBoard1 board = new ProgrammingBoard1();

    @Override
    public void init() {
        board.init(hardwareMap);
    }


    @Override
    public void loop() {
        double potValue = board.getPotRange();

        board.setServoPosition(potValue);
        telemetry.addData("Pot Angle", board.getPotAngle());
    }

}
