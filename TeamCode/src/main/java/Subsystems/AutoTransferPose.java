package Subsystems;

//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;

import UtilityClasses.Vector2D;

public abstract class AutoTransferPose extends LinearOpMode {
    @Override

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        HardwareMap hardwareMap = null;

        Pose2d startPose = new Pose2d(10, 15, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = null;
        traj = drive.trajectoryBuilder(startPose);
        try {
            splineTo(new Vector2D(45, 45), 0)
            .build(null);
        } catch (CertPathBuilderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

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

    protected abstract CertPathBuilder splineTo(Vector2D vector2D, int i);

    public class Pose2d {
        public Pose2d(int i, int i1, double toRadians) {
        }
    }

    public class Trajectory {
    }
}
