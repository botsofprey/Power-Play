package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.vars.CoordinateLocations;
import org.firstinspires.ftc.teamcode.vars.HeightsList;
import org.firstinspires.ftc.teamcode.vars.StaticImu;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous
public class newAutoRight extends LinearOpMode {
    /*
    Type                                       Name                                 Value
     */
    HeightsList                         heightsList =                   new HeightsList();
    Pose2d                                 prevtraj = new                         Pose2d(
                                                                   36 + 6, 12 - 6,
                                                                     Math.toRadians(315)
                                                                                        );

    cameraControl                           autocam = new                 cameraControl();
    AprilTagDetection                       tagData = new             AprilTagDetection();
    HardwareMechanisms                          mpb = new            HardwareMechanisms();
    SampleMecanumDrive                 mecanumDrive = new SampleMecanumDrive(hardwareMap);
    CoordinateLocations                      coords = new           CoordinateLocations();

    double                               liftHeight =                                   0;

    int                               tagOfInterest =                                   0,
                                                  i =                                   0;

    int[]                                coneheight =               heightsList.heights;

    final double                            tagsize =                               0.166,

                                                 fx =                             578.272,
                                                 fy =                             578.272,
                                                 cx =                             402.145,
                                                 cy =                             221.506,
                                      START_Y_COORD =            coords.rightStart.getY(),
                                              CATCH =                                 0.4,
                                            RELEASE =                                 0.0,
                                    HIGH_JUNCTION_X =         coords.rightHighJunc.getX(),
                                    HIGH_JUNCTION_Y =         coords.rightHighJunc.getY();

    final Vector2d                  RIGHT_START_VEC =             coords.rightStart.vec();

    final int                  HIGH_JUNCTION_HEIGHT =            heightsList.highJunction;

    AprilTagPipelineEXAMPLECOPY    aprilTagPipeline = new     AprilTagPipelineEXAMPLECOPY(
                                                                 tagsize, fx, fy, cx, cy);


    TrajectorySequence preload = mecanumDrive.trajectorySequenceBuilder(coords.rightStart)
                                .addSpatialMarker(RIGHT_START_VEC, () -> {
                                    mpb.setLift(HIGH_JUNCTION_HEIGHT);
                                    mpb.setClaw(CATCH);
                                })

                                .lineToLinearHeading(new Pose2d(-38, START_Y_COORD))
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                                .waitSeconds(0.75)
                                .turnDEG(-45)
                                .lineToLinearHeading(new Pose2d(HIGH_JUNCTION_X - 6,
                                                                HIGH_JUNCTION_Y + 6,
                                                                Math.toRadians(270)))
                                .addTemporalMarker(() -> {
                                    mpb.setClaw(RELEASE);
                                })
                                .build(),

               getConeAndScore = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                                .addSpatialMarker(new Vector2d(-36, 12), () -> {
                                    mpb.setLift((int) (coneheight[i] * 1.5));
                                })
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(315)))
                                .addTemporalMarker(() -> {
                                })
                                .turnDEG(-135)
                                .lineToLinearHeading(new Pose2d(-64, 12, toDEG(180)))
                                .addTemporalMarker(() -> {
                                    mpb.setLift(coneheight[i]);
                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {
                                    mpb.setClaw(CATCH);
                                })
                                .addTemporalMarker(() -> {
                                    mpb.setLift((int) (coneheight[i] * 1.5));
                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(getRuntime(), () -> {
                                    mpb.setLift(HIGH_JUNCTION_HEIGHT);
                                })
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(180)))
                                .turnDEG(135)
                                .lineToLinearHeading(new Pose2d(HIGH_JUNCTION_X - 6,
                                                                HIGH_JUNCTION_Y + 6,
                                                                toDEG(225)))
                                .waitSeconds(0.75)
                                .addTemporalMarker(() -> {
                                    mpb.setClaw(0);
                                })
                                .waitSeconds(0.5)
                                .build(),

                        park19 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                                .lineToLinearHeading(new Pose2d(-60, 12, toDEG(270)))
                                .build(),

                        park18 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                                .build(),

                        park17 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                                .lineToLinearHeading(new Pose2d(-12, 12, toDEG(270)))
                                .build();

    @Override
    public void runOpMode() throws InterruptedException {
        //set trajectory to run on start
        mecanumDrive.followTrajectorySequenceAsync(preload);

        //init loop
        while (!isStopRequested() && !isStarted()) {

            //check detected apriltags
            tagData = null;
            ArrayList<AprilTagDetection> currentDetections;
            currentDetections = aprilTagPipeline.getLatestDetections();

            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 && tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    break;
                }
                else
                    telemetry.addLine("No tag found");
            }
            telemetry.update();
        }

        if (isStarted()) {
            //preload
            do {
                mecanumDrive.update();
            }
            while (!isStopRequested() && mecanumDrive.isBusy());

            //getConeAndScore
            do {
                mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);

                do {
                    mecanumDrive.update();
                }
                while (!isStopRequested() && mecanumDrive.isBusy());
            }
            while (!isStopRequested() && ++i > 2);

            //park
            if (tagOfInterest == 17)
                mecanumDrive.followTrajectorySequenceAsync(park17);
            else if (tagOfInterest == 18)
                mecanumDrive.followTrajectorySequenceAsync(park18);
            else if (tagOfInterest == 19)
                mecanumDrive.followTrajectorySequenceAsync(park19);

            do {
                mecanumDrive.update();
            }
            while (!isStopRequested() && mecanumDrive.isBusy());
        }

        if (isStopRequested()) {
            StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
            autocam.destroyCameraInstance();
        }
    }
    private double toDEG(double angle) {
        return Math.toRadians(angle);
    }
}
