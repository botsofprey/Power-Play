package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard2;

@TeleOp
public class TouchSensorOpMode3 extends OpMode {
    ProgrammingBoard2 board = new ProgrammingBoard2();

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        String touchSensorString = "not pressed";
        if (board.isTouchSensorPressed()) {
            touchSensorString = "Pressed";
        }
        telemetry.addData("Touch sensor", touchSensorString);
    }
}