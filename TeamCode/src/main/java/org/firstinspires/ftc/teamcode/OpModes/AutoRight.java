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

    int coneheight = 0;
    int i = 0;

    int step = 0;

    Trajectory park19, park18, park17, preLoad, preLoadSpline;
    TrajectorySequence getConeAndScoreInitial, getConeAndScore;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();

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
        preLoad = mecanumDrive.trajectoryBuilder(coordinateLocations.leftStart).lineToLinearHeading(new Pose2d(coordinateLocations.leftHighJunc.getX() - 5, coordinateLocations.leftHighJunc.getY() + 5, Math.toRadians(-225))).build();
        getConeAndScoreInitial = mecanumDrive.trajectorySequenceBuilder(preLoad.end())
                .lineTo(new Vector2d(coordinateLocations.rightHighJunc.getX() - 12, coordinateLocations.rightHighJunc.getY() + 12))
                .turn(-135)
                .lineTo(new Vector2d(-62,12))
                .build();
        park19 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-8, 27, Math.toRadians(270))).build();
        park18 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-36, 16, Math.toRadians(270))).build();
        park17 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(-54, 27, Math.toRadians(270))).build();
        mecanumDrive.followTrajectoryAsync(preLoad);
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
            }
        } else if (step == 2) {
            mpb.setClaw(0);
            mpb.sleep(1500);
            step++;
        } else if (step == 3) {
            mecanumDrive.followTrajectorySequenceAsync(getConeAndScoreInitial);
            coneheight = heights.heights[i++];
            while (mecanumDrive.isBusy())
                mecanumDrive.update();
            step++;
        } else if (step == 4) {
            mecanumDrive.followTrajectorySequenceAsync(getConeAndScore);
            coneheight = heights.heights[i++];
            while (mecanumDrive.isBusy())
                mecanumDrive.update();
            if (coneheight == heights.heights[4])
                step++;
        } else if (step == 5) {
            if (tagOfInterest == 19) {
                mecanumDrive.followTrajectoryAsync(park19);
            } else if (tagOfInterest == 18) {
                mecanumDrive.followTrajectoryAsync(park18);
            } else if (tagOfInterest == 17) {
                mecanumDrive.followTrajectoryAsync(park17);
            }
            step++;
        } else if (step == 6) {
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
                step++;
            }
        } else if (step == 7) {
            mpb.setLift(0);
            if (mpb.getLift() == 0) {
                step++;
            }
        } else {
        telemetry.addLine("Done");
    }

}

    public void stop() {
        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
        autocam.destroyCameraInstance();
    }
}
