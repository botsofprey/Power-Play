package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous
public class LastMinuteScrimmageAuto extends LinearOpMode {
    HardwareMechanisms board = new HardwareMechanisms();

    public void runOpMode() {
        board.init(hardwareMap);
        board.drive.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        board.drive.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        board.drive.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        board.drive.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        if (opModeIsActive()) {
            board.drive.motorBackLeft.setTargetPosition(-300);
            board.drive.motorFrontLeft.setTargetPosition(-300);
            board.drive.motorBackRight.setTargetPosition(-300);
            board.drive.motorFrontRight.setTargetPosition(-300);
            board.drive.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            board.drive.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            board.drive.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            board.drive.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            board.drive.motorFrontLeft.setPower(1);
            board.drive.motorBackLeft.setPower(1);
            board.drive.motorFrontRight.setPower(1);
            board.drive.motorBackRight.setPower(1);
            board.setClaw(0.4);

        }
        while (opModeIsActive() && (board.drive.motorFrontRight.isBusy() || board.drive.motorBackRight.isBusy() || board.drive.motorFrontLeft.isBusy() || board.drive.motorBackLeft.isBusy())) {
        }
        board.drive.motorFrontLeft.setPower(0);
        board.drive.motorBackLeft.setPower(0);
        board.drive.motorFrontRight.setPower(0);
        board.drive.motorBackRight.setPower(0);

        StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS) + 1.5708;

        if (isStopRequested()) {
            board.drive.motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            board.drive.motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            board.drive.motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            board.drive.motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            StaticImu.imuStatic = board.getHeading(AngleUnit.RADIANS) + 1.5708;
        }
    }
}
