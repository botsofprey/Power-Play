package DriveEngine;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.Range;

//import DriveEngine.Deprecated.OldLocalizerClass;
//import UtilityClasses.JSONReader;
//import UtilityClasses.Deprecated.OldLocationClass;
//import UtilityClasses.Deprecated.Matrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import Subsystems.threeWheelOdometry;
import UtilityClasses.Location;
//import UtilityClasses.PIDController;

/**
 * This is a class for moving the robot to set positions on the field.
 * It also automatically calls the localizer to update the robot's location.
 *
 * @author Alex Prichard
 */
public class MecanumDrive {
    public DcMotor[] motors = new DcMotor[4];
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

    private double maxSpeed = 1,
    slowSpeed = .5, currentSpeed = .5;

    private LinearOpMode mode;

    private BNO055IMU imu;
    public Orientation lastAngles = new Orientation();
    private double globalAngle, prevAngle;
    private double driverAngle;
    private double targetAngle;
    private boolean rotating;

    public final static double TICKS_PER_CENTI = (5281.1 / (9 * Math.PI));

    private int[] startPos = new int[4],
    endPos = new int[4],
    distanceTraveled = new int[4];

    private double Kt;
    private double R;
    private double Kv;

 //   private OldLocalizerClass localizer;
//    private OldLocationClass currentLocation;
    private boolean currentlyMoving;
    private static final double buffer = 0.1;
    private static final double maxTorque = 1;
    public static final double MAX_SPEED = 24;
    public static final double MAX_ANGULAR = 90;

    // these have been tuned, you can re tune them if you want and know what you're doing
    private PIDCoefficients xCoefficients = new PIDCoefficients(0.08, 0.01, 0.02);
    private PIDCoefficients yCoefficients = new PIDCoefficients(0.08, 0.01, 0.02);
    private PIDCoefficients headingCoefficients = new PIDCoefficients(0.02, 0.006, 0.005);
  //  private PIDController xController = new PIDController(xCoefficients);
  //  private PIDController yController = new PIDController(yCoefficients);
  //  private PIDController hController = new PIDController(headingCoefficients);

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

        globalAngle = startAngle;
        driverAngle=startAngle;
    }

    public void moveToLocation(double x, double y, double h) {
        moveToLocation(new Location(x, y, h));
    }

    public void moveToLocation(Location targetLocation) {
       // moveToLocation(targetLocation.getAsOldLocation());

    }

    /**
     * This function implements the underlying movement logic for the drive base.
     * It is blocking and suspends execution until it finishes.
     * It can move to a location with a different heading from the current heading,
     * however turning while moving decreases its ability to stay on a straight path.
     * Additionally, due to how mecanum wheels work and some shortcomings of the
     * resulting feedback system, the coordinate system it uses is slightly stretched.
     * This stretching also changes every time the robot rotates.
     * In long autonomous programs, you should expect
     * the robot to not quite go where you tell it to.
     * Despite the poor accuracy of this movement function, it can actually be quite precise.
     *
     * To use it in an autonomous op mode, you should first make a list of all
     * locations you want the robot to go to as final variables at the top of the class.
     * Then, run the program up to the first untuned location.
     * Measure how far off the robot was and adjust the location in the code accordingly.
     * Repeat this until you can run the entire autonomous program without
     * the robot significantly deviating from the locations you set for it.
     * After you have a basic working path, readjust the locations
     * slightly as needed and test in between each adjustment.
     * This process will take a while.
     *
     * @param targetLocation
     * @param speed
     * @author Alex Prichard
     */

    /**
     * This rotates the robot to the given heading.
     * It works pretty well and reliably.
     *
     * @param //angle
     * @author Alex Prichard
     */


    // there's not much reason to use this method
    public void update() {

        if(currentlyMoving){
            for(DcMotor m : motors){

            }
        }
        if(rotating){
            double angleError = getAngle() - targetAngle;
            int negate=1;
            if(angleError / 180 > 1){
                negate = -1;
                angleError %= 180;
            }

			double newPower = negate*(angleError / 360);
            rotatewithPower(-newPower, newPower);

            if(compare(targetAngle, getAngle(), 15)){
                brake();
                rotating = false;
            }
        }
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

    public double getPower(){
        return motors[0].getPower();
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
}

