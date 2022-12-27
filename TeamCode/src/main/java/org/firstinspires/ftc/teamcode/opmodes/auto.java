package org.firstinspires.ftc.teamcode.opmodes;
//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
//easyopencv imports
//teamcode imports
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;
import org.openftc.apriltag.AprilTagDetection;

@Autonomous()
public class auto extends OpMode {
    //remember: System.currentTimeMillis();
    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    motorProgrammingBoard mpb;
    driveBaseMovement autoDriveBase;
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
            autoDriveBase = new driveBaseMovement();
            apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);

        //set up any functions or variables needed for program
            //initialize
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
    }

    @Override
    public void loop() {
        double[] moveReturn = autoDriveBase.moveRobotAUTO(180,0,0);
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
