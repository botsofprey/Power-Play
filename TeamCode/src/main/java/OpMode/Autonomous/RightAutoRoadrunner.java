package OpMode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//import Subsystems.AprilTagCamera;
//import Subsystems.Claw;
//import Subsystems.ClawArm;
//import Subsystems.Lift;
//import UtilityClasses.Controller;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import Subsystems.AprilTagPipelineExampleCOPY;
import UtilityClasses.cameraControl;

import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous(name = "Right Auto RR", group = "Autonomous")
public class RightAutoRoadrunner extends LinearOpMode {
    //private Lift lift;
    //private ClawArm clawArm;
    //private Claw claw;
    private AprilTagDetection tagData;
    private AprilTagPipelineExampleCOPY aprilTagPipeline;
    private cameraControl camera;
    int tagOfInterest;


    @Override
    public void runOpMode() throws InterruptedException {
        //AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        //lift = new Lift(hardwareMap);
        //clawArm = new ClawArm(hardwareMap);
        //claw = new Claw(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        camera.createCameraInstance(hardwareMap, telemetry);
        aprilTagPipeline = new AprilTagPipelineExampleCOPY(tagsize, fx, fy, cx, cy);
        camera.createCameraInstance(hardwareMap, telemetry);
        camera.camera.setPipeline(aprilTagPipeline);
        tagData = new AprilTagDetection();

        // We want to start the bot at x: 10, y: -8, heading: 90 degrees
        Pose2d startPose = new Pose2d(42, -61.4, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence ts = drive.trajectorySequenceBuilder(startPose)
                .lineToLinearHeading(new Pose2d(36, -61.4, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(34, -4, Math.toRadians(-15)))
                .build();

        TrajectorySequence park17 = drive.trajectorySequenceBuilder(ts.end())
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(12, -12, Math.toRadians(90)))
                .build();
        TrajectorySequence park18 = drive.trajectorySequenceBuilder(ts.end())
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .build();
        TrajectorySequence park19 = drive.trajectorySequenceBuilder(ts.end())
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(60, -12, Math.toRadians(90)))
                .build();

        while (!isStarted() && !isStopRequested()) {
            tagData = null;
            ArrayList<AprilTagDetection> currentDetections;
            currentDetections = aprilTagPipeline.getLatestDetections();

            for (AprilTagDetection tag : currentDetections) {
                if (currentDetections.size() != 0 && tag.id >= 17 && tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    break;
                }
                if (currentDetections.size() == 0)
                    telemetry.addLine("No tag found");
            }
            telemetry.update();
        }
        //start

        while(!isStopRequested()) {
            drive.followTrajectorySequence(ts);

            if (tagOfInterest == 17)
                drive.followTrajectorySequence(park17);
            if (tagOfInterest == 18)
                drive.followTrajectorySequence(park18);
            if (tagOfInterest == 19)
                drive.followTrajectorySequence(park19);
        }
    }
}
