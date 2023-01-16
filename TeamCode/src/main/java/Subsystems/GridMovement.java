package Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    //if moving in the x direction, then y=0
    //also if x=0 still y=0
    //but if y is moving, x is whateves
    public GridMovement (double y, double x) {
        while(x=>0.0) && y=0.0 ;
        //x=>0.0 && y =0.0;
        return ;
    }

    private void when() {

    }

}
