package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class RobotLocationEX1OpMode extends OpMode {
    RobotLocationEX1 robotLocationEX1 = new RobotLocationEX1(0);

    @Override
    public void init() {
        robotLocationEX1.setAngle(0);
    }
    @Override
    public void loop(){
        if (gamepad1.b) {
            robotLocationEX1.turn(0.1);
        }
        else if (gamepad1.a) {
            robotLocationEX1.turn(-0.1);
        }
        telemetry.addData("Location", robotLocationEX1);
        telemetry.addData("Angle", robotLocationEX1.getAngle());
    }
}

