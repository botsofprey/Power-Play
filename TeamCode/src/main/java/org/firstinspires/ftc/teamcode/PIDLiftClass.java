package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.Encoder;


@TeleOp()
public class PIDLiftClass extends OpMode {
    Encoder encoder = new Encoder();
    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
        encoder.setDirection(Encoder.Direction.FORWARD);
    }

    public void loop() {

    }
}