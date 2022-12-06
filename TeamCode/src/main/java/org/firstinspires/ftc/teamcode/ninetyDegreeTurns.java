package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ninetyDegreeTurns extends OpMode {
    HardwareMechanisms board = new HardwareMechanisms();

    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            if (!(board.getHeading(AngleUnit.DEGREES) == 0)) {
                if (board.getHeading(AngleUnit.DEGREES) >= 180) {
                    board.driveFieldRelative(0, 0, 0.5);
                } else if (board.getHeading(AngleUnit.DEGREES) <= 180) {
                    board.driveFieldRelative(0, 0, -0.5);
                }
            } else if (gamepad1.dpad_right) {
                if (!(board.getHeading(AngleUnit.DEGREES) == 90)) {
                    if (board.getHeading(AngleUnit.DEGREES) > 90) {
                        board.driveFieldRelative(0, 0, -0.5);
                    } else if (board.getHeading(AngleUnit.DEGREES) < 90) {
                        board.driveFieldRelative(0, 0, 0.5);
                    }
                }
            } else if (gamepad1.dpad_down) {
                if (!(board.getHeading(AngleUnit.DEGREES) == 180)) {
                    if (board.getHeading(AngleUnit.DEGREES) > 180) {
                        board.driveFieldRelative(0, 0, -0.5);
                    } else if (board.getHeading(AngleUnit.DEGREES) < 180) {
                        board.driveFieldRelative(0, 0, 0.5);
                    }
                }
            } else if (gamepad1.dpad_left) {
                if (!(board.getHeading(AngleUnit.DEGREES) == 270)) {
                    if (board.getHeading(AngleUnit.DEGREES) > 270) {
                        board.driveFieldRelative(0, 0, -0.5);
                    } else if (board.getHeading(AngleUnit.DEGREES) < 270)
                        board.driveFieldRelative(0, 0, 0.5);
                }
            }
        }
    }
}