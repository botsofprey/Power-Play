package UtilityClasses;

import java.util.Collections;

public class TrajectorySegment extends SequenceSegment{
    private final Trajectory trajectory;

    public TrajectorySegment(Trajectory trajectory) {
        super(trajectory.duration(), trajectory.start(), trajectory.end(), Collections.emptyList());
        this.trajectory = trajectory;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }
}
