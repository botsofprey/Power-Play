package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class RobotLocationsRadians extends OpMode {
    double angleRadians;

    public RobotLocationsRadians(double angleDegrees){
        this.angleRadians = Math.toRadians(angleDegrees);
    }
    public double getHeading() {
        double angle = this.angleRadians;
        while (angle > Math.PI){
            angle +=2 * Math.PI;
        }
        return Math.toDegrees(angle);
    }
    @Override
    public String toString(){
        return "RobotLocationRadians: angle (" + angleRadians +")";
    }
    public void turn(double angleChangeDegrees) {
        angleRadians += Math.toRadians(angleChangeDegrees);
    }
    public void setAngle(double angleDegrees) {
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
