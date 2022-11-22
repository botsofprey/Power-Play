package Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.opencv.core.Mat;

import DriveEngine.MecanumDrive;
import UtilityClasses.Location;
import UtilityClasses.PidController;

public class threeWheelOdometry {

    private LinearOpMode opMode;

    private MecanumDrive meccanumDrive;

    //private DcMotor leftVert, rightVert, horizontal, other;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    private Location positionLocation, targetLocation;

    private DistanceSensor leftDisSensor, rightDisSensor;
    private DistanceUnit distanceUnit = DistanceUnit.CM;

    private boolean moving=false;
    private enum direction {
        x,
        y,
        angle,
        stationary
    };
    private direction currentMovement;
    public PidController movePID, headingPID;

    //Robot measurements in C
    public final static double DISTANCE_FROM_MIDPOINT = 6;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * Math.PI * 2;
    public final static double CM_PER_TICK = (3.5 * Math.PI) / 1977;

    public threeWheelOdometry (HardwareMap hardwareMap, Location start, LinearOpMode op, MecanumDrive drive){
        meccanumDrive = drive;

        //leftDisSensor = hardwareMap.get(DistanceSensor.class, "leftDistance");
        //rightDisSensor = hardwareMap.get(DistanceSensor.class, "rightDistance");

        //Set start point
        positionLocation = start;

        this.opMode = op;

        currentMovement = direction.stationary;
        movePID = new PidController(.625, 0, .5);
    }

    private void calculateChange(){
        int dx1 = currentLeftPos - prevLeftPos,
            dx2 = currentRightPos - prevRightPos,
            dy = currentAuxPos - prevAuxPos;

        double angleDiff = meccanumDrive.getAngle() - positionLocation.angle;
        positionLocation.angle = meccanumDrive.getAngle();

        double theta = Math.toRadians(angleDiff),
                x = (dx1 * Math.cos(theta)) - (dy * Math.sin(theta)),
                y = (dx1 * Math.sin(theta)) + (dy * Math.cos(theta));

        //

        positionLocation.add(x * CM_PER_TICK, y * CM_PER_TICK, 0);
        positionLocation.angle = meccanumDrive.getAngle();

        //dx = x change
        //dy = y change
        //dt = delta theta
    }

    public void setTargetPoint(Location target){
        targetLocation = target;
        moving = true;
        movePID.reset();
        moveTowards(positionLocation.difference(targetLocation));
    }

    public void next90degrees(int negPos){
        Location target = new Location(positionLocation.x, positionLocation.y);

        if(negPos != -1 && negPos != 1)
            return;

        double angle = meccanumDrive.getAngle() + (negPos * 90),
                abs = Math.abs(angle);

        if(abs > 0 && abs <= 45){
            angle = 0;
        } else if(abs > 45 && abs <= 135){
            angle = 90;
        } else if(abs > 135 && abs <= 225){
            angle = 180;
        } else {
            angle = 0;
        }

        angle *= negPos;
        target.angle = angle;
        setTargetPoint(target);
    }

    private void moveTowards(Location diff){
        if(!compare(diff.x, 0, 15)){
            double power = movePID.calculateResponse(diff.x);

            meccanumDrive.moveWithPower(power);

            if(currentMovement != direction.x){
                System.out.println("Current state change: x at " + getLocation());
            }
            currentMovement = direction.x;

        }else if(!compare(diff.y, 0, 15)){
            double power = movePID.calculateResponse(diff.y);

            meccanumDrive.moveWithPower(power, -power, power, -power);
            if(currentMovement != direction.y){
                System.out.println("Current state change: y at " + getLocation());
            }
            currentMovement = direction.y;

        }else if (!compare(diff.angle, 0, 10)){
            double power = diff.angle / 90;
            power = Range.clip(power, -1, 1);

            meccanumDrive.moveWithPower(-power,-power,power,power);
            if(currentMovement != direction.angle){
                System.out.println("Current state change: angle at " + getLocation());
            }
            currentMovement = direction.angle;
        } else {
            currentMovement = direction.stationary;
            moving = false;
        }
    }

    public void update(){
        //Check if at target position and heading
        if(moving && !atTarget()){
            Location diff = positionLocation.difference(targetLocation);
            moveTowards(diff);

        }else{
            moving = false;
            currentMovement = direction.stationary;
            meccanumDrive.brake();
        }

        //Update previous & current position
        prevRightPos = currentRightPos;
        prevLeftPos = currentLeftPos;
        prevAuxPos = currentAuxPos;
        currentRightPos = meccanumDrive.motors[3].getCurrentPosition();
        currentLeftPos = -meccanumDrive.motors[0].getCurrentPosition();
        currentAuxPos = meccanumDrive.motors[1].getCurrentPosition();

        //Update current point values
        calculateChange();
    }

    public void moveToOpenSpace(double x, double y, double r, boolean slowMode){
        /*if (slowMode)
            meccanumDrive.moveTrueNorth(x, y, r);

        int xMovement = (int)Math.round(x),
            yMovement = (int) Math.round(y);


         */
        //29.845

        int newX = (int) Math.round(positionLocation.x / 29.845),
            newY = (int) Math.round(positionLocation.y / 29.845);

        newX = newX == 0 ? 1 : newX;
        newY = newY == 0 ? 1 : newY;

        double newPosX = ((newX + 1) * 29.845) + (newX * 59.69),
               newPosY = ((newY + 1) * 29.845) + (newY * 59.69);

        setTargetPoint(new Location(newPosX, newPosY));
        moving = true;
    }


    public String getLocation(){
        return Math.round(positionLocation.x) + " cm , " +
                Math.round(positionLocation.y) + " cm , " +
                Math.round(positionLocation.angle) + "°";
    }

    public boolean atTarget(){
        return positionLocation.compareAll(targetLocation, 15, 10);
    }

    private boolean compare(double a, double b, double offset){
        return Math.abs(a-b) <= offset;
    }

    private boolean equalAndNotZero(double a, double b){
        return compare(a, b, 2.5) && a != 0;
    }

    public int getCurrentLeftPos(){
        return currentLeftPos;
    }

    public int getCurrentRightPos(){
        return currentRightPos;
    }

    public int getCurrentAuxPos(){
        return currentAuxPos;
    }

    public direction getCurrentMovement(){
        return currentMovement;
    }

    public String getTargetLocation(){
        return Math.round(targetLocation.x) + " cm , " +
                Math.round(targetLocation.y) + " cm , " +
                Math.round(targetLocation.angle) + "°";
    }

}
