package org.firstinspires.ftc.teamcode;
//general imports
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;
//easyopencv imports
//roadrunner imports
import com.acmerobotics.roadrunner.trajectory.Trajectory;
//teamcode imports
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.HardwareMechanisms;
import org.openftc.apriltag.AprilTagDetection;

@Autonomous()
public class Auto extends OpMode {
    //remember: System.currentTimeMillis();
    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    HardwareMechanisms mpb;
    SampleMecanumDrive mecanumDrive;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;

    Trajectory parkIf17,
               parkIf18,
               parkIf19,
               getCone,
               toGroundJunction,
               toLowJunction,
               toMedJunction,
               toHighJunction;

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

    //road runner localization variables
    Pose2d currentPosition = new Pose2d();
    double currentHeading;

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
        //set up driving speeds
        autoDriveBase.normalMode = 2;
        autoDriveBase.setFastMode();
        autoDriveBase.setSlowMode();
        autoDriveBase.setDriveMode("normal");
        //start the camera
        autocam.createCameraInstance(hardwareMap, telemetry);
        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        //Initialize and declare all trajectories
        //1. Parking trajectories
            //Park if apriltag id 17 is seen
                parkIf17 = mecanumDrive.trajectoryBuilder(new Pose2d())
                .build();
            //Park if apriltag id 18 is seen
                parkIf18 = mecanumDrive.trajectoryBuilder(new Pose2d())
                .build();
            //Park if apriltag id 19 is seen
                parkIf19 = mecanumDrive.trajectoryBuilder(new Pose2d())
                .build();
        //2. Cone scoring and retrieval
            //Go from current position to auto cone stack
                getCone = mecanumDrive.trajectoryBuilder(currentPosition, currentHeading)
                    .
                    .build();
            //Go from current position to ground junction
                toGroundJunction = mecanumDrive.trajectoryBuilder(currentPosition, currentHeading)
                    .build();
            //Go from current position to low junction
                toLowJunction = mecanumDrive.trajectoryBuilder(currentPosition, currentHeading)
                    .build();
            //Go from current position to medium junction
                toMedJunction = mecanumDrive.trajectoryBuilder(currentPosition, currentHeading)
                    .build();
            //Go from current position to high junction
                toHighJunction = mecanumDrive.trajectoryBuilder(, currentHeading)
                    .build();

    }

    @Override
    public void loop() {
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();

        if(currentDetections.size() != 0) {
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 || tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    telemetry.addData("Tag data", tag.toString());
                    break;
                }
                else {
                    telemetry.addLine("No tag found.");
                }
                if (tagOfInterest == 17) {
                    mecanumDrive.followTrajectory(parkIf17);
                    updateCurrentHeading();

                }
                if (tagOfInterest == 18) {
                    mecanumDrive.followTrajectory(parkIf18);
                }
                if (tagOfInterest == 19) {
                    mecanumDrive.followTrajectory(parkIf19);
                }
            }
        }
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
    }

    public void updateCurrentPosition() {
        this.currentPosition = mecanumDrive.getPoseEstimate();
    }

    public void updateCurrentHeading() {
        this.currentHeading = mecanumDrive.getRawExternalHeading();
    }
}

