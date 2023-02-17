package OpMode.Autonomous;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class testPosition extends LinearOpMode {

    SampleMecanumDrive roadRunr;

    @Override
    public void runOpMode() throws InterruptedException {
        roadRunr = new SampleMecanumDrive(hardwareMap);

        waitForStart();
        if (isStarted()) {
            //x: 31.12509675479652 - 1.4809591325619242
            //y: -56.21113828014431 - 5.177230597917109
            roadRunr.setPoseEstimate(new Pose2d(41.644137,-61.388368888,Math.toRadians(90)));

            while(!isStopRequested()) {
                roadRunr.update();
                telemetry.addData("X: ", roadRunr.getPoseEstimate().getX());
                telemetry.addData("Y: ", roadRunr.getPoseEstimate().getY());
                telemetry.addData("Heading", roadRunr.getPoseEstimate().getHeading());
                telemetry.update();
            }
        }
    }
}
