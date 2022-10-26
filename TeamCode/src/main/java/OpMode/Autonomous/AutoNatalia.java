package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ProgrammingBoard7;

import DriveEngine.MecanumDrive;

@Autonomous(name="Auto Test", group = "Autonomous")
public class AutoNatalia extends LinearOpMode {
    ProgrammingBoard7 board = new ProgrammingBoard7()
    private MecanumDrive drive;

    public void runOpMode() throws InterruptedException {

    }
    @Override
    public void loop()
    board.init(hardwareMap)
}
