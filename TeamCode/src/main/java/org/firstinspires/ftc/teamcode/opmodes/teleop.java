package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@TeleOp()
public class teleop extends OpMode{

    motorProgrammingBoard mpb = new motorProgrammingBoard();
    driveBaseMovement teleDriveBase = new driveBaseMovement();
    double[] moveRobotReturn = new double[4];

    @Override
    public void init() {
        mpb.init(hardwareMap);
        teleDriveBase.driveSpeed = 2;
        teleDriveBase.setFastMode();
        teleDriveBase.setDriveMode("normal");
    }
    @Override
    public void loop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;

        double LSYbackward = 0.0;
        double LSYforward = 0.0;
        double LSXbackward = 0.0;
        double LSXforward = 0.0;
        double degreesARG;

        if (leftStickY < 0.0)
            LSYbackward = -leftStickY;
        else
            LSYforward = leftStickY;

        degreesARG = (rightStickX * teleDriveBase.driveMode) * 3.6;

        moveRobotReturn = teleDriveBase.moveRobot(degreesARG, LSYforward, LSYbackward,0,0 );

        mpb.motorFrontLeft.setPower(moveRobotReturn[0]);
        mpb.motorFrontRight.setPower(moveRobotReturn[1]);
        mpb.motorBackLeft.setPower(moveRobotReturn[2]);
        mpb.motorBackRight.setPower(moveRobotReturn[3]);

    }
}
