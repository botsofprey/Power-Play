package UtilityClasses;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

public class TrajectorySegment {
    public final Trajectory trajectory;

    public TrajectorySegment(Trajectory trajectory) {
       // super(trajectory.duration(),
                //trajectory.start(),
                //trajectory.end(),
                //Collections.emptyList());
        this.trajectory = trajectory;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }
}
