package org.firstinspires.ftc.teamcode;
//general imports

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous()
public class AutoRedWithSplines extends OpMode {
    //remember: System.currentTimeMillis();
    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    HardwareMechanisms mpb;
    SampleMecanumDrive mecanumDrive;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;

    //apriltag variable definitions
    //set the variable that holds the tag ID of interest
    int tagOfInterest;
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    Trajectory trajectory17, trajectory18, trajectory19;

    @Override
    public void init() {
        //allocate memory for objects
        tagData = new AprilTagDetection();
        autocam = new cameraControl();
        mpb = new HardwareMechanisms();
        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);

        //set up any functions or variables needed for program
        //initialize hardware
        mpb.init(hardwareMap);
        //start the camera
        autocam.createCameraInstance(hardwareMap, telemetry);
        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        trajectory19 = mecanumDrive.trajectoryBuilder(
                        new Pose2d())
                .splineTo(new Vector2d(0, -24), Math.toRadians(0))
                .splineTo(new Vector2d(24, -24), Math.toRadians(0))
                .build();
        trajectory18 = mecanumDrive.trajectoryBuilder(new Pose2d()).forward(24).build();
        trajectory17 = mecanumDrive.trajectoryBuilder(
                        new Pose2d())
                .splineTo(new Vector2d(0, 24), Math.toRadians(0))
                .splineTo(new Vector2d(24, 24), Math.toRadians(0))
                .build();
    }

    @Override
    public void loop() {
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();

        if (currentDetections.size() != 0) {
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 || tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    telemetry.addData("Tag data", tagData);
                    break;
                }
                if (tagOfInterest == 17) {
                    mecanumDrive.followTrajectory(trajectory17);

                }
                if (tagOfInterest == 18) {
                    mecanumDrive.followTrajectory(trajectory18);
                }
                if (tagOfInterest == 19) {
                    mecanumDrive.followTrajectory(trajectory19);
                }
            }
        }
        else {
            telemetry.addLine("No tag found.");
        }
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
//        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
    }
}


