package org.firstinspires.ftc.teamcode.opmodes;

//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.ArrayList;
//easyopencv imports
//apriltag imports
//teamcode imports
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.openftc.apriltag.AprilTagDetection;

@Autonomous()
public class autoTEMP extends OpMode {
    cameraControl autocam;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;
    //set the variable that holds the tag ID of interest
    int tagOfInterest;
    //show extra tag data
    AprilTagDetection tagData;
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    boolean tagFound = false;

    @Override
    public void init() {
        //allocate object memory
        apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);
        autocam = new cameraControl();

        //call createCameraInstance
        autocam.createCameraInstance(hardwareMap, telemetry);

        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);
    }

    @Override
    public void loop() {
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();

        if(currentDetections.size() != 0) {
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 || tag.id <= 19) {
                    tagFound = true;
                    tagOfInterest = tag.id;
                    tagData = tag;
                    break;
                }
            }
        }
        if(tagFound) {
            telemetry.addData("Tag of interest", tagOfInterest);
            telemetry.addData("Tag data", tagData);
        }
        else
            telemetry.addLine("No tag found.");
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
    }
}
