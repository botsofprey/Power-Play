package org.firstinspires.ftc.teamcode.hedgehogPID;

public class segment {
    public double[] motors;
    public double   duration;
    public double   targetVelocity;

    public segment(double[] motors, double duration, double targetVelocity) {
        this.motors         = motors;
        this.duration       = duration;
        this.targetVelocity = targetVelocity;
    }
}
