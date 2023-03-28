package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
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

public class AutoRightPARKONLY extends OpMode {
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

    TrajectorySequence preLoad, getConeAndScore, getConeAndScore2, park19, park18, park17;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();
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

        mecanumDrive.setPoseEstimate(coordinateLocations.rightStart);
        park19 = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.rightStart)
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX(), coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, 11.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-60, 11.5, Math.toRadians(270)))
                .build();
        park18 = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.rightStart)
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX(), coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, 11.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-36, 11.5, Math.toRadians(270)))
                .build();
        park17 = mecanumDrive.trajectorySequenceBuilder(coordinateLocations.rightStart)
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX(), coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, coordinateLocations.rightStart.getX() - 1, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(coordinateLocations.rightStart.getX() - 12, 11.5, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-12, 11.5, Math.toRadians(270)))
                .build();
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
            if (tagOfInterest == 17) {
                mecanumDrive.followTrajectorySequence(park17);
            } else if (tagOfInterest == 19) {
                mecanumDrive.followTrajectorySequence(park19);
            } else {
                mecanumDrive.followTrajectorySequence(park18);
            }
            step++;
        } else if (step == 1) {
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
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

