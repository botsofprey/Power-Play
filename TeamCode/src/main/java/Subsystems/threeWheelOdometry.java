package Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.opencv.core.Mat;

import DriveEngine.MecanumDrive;
import UtilityClasses.Location;

public class threeWheelOdometry {

    private LinearOpMode opMode;

    private MecanumDrive meccanumDrive;

    private DcMotor leftVert, rightVert, horizontal, other;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    private Location positionLocation, targetLocation;

    private boolean moving=false;

    //Robot measurements in C
    public final static double DISTANCE_FROM_MIDPOINT = 6;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * Math.PI * 2;
    public final static double CM_PER_TICK = (3.5 * Math.PI) / 1977;

    public threeWheelOdometry (HardwareMap hardwareMap, Location start, LinearOpMode op){
        meccanumDrive = new MecanumDrive(hardwareMap, op, start.angle);

        leftVert = hardwareMap.get(DcMotor.class, "frontRightDriveMotor");
        rightVert = hardwareMap.get(DcMotor.class, "backLeftDriveMotor");
        horizontal = hardwareMap.get(DcMotor.class, "frontLeftDriveMotor");
        leftVert.setDirection(DcMotorSimple.Direction.REVERSE);
        //Reset encoders to 0 ticks
        leftVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //When power = 0, brake
        leftVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set start point
        positionLocation = start;

        this.opMode = op;
    }

    private void calculateChange(){
        int dx1 = currentLeftPos - prevLeftPos,
            dx2 = currentRightPos - prevRightPos,
            dy = currentAuxPos - prevAuxPos;



        double theta = (dy / ANGLE_CIRCUMFERENCE) * 360,
                x = (dx1 * Math.cos(theta)) - (dy * Math.sin(theta)),
                y = (dx1 * Math.sin(theta)) + (dy * Math.cos(theta));

        positionLocation.add(x * CM_PER_TICK, y * CM_PER_TICK, theta);

        // dx's != 0 and dy = 0 -- forward
        //dx's = 0 and dy != 0 -- side
        //dx's != 0 and dy !=0 -- diagonal
        //dx's = opposites -- turn

        /*
        if(equalAndNotZero(dx1, dx2) && dy == 0){ //Went Straight
            double cmTraveled = dx1 * CM_PER_TICK;

            //Calculate x and y
            double x = Math.sin(positionLocation.theta) * cmTraveled,
                    y = Math.cos(positionLocation.theta) * cmTraveled;

            positionLocation.add(x, y, 0);

        } else if(dy != 0 && dx1 == dx2 && dx1 == 0){ //Went Side to Side
            double cmTraveled = dy * CM_PER_TICK;

            double x = Math.sin(90 - positionLocation.theta) * cmTraveled,
                    y = Math.cos(90 - positionLocation.theta) * cmTraveled;

            positionLocation.add(x, y, 0);

        } else if(equalAndNotZero(dx1, dx2) && dy != 0){ //diagonal
            double cmX = dx1 * CM_PER_TICK,
                   cmY = dy * CM_PER_TICK,
                    cmTraveled = Math.hypot(cmX, cmY); //Find distance traveled

            //Find theta of diagonal movement
            double movementAngle = Math.tanh(cmX/cmY);

            double x = Math.cos(movementAngle) * cmTraveled,
                    y = Math.sin(movementAngle) * cmTraveled;

            positionLocation.add(x,y,0);

        }else if (!compare(meccanumDrive.getAngle(), meccanumDrive.lastAngles.firstAngle, 15)){ //Turned
            double cmTravaled = dy * CM_PER_TICK;
            double dt = (cmTravaled / ANGLE_CIRCUMFERENCE) * 360; //Calculate theta
            positionLocation.add(0, 0, dt);
        } else { //Should not make it here
            System.out.println("Oh no...");
        }

         */

        //dx = x change
        //dy = y change
        //dt = delta theta
    }

    public void setTargetPoint(Location target){
        targetLocation = target;
        moving = true;
    }

    public void update(){
        //Check if at target position and heading
        if(moving && !atTarget()){
            Location diff = positionLocation.difference(targetLocation);

            double theta = (dy / ANGLE_CIRCUMFERENCE) * 360,
                    x = (dx1 * Math.cos(theta)) - (dy * Math.sin(theta)),
                    y = (dx1 * Math.sin(theta)) + (dy * Math.cos(theta));

            if(compare(diff.x, 0, 5)){
                double power = diff.x / Math.abs(diff.x);
                meccanumDrive.moveWithPower(power);
            }else if(compare(diff.y, 0, 5)){
                double power = diff.y / Math.abs(diff.y);
                meccanumDrive.moveWithPower(power, -power, power, -power);
            }else{
                meccanumDrive.rotateToAngle(targetLocation.angle);
            }
        }else{
            moving = false;
        }

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

        targetLocation = new Location(newPosX, newPosY);
        moving = true;
    }


    public String getLocation(){
        return positionLocation.x + " cm , " +
                positionLocation.y + " cm , " +
                positionLocation.angle + "°";
    }

    public boolean atTarget(){
        return positionLocation.compareAll(targetLocation, 5, 10);
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

}
