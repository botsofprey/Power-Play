package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard6;

@TeleOp()
public class PotOpMode extends OpMode {
    ProgrammingBoard6 board = new ProgrammingBoard6();
    @Override
    public void init(){
            board.init(hardwareMap);
    }
    @Override
    public void loop() {
        telemetry.addData("Pot Angle", board.getPotAngle());
    }
}
