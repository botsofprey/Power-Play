package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public abstract class RobotLocationEX2OpMode extends OpMode {

    double angle;
    double x,y;

    public void RobotLocationEX2(double angle){
        this.angle = angle;
    }

    public double getHeading(){
        double angle = this.angle;
        while (angle > 180){
            angle -= 360;
        }
        return angle;
    }

    RobotLocationEX2 robotLocationEX2 = new RobotLocationEX2();

    //@Override
    public void init(){
        robotLocationEX2.setAngle(0);
    }

    //@Override
    public void loop(){
        if (gamepad1.dpad_left){
            robotLocationEX2.turn(-0.1);
        } else if  (gamepad1.dpad_right){
            robotLocationEX2.turn(0.1);
        }

        if (gamepad1.dpad_up){
            robotLocationEX2.changeY(-0.1);
        } else if (gamepad1.dpad_down){
            robotLocationEX2.changeY(0.1);
        }
        telemetry.addData("Location", robotLocationEX2);
        telemetry.addData("Heading", robotLocationEX2.getHeading());
    }
}
