package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ProgrammingBoard7;

import DriveEngine.MecanumDrive;

@Autonomous(name="Auto Test", group = "Autonomous")
public class AutoNatalia extends LinearOpMode {
    ProgrammingBoard7 board = new ProgrammingBoard7();
    private MecanumDrive drive;

    private Servo servo;
    private Servo claw;
    private DcMotor motor;
    private DcMotor liftMotor;
    public void runOpMode() throws InterruptedException {

    }
    @Override
    public void loop() {
        board.init(hardwareMap);
    }

    {
        new AutoNatalia();
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
    }

}
