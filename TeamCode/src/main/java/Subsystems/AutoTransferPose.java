package Subsystems;

//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class AutoTransferPose extends LinearOpMode {
    @Override

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(10, 15, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = null;
        //traj = drive.trajectoryBuilder(startPose);
                //.splineTo(new Vector2D(45, 45), 0)
                //.build();

        drive.followTrajectory(traj);

        sleep(2000);

        //drive.followTrajectory(
                //drive.trajectoryBuilder(traj.end())
                        //.splineTo(new Vector2D(15, 15), Math.toRadians(180))
                        //.build()
        //);
        Object PoseStorage ;{}
        //PoseStorage.currentPose = drive.getPoseEstimate();
    }

    public class Pose2d {
        public Pose2d(int i, int i1, double toRadians) {
        }
    }

    public class Trajectory {
    }
}
