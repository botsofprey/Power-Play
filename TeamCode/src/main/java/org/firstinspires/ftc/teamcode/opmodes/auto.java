package org.firstinspires.ftc.teamcode.opmodes;

//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//easyopencv imports

//teamcode imports
import org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses.driveMode;
import org.firstinspires.ftc.teamcode.hedgehogPID.trajectory;
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

@Autonomous()
public class auto extends OpMode {
    //remember: System.currentTimeMillis();
    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    motorProgrammingBoard mpb;
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
            apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);

        //set up any functions or variables needed for program
            //initialize hardware
            mpb.init(hardwareMap);
            //start the camera
            autocam.createCameraInstance(hardwareMap, telemetry);
            //set the pipeline for the camera
            autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        //set up all trajectories
        //1. Parking trajectories
            //Trajectory to park in a designated area if the robot detects apriltag no. 17
            trajectory parkIf17 = new trajectory(new driveMode(1, "normal"));

            //Trajectory to park in a designated area if the robot detects apriltag no. 18
            trajectory parkIf18 = new trajectory(new driveMode(1, "normal"));

            //Trajectory to park in a designated area if the robot detects apriltag no. 19
            trajectory parkIf19 = new trajectory(new driveMode(1, "normal"));

        //2. Cone pickup trajectories
            //Trajectory to go to a cone in the auto pickup zone
            trajectory autoPickUp = new trajectory(new driveMode(1, "normal"));

            //Trajectory to go back from a given junction to the auto pickup zone
            trajectory autoPickUpReturn = new trajectory(new driveMode(1, "normal"));

            //Trajectory to go to a given junction of a specified height
            trajectory goToJunction = new trajectory(new driveMode(1, "normal"));

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