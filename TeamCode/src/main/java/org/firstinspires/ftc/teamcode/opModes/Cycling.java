package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
                .addTemporalMarker(() -> {
                    board.setClaw(0.4);
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    liftPosition = heightsList.highJunction;
                })
                .waitSeconds(0.1)
                .lineToConstantHeading(new Vector2d(-12, 60))
                .lineToSplineHeading(new Pose2d(-15, 36, 200))
                .lineToConstantHeading(new Vector2d(-14, 30))
                .splineToSplineHeading(new Pose2d(-18.5, 6, Math.toRadians(225)), Math.toRadians(90))
                .addTemporalMarker(() -> {
                    board.setClaw(0);
                })
                .waitSeconds(0.5)
                .lineToConstantHeading(new Vector2d(-12, 10))
                .addTemporalMarker(() -> {
                    liftPosition = heightsList.rightAboveACone;
                })
                .waitSeconds(0.5)
                .lineToSplineHeading(new Pose2d(-14, 30, Math.toRadians(180)))
                .lineToSplineHeading(new Pose2d(-13, 50, Math.toRadians(90)))
                .lineToConstantHeading(new Vector2d(-15, 63))
                .build();
    }

    @Override
    public void loop() {
        drive.followTrajectorySequenceAsync(cycle);
        drive.update();
        board.setLift(liftPosition);
    }
}
