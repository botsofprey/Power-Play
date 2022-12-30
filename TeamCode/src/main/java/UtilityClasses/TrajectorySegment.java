package UtilityClasses;

import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import java.util.Collections;
import java.util.List;

import Subsystems.Pose2d;

public class TrajectorySegment extends SequenceSegment{
    protected TrajectorySegment(double duration, Pose2d startPose, Pose2d endPose, List<TrajectoryMarker> markers) {
        super(duration, startPose, endPose, markers);
    }
    /*
    private final Trajectory trajectory;

    public TrajectorySegment(Trajectory trajectory) {
        super(trajectory.duration(), trajectory.start(), trajectory.end(), Collections.emptyList());
        this.trajectory = trajectory;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

     */
}
