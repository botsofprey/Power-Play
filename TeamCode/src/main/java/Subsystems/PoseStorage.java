package Subsystems;

import UtilityClasses.Vector2D;

public class PoseStorage {
    Vector2D myVector = new Vector2D(10, -5);
    Pose2d myPose = new Pose2d(10, -5, Math.toRadians(90));
    public static Pose2d currentPose = new Pose2d();
}
