package DriveEngine;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

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

import UtilityClasses.Location;
import UtilityClasses.StigmoidController;
import UtilityClasses.Vector2D;
//import UtilityClasses.PIDController;

/**
 * This is a class for moving the robot to set positions on the field.
 * It also automatically calls the localizer to update the robot's location.
 *
 * @author Alex Prichard
 */
public class MecanumDrive {
    private DcMotor[] motors = new DcMotor[4];
    private static final String[] MOTOR_NAMES = {
            "frontLeftDriveMotor",
            "backLeftDriveMotor",
            "frontRightDriveMotor",
            "backRightDriveMotor"
    };
    private DcMotorSimple.Direction[] directions = {
            DcMotorSimple.Direction.REVERSE,
            DcMotorSimple.Direction.REVERSE,
            DcMotorSimple.Direction.FORWARD,
            DcMotorSimple.Direction.FORWARD
    };

    private double maxSpeed = .8,
    slowSpeed = .5, currentSpeed;

    private LinearOpMode mode;

    private BNO055IMU imu;
    public Orientation lastAngles = new Orientation();
    private double globalAngle, prevAngle;
    private double driverAngle;
    private double targetAngle;
    private boolean rotating;

    public final static double TICKS_PER_CENTI = (5281.1 / (9 * Math.PI));
    private static final double tile = 2.54 * 24.0;

    private int[] startPos = new int[4],
    endPos = new int[4],
    distanceTraveled = new int[4];

    // for config file, see RobotConfig.json
    // track width and track length change based on width and length of the robot
    public MecanumDrive(HardwareMap hw, double startAngle /*, String configFileName,
                        Location startLocation, LinearOpMode m */ ) {
   //     this(hw, configFileName, startLocation.getAsOldLocation(), m);

        for(int i = 0; i < motors.length; i++){
            motors[i] = hw.get(DcMotor.class, MOTOR_NAMES[i]);
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[i].setDirection(directions[i]);
        }
        //Set IMU parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hw.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        globalAngle = startAngle;
        driverAngle=startAngle;

        currentSpeed = maxSpeed;
    }
    public MecanumDrive(HardwareMap hw, LinearOpMode m, double startAngle){
        for(int i = 0; i < motors.length; i++){
            motors[i] = hw.get(DcMotor.class, MOTOR_NAMES[i]);
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[i].setDirection(directions[i]);
        }

        currentSpeed = slowSpeed;

        mode = m;

        //odometry = odo;

        //Set IMU parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hw.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        globalAngle = -startAngle;
        driverAngle=startAngle;
    }

    public void moveTrueNorth(double forward, double right, double rotate){
       Orientation curOrient =
               imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

       double theta = Math.atan2(forward, right);
       double r = Math.hypot(forward, right);

       theta = AngleUnit.normalizeRadians(theta-curOrient.firstAngle);

       double newForward = r * Math.sin(theta);
       double newRight = r * Math.cos(theta);

       moveWithPower(
                newForward + newRight + rotate,
                newForward - newRight + rotate,
               newForward + newRight - rotate,
               newForward - newRight - rotate
       );
    }


    public void gridMovement(@NonNull Vector2D movement, Location current){
        double xMovement = 0, yMovement = 0;

        telemetry.update();

        if(movement.y > movement.x){
            xMovement = Math.sin(getRadians()) * movement.y;

           double tileYOffset = (tile * Math.round(current.y/tile)) - current.y;
           yMovement = stig.calculateResponse(Math.cos(getRadians()) *
                   tileYOffset) * movement.y;

        } else if(movement.x != 0){
            yMovement = Math.cos(getRadians()) * movement.x;

            double tileXOffset = (tile * Math.round(current.x/tile)) - current.x;
            xMovement = stig.calculateResponse(Math.sin(getRadians()) *
                    tileXOffset) * movement.x;
        }

        moveWithPower(
                xMovement + yMovement,
                xMovement - yMovement,
                xMovement + yMovement,
                xMovement - yMovement
        );
    }

    StigmoidController stig = new StigmoidController(1, 1, 1);
    public void grid(Vector2D movement, Location current){
        double relXMovement = 0, relYMovement = 0;

        if(Math.round(movement.y) != 0){
            relXMovement = Math.sin(getRadians()) * movement.x;

            double tileYOffset = (tile * Math.round(current.y/tile)) - current.y;
            relYMovement = stig.calculateResponse(Math.cos(getRadians()) * tileYOffset) * movement.x;

        } else if(Math.round(movement.x) != 0){
            relYMovement = Math.cos(getRadians()) * movement.y;

            double tileXOffset = (tile * Math.round(current.y/tile)) - current.y;
            relXMovement = stig.calculateResponse(Math.cos(getRadians()) * tileXOffset) * movement.y;
        }

        moveWithPower(
                relXMovement + relYMovement,
                relXMovement - relYMovement,
                relXMovement + relYMovement,
                relXMovement - relYMovement
        );
    }

    public void moveWithPower(double fl, double bl, double br, double fr){
        motors[0].setPower(Range.clip(fl, -1, 1) * currentSpeed);
        motors[1].setPower(Range.clip(bl, -1, 1) * currentSpeed);
        motors[2].setPower(Range.clip(fr, -1, 1) * currentSpeed);
        motors[3].setPower(Range.clip(br, -1, 1) * currentSpeed);
    }

    public void moveWithPower(double power){
        for(DcMotor m : motors){
            m.setPower(power * currentSpeed);
        }
    }

    public void rotatewithPower(double left, double right){
        motors[0].setPower(left * currentSpeed);
        motors[1].setPower(left * currentSpeed);
        motors[2].setPower(right * currentSpeed);
        motors[3].setPower(right * currentSpeed);
    }

    public static final String
            LEFT = "LEFT",
            RIGHT = "RIGHT",
            FORWARD = "FORWARD",
            BACKWARD = "BACKWARD";
    public void moveCenti(double centimeters, String direction){
        for(DcMotor m : motors){
            m.setTargetPosition((int)(centimeters * TICKS_PER_CENTI) + m.getCurrentPosition());
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m.setPower(.5);
        }

        switch (direction) {
            case LEFT:
                motors[0].setTargetPosition(-motors[0].getTargetPosition());
                motors[1].setTargetPosition(-motors[0].getTargetPosition());
                break;
            case RIGHT:
                motors[2].setTargetPosition(-motors[0].getTargetPosition());
                motors[3].setTargetPosition(-motors[0].getTargetPosition());
                break;
            case BACKWARD:
                motors[0].setTargetPosition(-motors[0].getTargetPosition());
                motors[1].setTargetPosition(-motors[0].getTargetPosition());
                motors[2].setTargetPosition(-motors[0].getTargetPosition());
                motors[3].setTargetPosition(-motors[0].getTargetPosition());
                break;
        }
    }

    public double getPower(int index){
        return motors[index].getPower();
    }

    public String getPowers(){
        return motors[0].getPower() + ", " +
                motors[1].getPower() + ", " +
                motors[2].getPower() + ", " +
                motors[3].getPower();
    }

    public double getAngle()
    {
        prevAngle = globalAngle;
        System.out.println("IMU NULL???: " + imu.getAngularOrientation());
        System.out.println("IMU NULL???: " + imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
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

    public void brake(){
        for(DcMotor m : motors){
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setPower(0);
        }
    }

    public void slowMode(){
        currentSpeed = currentSpeed == slowSpeed ? maxSpeed : slowSpeed;
    }

    public void slowMode(boolean slow){
        if (slow)
            currentSpeed = slowSpeed;
        else
            currentSpeed = maxSpeed;
    }

    public boolean isBusy(){
        return motors[0].isBusy();
    }

    public int getPosition(){
        return motors[0].getCurrentPosition();
    }

    public int getCurrentPositionMotor(int index){
        return motors[index].getCurrentPosition();
    }

    public void setModes(DcMotor.RunMode mode){
        for(DcMotor m : motors){
            m.setMode(mode);
        }
    }

    public int getTarget(){
        return motors[0].getTargetPosition();
    }


    public void rotateToAngle(double angle){
        targetAngle = angle;
        rotating = true;
        if(targetAngle < getAngle()){
            motors[0].setPower(-1);
            motors[1].setPower(-1);
            motors[2].setPower(1);
            motors[3].setPower(1);
        } else {
            motors[0].setPower(1);
            motors[1].setPower(1);
            motors[2].setPower(-1);
            motors[3].setPower(-1);
        }
    }

    private boolean compare(double a, double b, double diff){
        return Math.abs(a-b) <= diff;
    }

    public double getPrevAngle(){
        return prevAngle;
    }

    public double getRadians() {
        return Math.toRadians(getAngle());
    }

    public void setCurrentSpeed(double speed) {
        currentSpeed = speed;
    }
}

