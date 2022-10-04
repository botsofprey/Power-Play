package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@TeleOp maybe??? prolly not
public class RobotLocationEX1 {
    double angle;

    public RobotLocationEX1 (double angle){
        this.angle = angle;
    }
        //return angle;
        @Override
        public String toString() {
            return "RobotLocation: angle("+angle+")";
        }
        public void turn(double angleChange) {
            angle += angleChange;
        }
        public double setAngle (double angle) {
            this.angle = angle;
        }
        public double getAngle() {
            return angle;
            //just want to return angle
        }

    public void setAngle(int i) {
    }
}
