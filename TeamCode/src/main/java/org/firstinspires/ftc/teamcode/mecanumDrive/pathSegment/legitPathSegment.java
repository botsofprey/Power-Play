package org.firstinspires.ftc.teamcode.mecanumDrive.pathSegment;

import static org.firstinspires.ftc.teamcode.mecanumDrive.pathSegment.localization.lastKnownPosition;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import java.util.List;

public class legitPathSegment {
    //temporal data
    public final double targetDuration;
    //positional data
    public final Pose2d startPosition;
    public final Pose2d targetEndPosition;
    //path data
    public final double frontLeftMotor;
    public final double frontRightMotor;
    public final double backLeftMotor;
    public final double backRightMotor;
    //
    private final List<TrajectoryMarker> markers;

    public legitPathSegment(
            double targetDuration,
            Pose2d targetEndPosition,
            double frontLeftMotor,
            double frontRightMotor,
            double backLeftMotor,
            double backRightMotor,
            List<TrajectoryMarker> markers
    ) {
        this.startPosition = lastKnownPosition;

        this.targetDuration = targetDuration;
        this.targetEndPosition = targetEndPosition;
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;
        this.markers = markers;
    }
}

