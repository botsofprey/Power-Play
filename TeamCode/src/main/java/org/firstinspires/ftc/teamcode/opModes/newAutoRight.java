package org.firstinspires.ftc.teamcode.opModes;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_ANG_VEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants.TRACK_WIDTH;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
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

import org.firstinspires.ftc.teamcode.vars.CoordinateLocations;
import org.firstinspires.ftc.teamcode.vars.HeightsList;
import org.firstinspires.ftc.teamcode.vars.StaticImu;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class newAutoRight extends LinearOpMode {
    /*
    Type                                       Name                                 Value
     */
    HeightsList                         heightsList = new                   HeightsList();
    Pose2d                                 prevtraj = new                         Pose2d(
                                                                   -36 + 6, 12 - 6,
                                                                     Math.toRadians(315)
                                                                                        );

    cameraControl                           autocam = new                 cameraControl();
    HardwareMechanisms                          mpb = new            HardwareMechanisms();
    CoordinateLocations                      coords = new           CoordinateLocations();
    SampleMecanumDrive                 mecanumDrive;
    AprilTagDetection                       tagData;

    double                               liftHeight =                                   0;

    int                               tagOfInterest =                                   0,
                                                  i =                                   0;

    int[]                                coneheight =                 heightsList.heights;

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

    TrajectorySequence preload, getConeAndScore, park17, park18, park19;

    @Override
    public void runOpMode() throws InterruptedException {
        StaticImu.imuStatic = 0;

        mpb.init(hardwareMap);
        mpb.resetLiftAUTO();

        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        mecanumDrive.setPoseEstimate(coords.rightStart);

        aprilTagPipeline = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);
        autocam.createCameraInstance(hardwareMap, telemetry);
        autocam.camera.setPipeline(aprilTagPipeline);
        tagData = new AprilTagDetection();

        preload = preloadSETUP();

        getConeAndScore = getConeAndScoreSETUP();

        park19 = park19SETUP();

        park18 = park18SETUP();

        park17 = park17SETUP();

        //set trajectory to run on start
        mecanumDrive.followTrajectorySequenceAsync(preload);

        //init loop
        while (!isStopRequested() && !isStarted()) {
            //check detected apriltags
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

                i++;
            }
            while (!isStopRequested() && i > 3);

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
    /*


    END OPMODE


     */
    private double toRAD(double angle) {
        return Math.toRadians(angle);
    }

    private TrajectorySequence preloadSETUP() {
        return mecanumDrive.trajectorySequenceBuilder(coords.rightStart)
                .lineToLinearHeading(new Pose2d(-36, 64.5, toRAD(270)))
                .lineToSplineHeading(new Pose2d(-36, 30, toRAD(270)))
                .splineToSplineHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270 + 45))
                .waitSeconds(0.25)
                .waitSeconds(0.25)
                .build();
    }

    private TrajectorySequence getConeAndScoreSETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .back(4)
                .splineToLinearHeading(new Pose2d(-36, 12, toRAD(180)), toRAD(180))
                .lineToSplineHeading(new Pose2d(-64, 12, toRAD(180)))
                .waitSeconds(0.25)
                .waitSeconds(0.25)
                .lineToSplineHeading(new Pose2d(-36, 12, toRAD(180)))
                .splineToLinearHeading(new Pose2d(-24 - 9, 9, toRAD(270 + 45)), toRAD(270+ 45))
                .splineToLinearHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270+ 45))
                .waitSeconds(0.25)
                .waitSeconds(0.25)
                .build();
    }

    private TrajectorySequence park17SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
            .lineToLinearHeading(new Pose2d(-36, 12, toRAD(270 + 45)))
            .turn(toRAD(-45))
            .lineToLinearHeading(new Pose2d(-12,12, toRAD(270)))
            .build();
    }

    private TrajectorySequence park18SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
            .lineToLinearHeading(new Pose2d(-36, 12, toRAD(270)))
            .build();
    }

    private TrajectorySequence park19SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
            .lineToLinearHeading(new Pose2d(-36, 12, toRAD(270)))
            .lineToLinearHeading(new Pose2d(-60, 12, toRAD(270)))
            .build();
    }
}