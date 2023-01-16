package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.HardwareMechanisms;

@TeleOp()
public class RecalibrateHeightsForConeStack extends OpMode {
    int coneStackHeight = 5;
    boolean aPressed = false;
    int liftPosition;
    int fiveStackHeight;
    int fourStackHeight;
    int threeStackHeight;
    int twoStackHeight;
    int oneStackHeight;

    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
    }

    public void loop() {
        if (gamepad1.a && !aPressed) {
            coneStackHeight -= 1;
        }

        liftPosition += gamepad1.right_trigger - gamepad1.left_trigger;
        board.setLift(liftPosition);

        if (coneStackHeight == 5) {
            fiveStackHeight = (int) board.getLift();
            telemetry.addData("current cone stack height", 5);
        } else if (coneStackHeight == 4) {
            fourStackHeight = (int) board.getLift();
            telemetry.addData("current cone stack height", 4);
        } else if (coneStackHeight == 3) {
            threeStackHeight = (int) board.getLift();
            telemetry.addData("current cone stack height", 3);
        } else if (coneStackHeight == 2) {
            twoStackHeight = (int) board.getLift();
            telemetry.addData("current cone stack height", 2);
        } else if (coneStackHeight == 1) {
            oneStackHeight = (int) board.getLift();
            telemetry.addData("current cone stack height", 1);
        } else {
            telemetry.addData("five stack cone height", fiveStackHeight);
            telemetry.addData("four stack cone height", fourStackHeight);
            telemetry.addData("three stack cone height", threeStackHeight);
            telemetry.addData("two stack cone height", twoStackHeight);
            telemetry.addData("five stack cone height", oneStackHeight);
        }
    }
}
