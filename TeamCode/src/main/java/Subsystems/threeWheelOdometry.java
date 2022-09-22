package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import UtilityClasses.Point;

public class threeWheelOdometry {

    private DcMotor leftVert, rightVert, horizontal;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    private Point positionPoint, targetPoint;

    //Robot measurements in CM
    //***Set values later
    public final static double DISTANCE_FROM_MIDPOINT;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * 2 * Math.PI;
    public final static double CM_PER_TICK;

    enum MOVEMENT_STATE {
        MOVING,
        TURNING,
        STATIONARY
    }
    private MOVEMENT_STATE currentState;

    public threeWheelOdometry (HardwareMap hardwareMap, Point start){
        leftVert = hardwareMap.get(DcMotor.class, "verticalLeft");
        rightVert = hardwareMap.get(DcMotor.class, "verticalRight");
        horizontal = hardwareMap.get(DcMotor.class, "horizontal");
        //Reset encoders to 0 ticks
        leftVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Don't use encoder
        leftVert.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightVert.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //When power = 0, brake
        leftVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set start point
        positionPoint = start;
    }

    private void calculateChange(){
        int dx1 = currentLeftPos - prevLeftPos,
            dx2 = currentRightPos - prevRightPos,
            dy = currentAuxPos - prevAuxPos;

        if(dx1 == dx2 && dy == 0){ //Went Straight
            double cmTraveled = dx1 * CM_PER_TICK;
            positionPoint.add(cmTraveled, 0, 0);
        } else if(dy != 0 && dx1 == dx2){ //Went Side to Side
            double cmTraveled = dy * CM_PER_TICK;
            positionPoint.add(0, cmTraveled, 0);
        } else{ //Turned
            double cmTravaled = dy * CM_PER_TICK;
            double dt = (cmTravaled / ANGLE_CIRCUMFERENCE) * 360;
            positionPoint.add(0, 0, dt);
        }
        //dx = x change
        //dy = y change
        //dt = delta theta
    }

    public void moveTo(Point target){
        targetPoint = target;
    }

    public void update(){
        //Check if at target position and heading
        //***Add movement
        /*
        if(!positionPoint.compareAll(targetPoint, 2.5, 15)){
            if(!positionPoint.comparePosition(targetPoint, 2.5)){ //Not in target position

            }else { //Not in target rotation

            }
        }
         */

        //Update previous & current position
        prevRightPos = currentRightPos;
        prevLeftPos = currentLeftPos;
        prevAuxPos = currentAuxPos;
        currentRightPos = rightVert.getCurrentPosition();
        currentLeftPos = leftVert.getCurrentPosition();
        currentAuxPos = horizontal.getCurrentPosition();

        //Update current point values
        calculateChange();
    }

    public String getPoint(){
        return positionPoint.x + ", " + positionPoint.y + ", " + positionPoint.angle;
    }

}
