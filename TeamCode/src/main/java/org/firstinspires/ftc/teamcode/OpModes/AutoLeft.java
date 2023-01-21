package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
public class AutoLeft extends OpMode {
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

    int step = 0;

    Trajectory park19, park18, park17;
    TrajectorySequence preLoad, getConeAndScore;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();
    Pose2d prevtraj = new Pose2d(coordinateLocations.rightHighJunc.getX() - 5, coordinateLocations.rightHighJunc.getY() + 3, Math.toRadians(315));
    int coneheight = heights.heights[0];
    int i = 0;

    @Override
    public void init() {
        StaticImu.imuStatic = 0;
        apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);
        tagData = new AprilTagDetection();
        //set up any functions or variables needed for program
        //initialize hardware
        mpb.init(hardwareMap);
        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        //start the camera
        autocam.createCameraInstance(hardwareMap, telemetry);
        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        mecanumDrive.setPoseEstimate(coordinateLocations.leftStart);

        preLoad = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.leftStart)
                .lineToLinearHeading(new Pose2d(33, coordinateLocations.leftStart.getY(), Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(32, 12, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.leftHighJunc.getX() + 5, coordinateLocations.leftHighJunc.getY() + 5, Math.toRadians(225)))
                .build();
        getConeAndScore = mecanumDrive.trajectorySequenceBuilder(prevtraj).lineTo(new Vector2d(coordinateLocations.leftHighJunc.getX() + 12, coordinateLocations.leftHighJunc.getY() + 12)).addTemporalMarker(() -> {
                    mpb.setLift(300);
                }).turn(Math.toRadians(135)).lineTo(new Vector2d(62, 12)).addTemporalMarker(() -> {
                    mpb.setLift(coneheight);
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    mpb.setClaw(0.4);
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    mpb.setLift(500);
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    mpb.setLift(heights.highJunction);
                }).lineTo(new Vector2d(coordinateLocations.leftHighJunc.getX() + 12, coordinateLocations.leftHighJunc.getY() + 12)).turn(Math.toRadians(135)).lineToLinearHeading(new Pose2d(coordinateLocations.leftHighJunc.getX() + 5, coordinateLocations.leftHighJunc.getY() + 3, Math.toRadians(210))).addTemporalMarker(() -> {
                    mpb.setClaw(0);
                })
                .waitSeconds(0.5)
                .build();
        park19 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(60, 12, Math.toRadians(270))).build();
        park18 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(36, 12, Math.toRadians(270))).build();
        park17 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(12, 12, preLoad.end().getHeading())).build();
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
            mpb.sleep(1500);
            step++;
        } else if (step == 1) {
            mpb.setLift(heights.highJunction);
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
                step++;
            } else if (step == 2) {
                mpb.setClaw(0);
                mpb.sleep(1500);
                step++;
                mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);
            } else if (step == 3) {
                prevtraj = getConeAndScore.end();
                coneheight = heights.heights[i];
                mecanumDrive.update();
                if (!mecanumDrive.isBusy()) {
                    step++;
                    i++;
                    mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);
                }

            } else if (step == 4) {
                mpb.setClaw(0);
                if (tagOfInterest == 19) {
                    mecanumDrive.followTrajectoryAsync(park19);
                } else if (tagOfInterest == 17) {
                    mecanumDrive.followTrajectoryAsync(park17);
                } else {
                    mecanumDrive.followTrajectoryAsync(park18);
                }
                mpb.sleep(1500);
                step++;
            } else if (step == 5) {
                mecanumDrive.update();
                if (!mecanumDrive.isBusy()) {
                    step++;
                }
            } else if (step == 6) {
                mpb.setLift(0);
                if (mpb.getLift() == 0) {
                    step++;
                }
            } else {
                telemetry.addLine("Done");
            }

        }
    }

    public void stop() {
        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
        autocam.destroyCameraInstance();
    }
}
