package org.firstinspires.ftc.teamcode;
//general imports

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous()
public class AutoRedLeft extends OpMode {
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

    Trajectory right17, forward18, forward19, left19, forward17;

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

        left19 = mecanumDrive.trajectoryBuilder(new Pose2d()).strafeLeft(24).build();
        forward19 = mecanumDrive.trajectoryBuilder(left19.end()).forward(24).build();
        forward18 = mecanumDrive.trajectoryBuilder(new Pose2d()).forward(24).build();
        right17 = mecanumDrive.trajectoryBuilder(new Pose2d()).strafeRight(24).build();
        forward17 = mecanumDrive.trajectoryBuilder(right17.end()).forward(24).build();
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
            }
        }
        else {
            telemetry.addLine("No tag found.");
        }

        //end
        if (tagOfInterest == 17) {
            mecanumDrive.followTrajectory(right17);
            mecanumDrive.followTrajectory(forward17);
            mecanumDrive.update();
        }
        if (tagOfInterest == 18) {
            mecanumDrive.followTrajectory(forward18);
            mecanumDrive.update();
        }
        if (tagOfInterest == 19) {
            mecanumDrive.followTrajectory(left19);
            mecanumDrive.followTrajectory(forward19);
            mecanumDrive.update();
        }
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
        StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);
    }
}

