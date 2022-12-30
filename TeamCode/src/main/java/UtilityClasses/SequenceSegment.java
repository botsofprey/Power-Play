package UtilityClasses;

import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import java.util.List;

import Subsystems.Pose2d;

public abstract class SequenceSegment {
    private final double duration;
    private final Pose2d startPose;
    private final Pose2d endPose;
    private final List<TrajectoryMarker> markers;
    
    protected SequenceSegment(
            double duration,
            Pose2d startPose, Pose2d endPose,
            List<TrajectoryMarker> markers
    ) {
        this.duration = duration;
        this.startPose = startPose;
        this.endPose = endPose;
        this.markers = markers;
    }

    public SequenceSegment(double duration, double start, Object end, List<TrajectoryMarker> markers, double duration1, Pose2d startPose, Pose2d endPose, List<TrajectoryMarker> markers1) {
        this.duration = duration1;
        this.startPose = startPose;
        this.endPose = endPose;
        this.markers = markers1;
    }

    public double getDuration() {
        return this.duration;
    }
    
    public Pose2d getStartPose(){
        return startPose;
    }
    
    public Pose2d getEndPose(){
        return endPose;
    }
    
    public List<TrajectoryMarker> getMarkers(){
        return markers;
    }
}
