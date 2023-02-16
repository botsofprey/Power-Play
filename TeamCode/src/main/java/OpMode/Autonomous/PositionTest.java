package OpMode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class PositionTest extends LinearOpMode {
    SampleMecanumDrive roadRunr;

    @Override
    public void runOpMode() throws InterruptedException {
        roadRunr = new SampleMecanumDrive(hardwareMap);

        roadRunr.setPoseEstimate();
    }
}
