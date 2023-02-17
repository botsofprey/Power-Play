package OpMode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//import Subsystems.AprilTagCamera;
//import Subsystems.Claw;
//import Subsystems.ClawArm;
//import Subsystems.Lift;
//import UtilityClasses.Controller;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Right Auto RR", group = "Autonomous")
public class RightAutoRoadrunner extends LinearOpMode {
    //private Lift lift;
    //private ClawArm clawArm;
    //private Claw claw;


    @Override
    public void runOpMode() throws InterruptedException {
        //AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        //lift = new Lift(hardwareMap);
        //clawArm = new ClawArm(hardwareMap);
        //claw = new Claw(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // We want to start the bot at x: 10, y: -8, heading: 90 degrees
        Pose2d startPose = new Pose2d(42, -61.4, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence ts = drive.trajectorySequenceBuilder(startPose)
                .lineToLinearHeading(new Pose2d(36, -61.4, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(36, -12, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(34, -4, Math.toRadians(-15)))
                .build();

        drive.followTrajectorySequence(ts);
    }
}
