package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ProgrammingBoard7;

import java.lang.reflect.Member;

import DriveEngine.MecanumDrive;

@Autonomous(name="Auto Test", group = "Autonomous")
public class AutoNatalia extends LinearOpMode {
    ProgrammingBoard7 board = new ProgrammingBoard7();
    private MecanumDrive drive;

    private Servo servo;
    private Servo claw;
    private DcMotor motor;
    private DcMotor liftMotor;
    public void runOpMode() throws InterruptedException
    {}

    @Override
    public void waitForStart() {
        super.waitForStart();
    }

    //void loop() {
    //    board.init(hardwareMap);
    //}

    {
        new AutoNatalia();
                    /*
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(0);
        drive.moveCenti(160, MecanumDrive.RIGHT);
        drive.moveCenti(60 , MecanumDrive.FORWARD);
        claw.setPosition(0);
        drive.rotateToAngle(135);
        claw.setPosition(1);

        //repeat if time allows
        servo.setPosition(0);

        //no clue how to do the claw or lift
        //call claw:
        //call lift:
                    */

        //starting over
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(1);
        drive.moveCenti(210,MecanumDrive.RIGHT);
        drive.moveCenti(90, MecanumDrive.FORWARD);
        drive.rotateToAngle(-90);
        drive.moveCenti(60,MecanumDrive.FORWARD);
        drive.rotateToAngle(45);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.rotateToAngle(-45);
        drive.moveCenti(60, MecanumDrive.BACKWARD);
        drive.rotateToAngle(90);
        drive.moveCenti(90, MecanumDrive.BACKWARD);
        drive.brake();

        //another one
        //figure out true north
        private void moveTrueNorth(double x, double y, double h) {

    }

}
