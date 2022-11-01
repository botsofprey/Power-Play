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
    private DcMotor motor;
    public void runOpMode() throws InterruptedException {

    }
    @Override
    public void loop() {
        board.init(hardwareMap);
    }
    while(opModeIsActive(){
        drive.moveCenti(120, MecanumDrive.LEFT);
        servo.setPosition(1);
        drive.moveCenti(480, MecanumDrive.FORWARD);

        //pick up cone
        drive.rotateToAngle(135);
        //repeat if time allows
        servo.setPosition(0);

        motor.setMode();

        //no clue how to do the claw or lift
    }

}
