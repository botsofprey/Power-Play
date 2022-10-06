package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class RobotLocation extends OpMode {
    @Override
    public void loop(){
    }
    @Override
    public void init(){
    }
    double angle;

    public RobotLocation(double angle){
        this.angle = angle;
    }

    public double getHeading(){
        double angle = this.angle;
        while(angle > 180){
            angle -=360;
        }
        while(angle < -180){
            angle +=360;
        }
        return angle;
    }
    @Override
    public String toString(){
        return "RobotLocation: angle("+angle+")";
    }
    public void turn(double angleChange){
        angle +=angleChange;
    }
    public void setAngle(double angle){
        this.angle = angle;
    }
}
