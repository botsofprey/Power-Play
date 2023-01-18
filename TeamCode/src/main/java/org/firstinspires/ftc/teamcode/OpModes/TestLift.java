package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;
@TeleOp
public class TestLift extends OpMode {
    int position;
    HardwareMechanisms board = new HardwareMechanisms();
   public void init(){
        board.init(hardwareMap);
    }
    public void loop(){
       position += gamepad1.right_trigger - gamepad1.left_trigger;
       board.setLift(position);
        telemetry.addData("lift", board.getLift());
    }
}
