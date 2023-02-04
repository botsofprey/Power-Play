package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.vars.HeightsList;

@TeleOp
public class Cycling extends OpMode {
    int liftPosition;
    TrajectorySequence cycle;
    HardwareMechanisms board = new HardwareMechanisms();
    SampleMecanumDrive drive;
    HeightsList heightsList = new HeightsList();

    @Override
    public void init() {
        board.init(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-15, 63, Math.toRadians(90)));
        cycle = drive.trajectorySequenceBuilder(new Pose2d(-15, 63, Math.toRadians(90)))
                .addTemporalMarker(()->{
                    liftPosition = heightsList.highJunction;
                })
                .waitSeconds(0.5)
                .lineToLinearHeading(new Pose2d(-15, 12, Math.toRadians(90)))
                .turn(Math.toRadians(-45))
                .lineToLinearHeading(new Pose2d(-5, 18, Math.toRadians(135)))
//                .addTemporalMarker(()->{})
//                .waitSeconds(0.5)
                .build();
        drive.followTrajectorySequenceAsync(cycle);
    }

    @Override
    public void loop() {
        drive.update();
        board.setLift(liftPosition);

    }
}
