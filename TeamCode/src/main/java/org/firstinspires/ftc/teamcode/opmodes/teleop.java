package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mecanumDriveOLD.mecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@TeleOp()
public class teleop extends OpMode{
    //set up objects for use in teleop
    motorProgrammingBoard mpb = new motorProgrammingBoard();
    mecanumDrive teleDriveBase = new mecanumDrive();
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
        boolean a = gamepad1.a;
        boolean leftbumper = gamepad1.left_bumper;
        boolean rightbumper = gamepad1.right_bumper;

        //track the inputs of the controller
        telemetry.addData("Left Stick X",leftStickX);
        telemetry.addData("Left Stick Y",leftStickY);
        telemetry.addData("Turn (Right Stick X)",rightStickX);
        telemetry.addData("A button",a);
        telemetry.addData("Left Bumper",leftbumper);

        //send control of movement calculation to moveRobot and set return array of function equal
        //to moveRobotReturn
        moveRobotReturn = teleDriveBase.moveRobot(rightStickX, leftStickY, leftStickX);

        //set returned values in moveRobotReturn as power values for motors
        mpb.motorFrontLeft.setPower(moveRobotReturn[0]);
        mpb.motorFrontRight.setPower(moveRobotReturn[1]);
        mpb.motorBackLeft.setPower(moveRobotReturn[2]);
        mpb.motorBackRight.setPower(moveRobotReturn[3]);


    }
}