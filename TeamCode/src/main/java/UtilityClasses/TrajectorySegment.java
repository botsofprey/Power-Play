package UtilityClasses;

//import com.acmerobotics.roadrunner.trajectory.Trajectory;

import Subsystems.AutoTransferPose;

public class TrajectorySegment {
    public final AutoTransferPose.Trajectory trajectory;

    public TrajectorySegment(AutoTransferPose.Trajectory trajectory) {
       // super(trajectory.duration(),
                //trajectory.start(),
                //trajectory.end(),
                //Collections.emptyList());
        this.trajectory = trajectory;
    }

    public AutoTransferPose.Trajectory getTrajectory() {
        return this.trajectory;
    }
}
