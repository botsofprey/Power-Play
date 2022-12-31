package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp()
public class TestImu extends OpMode {
    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
    }

    public void loop() {
        telemetry.addData("Angle", board.getHeading(AngleUnit.RADIANS));
    }
    public void stop(){
        StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS);
    }
}
