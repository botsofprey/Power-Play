package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
public class AutoRight extends OpMode {
    SampleMecanumDrive mecanumDrive;
    AprilTagDetection tagData;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;
    int tagOfInterest;
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    int i = 0;

    int step = 0;

    Trajectory park19, park18, park17;
    TrajectorySequence preLoad, getConeAndScore;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();
    Pose2d prevtraj = new Pose2d(-21.5, 9.5, Math.toRadians(270));
    int coneheight = heights.heights[0];
    int liftHeight = heights.highJunction;
    double Before, After;

    @Override
    public void init() {
        StaticImu.imuStatic = 0;
        apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);
        tagData = new AprilTagDetection();
        //set up any functions or variables needed for program
        //initialize hardware
        mpb.init(hardwareMap);
        mpb.resetLift();
        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        //start the camera
        autocam.createCameraInstance(hardwareMap, telemetry);
        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        mecanumDrive.setPoseEstimate(coordinateLocations.rightStart);

        preLoad = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.rightStart)
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX(), coordinateLocations.rightStart.getY() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-12, coordinateLocations.rightStart.getY() - 1, Math.toRadians(270)))
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(-12, 15, Math.toRadians(270)))
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(-21.5, 15, Math.toRadians(270)))
                .addTemporalMarker(() -> {
                    liftHeight = heights.highJunction + 300;
                })
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(-21.5, 9.5, Math.toRadians(270)))
                .waitSeconds(1)
                .build();
        getConeAndScore = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(-21.5, 12.5, Math.toRadians(270)))
                .addTemporalMarker(() -> { Before = mpb.getNormalizedDegrees();})
                .turnDEG(-90)
                .addTemporalMarker(() -> { After = mpb.getNormalizedDegrees();})
                .waitSeconds(1)
                .addTemporalMarker(() -> {
                    liftHeight = (int) (heights.heights[i] * 1.5);
                })
                .waitSeconds(1)
                .lineToLinearHeading(new Pose2d(-58.5, 12.5, Math.toRadians(180)))
                .build();
        park19 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-60, 12, Math.toRadians(270))).build();
        park18 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270))).build();
        park17 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-12, 12, Math.toRadians(270))).build();
        mecanumDrive.followTrajectorySequenceAsync(preLoad);
    }

    @Override
    public void init_loop() {
        //tag detection
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections;
        currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();
        for (AprilTagDetection tag : currentDetections) {
            if (tag.id >= 17 && tag.id <= 19) {
                tagOfInterest = tag.id;
                telemetry.addData("Tag of interest", tagOfInterest);
                telemetry.addData("Tag data", tag.toString());
                break;
            } else {
                telemetry.addLine("No tag found.");
            }
        }
        if (currentDetections.size() == 0) {
            telemetry.addLine("no detections found");
            telemetry.update();
        }
    }

    @Override
    public void loop() {
        if (step == 0) {
            mpb.setClaw(0.4);
            mpb.sleep(1000);
            liftHeight = coneheight;
            step++;
        } else if (step == 1) {
            mpb.setLift(liftHeight);
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
                prevtraj = mecanumDrive.getPoseEstimate();
                step++;
            }
        } else if (step == 2) {
            mpb.sleep(1500);
            mpb.setClaw(0);
            mpb.sleep(500);
            step++;
            mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);
        } else if (step == 3) {
            prevtraj = getConeAndScore.end();
            coneheight = heights.heights[i];
            mecanumDrive.update();
            mpb.setLift(liftHeight);
            if (!mecanumDrive.isBusy()) {
                i++;
                if (i == 1) {
                    step++;
                } else {
                    mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);
                }
            }
//        } else if (step == 4) {
//            if (tagOfInterest == 19) {
//                mecanumDrive.followTrajectoryAsync(park19);
//            } else if (tagOfInterest == 17) {
//                mecanumDrive.followTrajectoryAsync(park17);
//            } else {
//                mecanumDrive.followTrajectoryAsync(park18);
//            }
//            step++;
//        } else if (step == 5) {
//            mecanumDrive.update();
//            if (!mecanumDrive.isBusy()) {
//                step++;
//            }
//        } else if (step == 6) {
//            mpb.setLift(0);
//            if (mpb.getLift() == 0) {
//                step++;
//            }
        } else {
            telemetry.addLine("Done");
        }
        mecanumDrive.updatePoseEstimate();
        mecanumDrive.setPoseEstimate(new Pose2d(
                mecanumDrive.getPoseEstimate().getX(),
                mecanumDrive.getPoseEstimate().getY(),
                Math.toRadians(270 + mpb.getNormalizedDegrees())
        ));
        telemetry.addData("current position", mecanumDrive.getPoseEstimate());
        telemetry.addData("imu value", mpb.getNormalizedDegrees());
        telemetry.addData("Before turn", Before);
        telemetry.addData("After turn", After);
    }

    public void stop() {
        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
        autocam.destroyCameraInstance();
    }
}
