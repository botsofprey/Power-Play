package org.firstinspires.ftc.teamcode;

//maybe??
//@TeleOp
public class RobotLocationEX2 {//extends OpMode idk maybe not??
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
        angle += angleChange
    }
    public void setAngle(double angle){
        this.angle=angle;
    }
    //public void getAngle() ??? corrected to below not sure which one is correct
    public double getAngle(){
        return this.angle;
    }
    //now im stuck
}