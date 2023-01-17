package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
    //set size of tag in meters
    double tagsize = 0.166;
    //lens intrinsics
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    Trajectory right19,
            forward18,
            forward19,
            left17,
            forward17,
            preLoad1,
            preLoad2,
            test;

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

        mecanumDrive.setPoseEstimate(locations.leftBlueStart);

        preLoad1 = mecanumDrive.trajectoryBuilder(locations.leftBlueStart)
                .addDisplacementMarker(0.0, () -> {
                    mpb.lift.setPower(1);
                })
                .addTemporalMarker(2, () -> {
                    mpb.lift.setTargetPosition(0);
                })
                .lineTo(new Vector2d(locations.leftHighJunc.getX() + 12,
                        locations.leftHighJunc.getY() + 12))
                .build();

        preLoad2 = mecanumDrive.trajectoryBuilder(preLoad1.end().plus(new Pose2d(0, 0, Math.toRadians(-45))))
                .lineToLinearHeading(new Pose2d(locations.leftHighJunc.getX() + 5,
                                    locations.leftHighJunc.getY() + 5, Math.toRadians(225)))
                .build();

        right19 = mecanumDrive.trajectoryBuilder(new Pose2d()).strafeRight(24).build();
        forward19 = mecanumDrive.trajectoryBuilder(right19.end()).forward(24).build();
        forward18 = mecanumDrive.trajectoryBuilder(new Pose2d()).forward(24).build();
        left17 = mecanumDrive.trajectoryBuilder(new Pose2d()).strafeLeft(24).build();
        forward17 = mecanumDrive.trajectoryBuilder(left17.end()).forward(24).build();

        //tag detection
        tagData = null;
        ArrayList<AprilTagDetection> currentDetections = apriltagpipelineEXAMPLE.getLatestDetections();
        mecanumDrive.getPoseEstimate();
        if (currentDetections.size() != 0) {
            for (AprilTagDetection tag : currentDetections) {
                if (tag.id >= 17 || tag.id <= 19) {
                    tagOfInterest = tag.id;
                    telemetry.addData("Tag of interest", tagOfInterest);
                    telemetry.addData("Tag data", tag.toString());
                    break;
                }
            }
        }
        else {
            telemetry.addLine("No tag found.");
        }

        waitForStart();

        if(!isStopRequested() && opModeIsActive()) {
            //begin
            mecanumDrive.followTrajectory(preLoad1);
            mecanumDrive.updatePoseEstimate();
            mecanumDrive.turn(Math.toRadians(-45));
            mecanumDrive.updatePoseEstimate();
            mecanumDrive.followTrajectory(preLoad2);
            mecanumDrive.updatePoseEstimate();

            //end
            if (tagOfInterest == 17) {
                mecanumDrive.followTrajectory(left17);
                mecanumDrive.followTrajectory(forward17);
            }
            if (tagOfInterest == 18) {
                mecanumDrive.followTrajectory(forward18);
            }
            if (tagOfInterest == 19) {
                mecanumDrive.followTrajectory(right19);
                mecanumDrive.followTrajectory(forward19);
            }
            StaticImu.imuStatic = mpb.getHeading(AngleUnit.RADIANS);

            autocam.destroyCameraInstance();
        }
    }
}
