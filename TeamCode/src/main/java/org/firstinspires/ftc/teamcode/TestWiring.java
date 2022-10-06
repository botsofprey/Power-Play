package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ProgrammingBoard1;
import org.firstinspires.ftc.teamcode.TestItem;

import java.util.ArrayList;

@TeleOp()
public class TestWiring extends OpMode {
    ProgrammingBoard1 board = new ProgrammingBoard1();
    ArrayList<TestItem> tests;
    boolean wasDown, wasUp;
    int testNum;

    @Override
    public void init() {
        board.init(hardwareMap);
        tests = board.getTests();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up && !wasUp) {
            testNum--;
            if (testNum < 0) {
                testNum = tests.size() - 1;
            }
        }
        wasUp = gamepad1.dpad_up;


        if (gamepad1.dpad_down && !wasDown) {
            testNum++;
            if (testNum >= tests.size()) {
                testNum = 0;
            }
        }
        wasDown = gamepad1.dpad_down;
        telemetry.addLine("Use Up and Down on D-pad to cycle through choices");
        telemetry.addLine("Press A to run test");
        TestItem currTest = tests.get(testNum);
        telemetry.addData("Test:", currTest.getDescription());
        currTest.run(gamepad1.a, telemetry);
    }
}
