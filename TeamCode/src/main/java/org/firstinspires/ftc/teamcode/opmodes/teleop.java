package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@TeleOp()
public class teleop extends OpMode{
    //set up objects for use in teleop
    motorProgrammingBoard mpb = new motorProgrammingBoard();
    driveBaseMovement teleDriveBase = new driveBaseMovement();
    double[] moveRobotReturn = new double[4];

    @Override
    public void init() {
        mpb.init(hardwareMap);
        teleDriveBase.normalMode = 2;
        teleDriveBase.setFastMode();
        teleDriveBase.setDriveMode("normal");
    }
    @Override
    public void loop() {
        //map gamepad input to local variables
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;

        //track the inputs of the controller
        telemetry.addData("Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Turn (Right Stick X)",gamepad1.right_stick_x);

        //convert right stick's x axis input into usable input for moveRobot  function
        double degreesARG = (rightStickX * teleDriveBase.driveMode) * 3.6;

        //send control of movement calculation to moveRobot and set return array of function equal
        //to moveRobotReturn
        moveRobotReturn = teleDriveBase.moveRobot(degreesARG, leftStickY, leftStickX);

        //set returned values in moveRobotReturn as power values for motors
        mpb.motorFrontLeft.setPower(moveRobotReturn[0]);
        mpb.motorFrontRight.setPower(moveRobotReturn[1]);
        mpb.motorBackLeft.setPower(moveRobotReturn[2]);
        mpb.motorBackRight.setPower(moveRobotReturn[3]);

    }
}