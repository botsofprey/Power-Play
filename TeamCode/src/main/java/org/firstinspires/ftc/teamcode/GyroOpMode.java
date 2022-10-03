package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ProgrammingBoard1;

@TeleOp()

public class GyroOpMode extends OpMode {
    ProgrammingBoard1 board = new ProgrammingBoard1();

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        double headingDegrees = board.getHeading(AngleUnit.DEGREES);
        telemetry.addData("Our Heading (Degrees)", board.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Our Heading (Radians)", board.getHeading(AngleUnit.RADIANS));
        board.setMotorSpeed(headingDegrees);
    }

}
