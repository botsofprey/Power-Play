package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@TeleOp()
public class teleop extends OpMode{

    double[] moveRobotReturn = new double[4];

    @Override
    public void init() {
        motorProgrammingBoard pb1 = new motorProgrammingBoard();
        pb1.init(hardwareMap);
        driveBaseMovement.driveSpeed = 2;
        driveBaseMovement.setDriveMode("normal");
        pb1.setMotorSpeed((double)(driveBaseMovement.driveMode));
    }
    @Override
    public void loop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;

        double LSYbackward = 0.0;
        double LSYforward = 0.0;
        double degreesARG;

        if (leftStickY < 0.0)
            LSYbackward = -leftStickY/10;
        else
            LSYforward = leftStickY/10;

        degreesARG = (rightStickX * driveBaseMovement.driveSpeed) * 3.6;

        moveRobotReturn = driveBaseMovement.moveRobot(degreesARG, LSYforward, LSYbackward,0,0 );
    }
}
