package org.firstinspires.ftc.teamcode.opmodes;
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
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.mecanumDrive.mecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;
import org.openftc.apriltag.AprilTagDetection;

import static org.firstinspires.ftc.teamcode.mecanumDrive.driveVariables.VELOCITY_CONSTRAINT;
import static org.firstinspires.ftc.teamcode.mecanumDrive.driveVariables.ACCELERATION_CONSTRAINT;

@Autonomous()
public class auto extends OpMode {
    //remember: System.currentTimeMillis();
    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    motorProgrammingBoard mpb;
    org.firstinspires.ftc.teamcode.mecanumDrive.mecanumDrive autoDriveBase;
    org.firstinspires.ftc.teamcode.mecanumDrive.mecanumDrive mecanumDrive;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;

    //create array to hold the returned variables from robot
    //movement functions
    double[] moveRobotReturn = new double[4];

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

    @Override
    public void init() {
        //allocate memory for objects
        tagData = new AprilTagDetection();
        autocam = new cameraControl();
        mpb = new motorProgrammingBoard();
        autoDriveBase = new mecanumDrive();
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

        //set up all trajectories
        //1. Parking trajectories
        //Trajectory to park in a designated area if the robot detects apriltag no. 17
        Trajectory parkIf17 = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //Trajectory to park in a designated area if the robot detects apriltag no. 18
        Trajectory parkIf18 = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //Trajectory to park in a designated area if the robot detects apriltag no. 19
        Trajectory parkIf19 = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //2. Cone pickup trajectories
        //Trajectory to go to a cone in the auto pickup zone
        Trajectory autoPickUp = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //Trajectory to go back from a given junction to the auto pickup zone
        Trajectory autoPickUpReturn = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //Trajectory to go to a given junction of a specified height
        Trajectory goToJunction = new TrajectoryBuilder(new Pose2d(), VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT).build();

        //
    }

    @Override
    public void loop() {
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();

        if(currentDetections.size() != 0) {
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 || tag.id <= 19) {
                    tagOfInterest = tag.id;
                    tagData = tag;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    telemetry.addData("Tag data", tagData);
                    break;
                }
                else {
                    telemetry.addLine("No tag found.");
                }
                if (tagOfInterest == 17) {

                }
                if (tagOfInterest == 18) {

                }
                if (tagOfInterest == 19) {

                }
            }
        }
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
    }
}

