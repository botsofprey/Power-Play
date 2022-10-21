package Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import DriveEngine.MecanumDrive;
import UtilityClasses.Location;

public class threeWheelOdometry {

    private LinearOpMode opMode;

    private MecanumDrive meccanumDrive;

    private DcMotor leftVert, /*rightVert,*/ horizontal;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    private Location positionLocation, targetLocation;

    //Robot measurements in CM
    //***Set values later
    public final static double DISTANCE_FROM_MIDPOINT = 3;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * 2 * Math.PI;
    public final static double CM_PER_TICK = .1;

    public threeWheelOdometry (HardwareMap hardwareMap, Location start, LinearOpMode op){
        meccanumDrive = new MecanumDrive(hardwareMap, op, start.angle);

        leftVert = hardwareMap.get(DcMotor.class, "verticalLeft");
        //rightVert = hardwareMap.get(DcMotor.class, "verticalRight");
        horizontal = hardwareMap.get(DcMotor.class, "horizontal");
        //Reset encoders to 0 ticks
        leftVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightVert.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //When power = 0, brake
        leftVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //rightVert.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set start point
        positionLocation = start;

        this.opMode = op;
    }

    private void calculateChange(){
        int dx1 = currentLeftPos - prevLeftPos,
            dx2 = currentRightPos - prevRightPos,
            dy = currentAuxPos - prevAuxPos;

        // dx's != 0 and dy = 0 -- forward
        //dx's = 0 and dy != 0 -- side
        //dx's != 0 and dy !=0 -- diagonal
        //dx's = opposites -- turn

        if(equalAndNotZero(dx1, dx2) && dy == 0){ //Went Straight
            double cmTraveled = dx1 * CM_PER_TICK;

            //Calculate x and y
            double x = Math.cos(positionLocation.angle) * cmTraveled,
                    y = Math.sin(positionLocation.angle) * cmTraveled;

            positionLocation.add(x, y, 0);

        } else if(dy != 0 && dx1 == dx2 && dx1 == 0){ //Went Side to Side
            double cmTraveled = dy * CM_PER_TICK;

            double x = Math.cos(90 - positionLocation.angle) * cmTraveled,
                    y = Math.sin(90 - positionLocation.angle) * cmTraveled;

            positionLocation.add(x, y, 0);

        } else if(equalAndNotZero(-dx1, dx2) && dy != 0){ //diagonal
            double cmX = dx1 * CM_PER_TICK,
                   cmY = dy * CM_PER_TICK,
                    cmTraveled = Math.hypot(cmX, cmY); //Find distance traveled

            //Find angle of diagonal movement
            double movementAngle = Math.tanh(cmX/cmY);

            double x = Math.cos(movementAngle) * cmTraveled,
                    y = Math.sin(movementAngle) * cmTraveled;

            positionLocation.add(x,y,0);

        }else if (!compare(meccanumDrive.getAngle(), meccanumDrive.lastAngles.firstAngle, 15)){ //Turned
            double cmTravaled = dy * CM_PER_TICK;
            double dt = (cmTravaled / ANGLE_CIRCUMFERENCE) * 360; //Calculate angle
            positionLocation.add(0, 0, dt);
        } else { //Should not make it here
            System.out.println("Oh no...");
        }

        //dx = x change
        //dy = y change
        //dt = delta theta
    }

    public void setTargetPoint(Location target){
        targetLocation = target;
    }

    public void update(){
        //Check if at target position and heading
        if(!positionLocation.compareAll(targetLocation, 2.5, 15)){
            Location diff = positionLocation.difference(targetLocation);
            meccanumDrive.moveWithPower(
                    diff.x + diff.y + diff.angle,
                    diff.x - diff.y + diff.angle,
                    diff.x + diff.y - diff.angle,
                    diff.x - diff.y - diff.angle);
        }

        //Update previous & current position
        prevRightPos = currentRightPos;
        prevLeftPos = currentLeftPos;
        prevAuxPos = currentAuxPos;
        //currentRightPos = rightVert.getCurrentPosition();
        currentLeftPos = leftVert.getCurrentPosition();
        currentAuxPos = horizontal.getCurrentPosition();

        //Update current point values
        calculateChange();
    }

    public String getLocation(){
        return positionLocation.x + ", " + positionLocation.y + ", " + positionLocation.angle;
    }



    private boolean compare(double a, double b, double offset){
        return Math.abs(a-b) <= offset;
    }

    private boolean equalAndNotZero(double a, double b){
        return a == b && a != 0;
    }
}
