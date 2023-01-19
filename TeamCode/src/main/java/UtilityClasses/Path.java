package UtilityClasses;

import Subsystems.MecanumDrive;

public class Path {
    private Location start, end;
    private Location[] locationPath;

    public Path(Location s, Location e){
        start = s;
        end = e;
    }

    public void createPath(){

    }

    public double length() {
        return 0;
    }

    public MecanumDrive.Pose2d get(double displacement) {
        return null;
    }
}
