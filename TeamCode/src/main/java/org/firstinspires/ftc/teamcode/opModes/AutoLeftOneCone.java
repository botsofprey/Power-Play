package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
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
public class AutoLeftOneCone extends OpMode {
    SampleMecanumDrive mecanumDrive;
    AprilTagDetection tagData;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;
    int tagOfInterest;
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272, fy = 578.272, cx = 402.145, cy = 221.506;
    int step = 0;

    TrajectorySequence preLoad, park19, park18, park17;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();
    Pose2d prevtraj = new Pose2d(26, 7.25, Math.toRadians(270));
    int coneheight = heights.heights[0];
    int liftHeight = heights.highJunction;

    private void convertToImuHeading() {
        mecanumDrive.updatePoseEstimate();
        mecanumDrive.setPoseEstimate(new Pose2d(
                mecanumDrive.getPoseEstimate().getX(),
                mecanumDrive.getPoseEstimate().getY(),
                Math.toRadians(270 + mpb.getNormalizedDegrees())
        ));
    }

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

        mecanumDrive.setPoseEstimate(coordinateLocations.leftStart);

        preLoad = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.leftStart)
                .lineToLinearHeading(new Pose2d(coordinateLocations.leftStart.getX(), coordinateLocations.leftStart.getY() - 5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(12, coordinateLocations.leftStart.getY() - 5, Math.toRadians(270)))
                .addTemporalMarker(this::convertToImuHeading)
                .lineToLinearHeading(new Pose2d(12, 11.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(24.5, 11.5, Math.toRadians(270)))
                .addTemporalMarker(() -> {
                    liftHeight = heights.highJunction + 300;
                    convertToImuHeading();
                })
                .waitSeconds(0.25)
                .lineToLinearHeading(new Pose2d(26, 7.5, Math.toRadians(270)))
                .waitSeconds(0.1)
                .addTemporalMarker(this::convertToImuHeading)
                .build();
        park19 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(prevtraj.getX(), 14.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(12, 13, Math.toRadians(270)))
                .build();
        park18 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(prevtraj.getX(), 14.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(36, 13, Math.toRadians(270)))
                .build();
        park17 = mecanumDrive.trajectorySequenceBuilder(prevtraj)
                .lineToLinearHeading(new Pose2d(prevtraj.getX(), 14.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(60, 13, Math.toRadians(270)))
                .build();
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
            mpb.sleep(500);
            mpb.setClaw(0);
            mpb.sleep(500);
            step++;
            convertToImuHeading();
        } else if (step == 3) {
            if (tagOfInterest == 19) {
                mecanumDrive.followTrajectorySequenceAsync(park19);
            } else if (tagOfInterest == 17) {
                mecanumDrive.followTrajectorySequenceAsync(park17);
            } else {
                mecanumDrive.followTrajectorySequenceAsync(park18);
            }
            step++;
        } else if (step == 4) {
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
                step++;
            }
        } else if (step == 5) {
            mpb.setLift(0);
            if (mpb.getLift() == 0) {
                step++;
            }
        } else {
            telemetry.addLine("Done");
        }
        telemetry.addData("current position", mecanumDrive.getPoseEstimate());
        telemetry.addData("imu value", mpb.getNormalizedDegrees() + 270);
    }

    public void stop() {
        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
        autocam.destroyCameraInstance();
    }
}