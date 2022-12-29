package org.firstinspires.ftc.teamcode.mecanumDrive.pathSegment;

import static org.firstinspires.ftc.teamcode.mecanumDrive.pathSegment.localization.lastKnownPosition;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import java.util.List;

public class pathSegment {
    //temporal data
    public final double targetDuration;
    //positional data
    public final Pose2d startPosition;
    public final Pose2d targetEndPosition;
    //path data
    public final double turnDEG;
    public final double strafe;
    public final double forback;
    //
    public final List<TrajectoryMarker> markers;

    public pathSegment(
            double targetDuration,
            Pose2d endPosition,
            double turnDEGREES,
            double strafe,
            double forback,
            List<TrajectoryMarker> markers
    ) {
        this.startPosition = lastKnownPosition;

        this.targetDuration = targetDuration;
        this.targetEndPosition = endPosition;
        this.turnDEG = turnDEGREES;
        this.strafe = strafe;
        this.forback = forback;
        this.markers = markers;
    }
}
