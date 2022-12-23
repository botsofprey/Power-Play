package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MecanumDrivetrain extends OpMode {
    /**
     * The variable used to represent the amount forward, implemented as gamepad1.left_stick_y
     */
    double y;
    /**
     * The variable used to represent the amount right, implemented as gamepad1.left_stick_x
     */
    double x;
    /**
     * The variable used to represent turning, implemented as gamepad1.right_stick_x
     */
    double rx;

    HardwareMechanisms board = new HardwareMechanisms();

    @Override
    public void init() {
        board.init(hardwareMap);
    }


    public void loop() {
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;

        if (gamepad1.right_bumper) {
            board.driveFieldRelative(y / 4, x / 4, rx / 4);
            telemetry.addData("Mode", "Slow");

        } else if (gamepad1.left_bumper) {
            board.driveFieldRelative(y, x, rx);
            telemetry.addData("Mode", "Speed");
        } else {
            board.driveFieldRelative(y / 2, x / 2, rx / 2);
            telemetry.addData("Mode", "Normal");
        }
    }
}
