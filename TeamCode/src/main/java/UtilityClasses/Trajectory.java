package UtilityClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Subsystems.MecanumDrive;

public class Trajectory extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);


        //Path myTrajectory = drive.trajectoryBuilder(new Pose2d())
          //      .strafeRight(10)
            //    .forward(5)
              //  .build();
        //waitForStart();

        if(isStopRequested()) return;
        //drive.init(myTrajectory);
    }

    public double duration() {
        return 0;
    }

    public Object end() {
        return null;
    }
}
