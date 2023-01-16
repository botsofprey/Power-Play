package Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import UtilityClasses.Location;
import UtilityClasses.PidController;
import UtilityClasses.Vector2D;

public abstract class GridMovement extends LinearOpMode {
    /*private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;*/
    private int x;
    private int y;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    public void gridMovement(@NonNull Vector2D movement, Location current){
        double xMovement = 0, yMovement = 0;

        double tile = 60; //cm
        PidController stig = null;
        if(movement.y > movement.x){
            xMovement = Math.sin(getRadians()) * movement.y;

            double tileYOffset = (tile * Math.round(current.y/tile)) - current.y;
            yMovement = stig.calculateResponse(Math.cos(getRadians()) * tileYOffset) * movement.y;

        } else if(movement.x != 0){
            yMovement = Math.cos(getRadians()) * movement.x;

            double tileXOffset = (tile * Math.round(current.x/tile)) - current.x;
            xMovement = stig.calculateResponse(Math.sin(getRadians()) * tileXOffset) * movement.x;
        }

        moveWithPower(
                xMovement + yMovement,
                xMovement - yMovement,
                xMovement + yMovement,
                xMovement - yMovement
        );
    }

    protected abstract void moveWithPower(double frontLeft, double backLeft,
                                          double backRight, double frontRight);

    protected abstract double getRadians();

    //if moving in the x direction, then y=0
    //also if x=0 still y=0
    //but if y is moving, x is whateves idk
    public GridMovement (double y, double x) {
        while(x=>0.0) && y==0.0;
        //x=>0.0 && y =0.0;
        return ;
    }

    private void when() {

    }

}
