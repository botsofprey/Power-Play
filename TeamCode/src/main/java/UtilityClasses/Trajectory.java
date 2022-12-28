package UtilityClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Subsystems.MecanumDrive;
import Subsystems.Pose2d;

public class Trajectory extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(10)
                .forward(5)
                .build();
        waitForStart();

        if(isStopRequested()) return;
        drive.followTrajectory(myTrajectory);
    }

    public double duration() {
        return 0;
    }

    public Object end() {
        return null;
    }
}
