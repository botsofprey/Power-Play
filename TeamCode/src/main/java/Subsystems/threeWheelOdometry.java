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

    private DcMotor leftVert, rightVert, horizontal;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    private Location positionLocation, targetLocation;

    private BNO055IMU imu;
    private Orientation lastAngles = new Orientation();
    private double globalAngle;

    //Robot measurements in CM
    //***Set values later
    public final static double DISTANCE_FROM_MIDPOINT;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * 2 * Math.PI;
    public final static double CM_PER_TICK;

    public threeWheelOdometry (HardwareMap hardwareMap, Location start, LinearOpMode op){
        meccanumDrive = new MecanumDrive(hardwareMap);

        leftVert = hardwareMap.get(DcMotor.class, "verticalLeft");
        rightVert = hardwareMap.get(DcMotor.class, "verticalRight");
        horizontal = hardwareMap.get(DcMotor.class, "horizontal");
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

        //Set IMU parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        globalAngle = positionLocation.angle;

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
            positionLocation.add(cmTraveled, 0, 0);

        } else if(dy != 0 && dx1 == dx2 && dx1 == 0){ //Went Side to Side
            double cmTraveled = dy * CM_PER_TICK;
            positionLocation.add(0, cmTraveled, 0);

        } else if(equalAndNotZero(-dx1, dx2) && dy != 0){ //diagonal
            double cmX = dx1 * CM_PER_TICK,
                   cmY = dy * CM_PER_TICK;
            positionLocation.add(cmX,cmY,0);

        }else if (!compare(getAngle(), lastAngles.firstAngle, 15)){ //Turned
            double cmTravaled = dy * CM_PER_TICK;
            double dt = (cmTravaled / ANGLE_CIRCUMFERENCE) * 360; //Calculate angle
            positionLocation.add(0, 0, dt);
        } else{ //Should not make it here
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
        //***Add movement
        if(!positionLocation.compareAll(targetLocation, 2.5, 15)){
            if(!positionLocation.comparePosition(targetLocation, 2.5)){ //Not in target position
                Location cmNeeded = positionLocation.difference(targetLocation);
                if(Math.abs(cmNeeded.x) > 2.5) { //Needs to move forward or backward
                    double motorPower = .5 * Range.clip(cmNeeded.x, -1, 1);
                    meccanumDrive.move(motorPower,motorPower,motorPower,motorPower);
                }else{ //Needs to move side to side
                    double motorPower = .5 * Range.clip(cmNeeded.y, -1, 1);
                    meccanumDrive.move(motorPower,-motorPower,-motorPower,motorPower);
                }
            }else { //Not in target rotation
                meccanumDrive.move(-.5, -.5, .5, .5);
            }
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

    public String getPoint(){
        return positionLocation.x + ", " + positionLocation.y + ", " + positionLocation.angle;
    }

    public double getAngle()
    {
        Orientation angles = imu.getAngularOrientation
                (AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        if (globalAngle < -180)
            globalAngle += 360;
        else if (globalAngle > 180)
            globalAngle -= 360;

        return -globalAngle;
    }

    private boolean compare(double a, double b, double offset){
        return Math.abs(a-b) <= offset;
    }

    private boolean equalAndNotZero(double a, double b){
        return a == b && a != 0;
    }
}
