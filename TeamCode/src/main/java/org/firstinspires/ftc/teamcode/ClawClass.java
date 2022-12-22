package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ClawClass extends OpMode {
    double targetClaw;
    HardwareMechanisms board = new HardwareMechanisms();

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad2.a) {
            targetClaw -= 0.001;
        } else if (gamepad2.b) {
            targetClaw += 0.001;
        }
        board.setClaw(targetClaw);
        telemetry.addData("claw position", board.getClaw());
    }

}
