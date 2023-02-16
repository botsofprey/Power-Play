package OpMode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class testPosition extends LinearOpMode {

    SampleMecanumDrive roadRunr;

    @Override
    public void runOpMode() throws InterruptedException {
        roadRunr = new SampleMecanumDrive(hardwareMap);
        roadRunr.setPoseEstimate(0,0);

        waitForStart();
        if (isStarted()) {
            while(!isStopRequested()) {

            }
        }
    }
}
