package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@TeleOp()
public class teleop extends OpMode{

    motorProgrammingBoard pb1 = new motorProgrammingBoard();
    driveBaseMovement teleDriveBase = new driveBaseMovement();
    double[] moveRobotReturn = new double[4];

    @Override
    public void init() {
        pb1.init(hardwareMap);
        teleDriveBase.driveSpeed = 2;
        teleDriveBase.setDriveMode("normal");
        pb1.setMotorSpeed((double)(teleDriveBase.driveMode));
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

        degreesARG = (rightStickX * teleDriveBase.driveSpeed) * 3.6;

        moveRobotReturn = teleDriveBase.moveRobot(degreesARG, LSYforward, LSYbackward,0,0 );
    }
}
