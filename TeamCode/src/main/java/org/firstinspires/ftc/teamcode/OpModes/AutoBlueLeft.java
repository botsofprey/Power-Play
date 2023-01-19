package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
public class AutoBlueLeft extends LinearOpMode {

    //prototype objects to be created
    cameraControl autocam;
    AprilTagDetection tagData;
    HardwareMechanisms mpb;
    CoordinateLocations locations;
    SampleMecanumDrive mecanumDrive;
    AprilTagPipelineEXAMPLECOPY apriltagpipelineEXAMPLE;

    //set the variable that holds the tag ID of interest
    int tagOfInterest;
    boolean foundTag;
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    TrajectorySequence preLoad1;

    @Override
    public void runOpMode() {
        //allocate memory for objects
        tagData = new AprilTagDetection();
        autocam = new cameraControl();
        mpb = new HardwareMechanisms();
        locations = new CoordinateLocations();
        mecanumDrive = new SampleMecanumDrive(hardwareMap);
        HeightsList HL = new HeightsList();
        apriltagpipelineEXAMPLE = new AprilTagPipelineEXAMPLECOPY(tagsize, fx, fy, cx, cy);

        //set up any functions or variables needed for program
        //initialize hardware
        mpb.init(hardwareMap);
        mpb.lift.setTargetPosition(0);
        //start the camera
        autocam.createCameraInstance(hardwareMap, telemetry);
        //set the pipeline for the camera
        autocam.camera.setPipeline(apriltagpipelineEXAMPLE);

        mecanumDrive.setPoseEstimate(locations.leftStart);

        preLoad1 = mecanumDrive.trajectorySequenceBuilder(locations.leftStart)
                .addDisplacementMarker(5, () -> {
                    mpb.lift.setPower(1);
                })
                .lineTo(new Vector2d(locations.leftHighJunc.getX() + 12,
                        locations.leftHighJunc.getY() + 12))
                .turn(Math.toRadians(-45))
                .lineTo(new Vector2d(locations.leftHighJunc.getX() + 5,
                        locations.leftHighJunc.getY() + 5))
                .build();

        //tag detection
        tagData = null;
        mecanumDrive.getPoseEstimate();

        ArrayList<AprilTagDetection> currentDetections;

        do {
            currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 && tag.id <= 19) {
                    foundTag = true;
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    telemetry.addData("Tag data", tag.toString());
                    break;
                } else
                    telemetry.addLine("No tag found.");
            }
                telemetry.update();
        } while (!isStarted());
        telemetry.update();

        preLoad1 = mecanumDrive.trajectorySequenceBuilder(locations.leftStart)
                .addTemporalMarker(0, () -> {
                    mpb.lift.setPower(1);
                })
                .addSpatialMarker(new Vector2d(locations.leftHighJunc.getX() + 17,
                        locations.leftHighJunc.getY() +17), () -> {
                    mpb.lift.setPower(0);
                })
                .lineTo(new Vector2d(locations.leftHighJunc.getX() + 12,
                        locations.leftHighJunc.getY() + 12))
                .turn(Math.toRadians(-45))
                .lineTo(new Vector2d(locations.leftHighJunc.getX() + 5,
                        locations.leftHighJunc.getY() + 5))
                .build();

        if(!isStopRequested() && opModeIsActive()) {
            //begin
            mecanumDrive.followTrajectorySequence(preLoad1);

            //end
            if (tagOfInterest == 17) {

            }
            if (tagOfInterest == 18) {
            }
            if (tagOfInterest == 19) {

            }
        }

        if (isStopRequested()) {
            StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);

            autocam.destroyCameraInstance();
        }
    }
}
