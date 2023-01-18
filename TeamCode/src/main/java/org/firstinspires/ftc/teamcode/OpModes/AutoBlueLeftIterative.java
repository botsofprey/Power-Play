package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.HardwareMechanisms;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vars.CoordinateLocations;
import org.firstinspires.ftc.teamcode.vars.HeightsList;
import org.firstinspires.ftc.teamcode.vars.StaticImu;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous
public class AutoBlueLeftIterative extends OpMode {
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

    Trajectory park19, park18, park17, preLoad;

    HeightsList heights = new HeightsList();
    cameraControl autocam = new cameraControl();
    HardwareMechanisms mpb = new HardwareMechanisms();
    CoordinateLocations coordinateLocations = new CoordinateLocations();

    @Override
    public void init() {
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

        mecanumDrive.setPoseEstimate(coordinateLocations.leftBlueStart);
        preLoad = mecanumDrive.trajectoryBuilder(coordinateLocations.leftBlueStart).lineToLinearHeading(new Pose2d(coordinateLocations.leftHighJunc.getX() + 5, coordinateLocations.leftHighJunc.getY() + 5, Math.toRadians(250))).build();

        park19 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(8, 27, Math.toRadians(270))).build();
        park18 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(36, 16, Math.toRadians(270))).build();
        park17 = mecanumDrive.trajectoryBuilder(preLoad.end()).lineToLinearHeading(new Pose2d(54, 27, Math.toRadians(270))).build();
        mecanumDrive.followTrajectoryAsync(preLoad);
        //tag detection
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();

       while (true) {
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
            if (currentDetections.size() > 0) {
                break;
            }
        }
    }

    @Override
    public void loop() {
        if (step == 0) {
            mpb.setClaw(0.4);
            mpb.sleep(1000);
            step++;
        } else if (step == 1) {
            mpb.setLift(heights.highJunction);
            mecanumDrive.update();
            if (!mecanumDrive.isBusy()) {
                step++;
            }
        } else if (step == 2) {
            mpb.setClaw(0);
            if (tagOfInterest == 19) {
                mecanumDrive.followTrajectoryAsync(park19);
            } else if (tagOfInterest == 18) {
                mecanumDrive.followTrajectoryAsync(park18);
            } else if (tagOfInterest == 17) {
                mecanumDrive.followTrajectoryAsync(park17);
            }
            step++;
        } else if (step == 3) {
            mecanumDrive.update();
            mpb.setLift(0);
            if (!mecanumDrive.isBusy()) {
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
