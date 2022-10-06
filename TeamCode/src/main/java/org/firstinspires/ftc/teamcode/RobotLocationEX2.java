package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class RobotLocationEX2  extends OpMode { //idk maybe not??

    double angle;
    double x;

    public double RobotLocationEX1(double angle) {
        this.angle = angle;

        while (angle > 180) {
            angle -= 360;
        }

        while (angle < 180) {
            angle += 360;
        }
        return angle;
    }
    @Override
    public String toString(){

        return "RobotLocation: angle("+angle+") x("+x+")";
    }
    public void turn(double angleChange){

        angle += angleChange;
    }
    public void setAngle(double angle){

        this.angle=angle;
    }
    //public void getAngle() ??? corrected to below not sure which one is correct
    public double getAngle(){

        return angle; //i thought i had to return this.angle
    }
    //now im stuck
    //nvm
    public double getX() {
        return x;
    }

    public String getHeading() {
        return null;
    }

    public void changeY(double v) {
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}