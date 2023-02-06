package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

@Autonomous
public class newAutoRight extends LinearOpMode {
    /*
    Type                                       Name                                 Value
     */
    HardwareMap hardwareMap;
    HeightsList heightsList = new HeightsList();
    final int HIGH_JUNCTION_HEIGHT = heightsList.highJunction;
    Pose2d prevtraj = new Pose2d(
            36 + 6, 12 - 6,
            Math.toRadians(315)
    );
    cameraControl autocam = new cameraControl();
    AprilTagDetection tagData = new AprilTagDetection();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coords = new CoordinateLocations();
    final double tagsize = 0.166,

    fx = 578.272,
            fy = 578.272,
            cx = 402.145,
            cy = 221.506,
            START_Y_COORD = coords.rightStart.getY(),
            CATCH = 0.4,
            RELEASE = 0.0,
            HIGH_JUNCTION_X = coords.rightHighJunc.getX(),
            HIGH_JUNCTION_Y = coords.rightHighJunc.getY();
    AprilTagPipelineEXAMPLECOPY aprilTagPipeline = new AprilTagPipelineEXAMPLECOPY(
            tagsize, fx, fy, cx, cy);
    final Vector2d RIGHT_START_VEC = coords.rightStart.vec();
    SampleMecanumDrive mecanumDrive;
    double liftHeight = 0;
    int tagOfInterest = 0,
            i = 0;
    int[] coneheight = heightsList.heights;
    TrajectorySequence preload, getConeAndScore, park17, park18, park19;

    @Override
    public void runOpMode() throws InterruptedException {

        mecanumDrive = new SampleMecanumDrive(hardwareMap);

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
                if (tag.id >= 17 && tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    break;
                } else
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

    /*


    END OPMODE


     */
    private double toDEG(double angle) {
        return Math.toRadians(angle);
    }

    private TrajectorySequence preloadSETUP() {
        return mecanumDrive.trajectorySequenceBuilder(coords.rightStart)
//                .addSpatialMarker(RIGHT_START_VEC, () -> {
//                    mpb.setClaw(CATCH);
//                    mpb.setLift(HIGH_JUNCTION_HEIGHT);
//                })
                .addTemporalMarker(() -> {
                    mpb.setClaw(CATCH);
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    mpb.setLift(HIGH_JUNCTION_HEIGHT);
                })
                .lineToLinearHeading(new Pose2d(-38, START_Y_COORD))
                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                .waitSeconds(0.75)
                .turnDEG(-45)
                .lineToLinearHeading(new Pose2d(HIGH_JUNCTION_X - 6,
                        HIGH_JUNCTION_Y + 6,
                        Math.toRadians(270)))
                .addTemporalMarker(() -> mpb.setClaw(RELEASE))
                .build();
    }

    private TrajectorySequence getConeAndScoreSETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
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
                .build();
    }

    private TrajectorySequence park17SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                .lineToLinearHeading(new Pose2d(-12, 12, toDEG(270)))
                .build();
    }

    private TrajectorySequence park18SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                .build();
    }

    private TrajectorySequence park19SETUP() {
        return mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(-36, 12, toDEG(270)))
                .lineToLinearHeading(new Pose2d(-60, 12, toDEG(270)))
                .build();
    }
}