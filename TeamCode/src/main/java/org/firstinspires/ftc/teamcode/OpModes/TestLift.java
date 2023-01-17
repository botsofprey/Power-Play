package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;

public class TestLift extends OpMode {
    int position;
    HardwareMechanisms board = new HardwareMechanisms();
   public void init(){
        board.init(hardwareMap);
    }
    public void loop(){
       position += gamepad1.right_trigger - gamepad1.left_trigger;
        telemetry.addData("lift", board.getLift());
    }
}
