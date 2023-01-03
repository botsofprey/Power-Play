package Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import DriveEngine.MecanumDrive;
import UtilityClasses.AverageDistanceSensor;
import UtilityClasses.Location;
import UtilityClasses.PidController;

public class threeWheelOdometry {

    private LinearOpMode opMode;

    private MecanumDrive meccanumDrive;

    //private DcMotor leftVert, rightVert, horizontal, other;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0, other;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    public Location positionLocation;
    private Location targetLocation;
    private Location start;

    //private AverageDistanceSensor leftDisSensor, rightDisSensor;
    private DistanceUnit distanceUnit = DistanceUnit.CM;

    private boolean moving = false, maintain = false;
    private double startAngle = 0;
    private double cmOffset = 5, angleOffset = 10;

    private double xMult = 60.0/7.0;

    private enum direction {
        x,
        y,
        angle,
        stationary
    }

    ;
    private direction currentMovement;
    public PidController movePID, headingPID;

    //Robot measurements in mm
    public final static double DISTANCE_FROM_MIDPOINT = 18.5;//6.8;
    public final static double LENGETH_BETWEEN_VERTS = 42;//29.2, LEFT_DISTANCE_FROM_MID = 13.5, RIGHT_DISTANCE_FROM_MID = 10.4;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * Math.PI * 2;
    public final static double CM_PER_TICK = (3.5 * Math.PI) / 8192;

    public threeWheelOdometry(HardwareMap hardwareMap, Location start, LinearOpMode op, MecanumDrive drive) {
        meccanumDrive = drive;

        /*
        leftDisSensor = new AverageDistanceSensor(
                hardwareMap.get(DistanceSensor.class, "leftDistance"),
                DistanceUnit.CM,
                25);
        rightDisSensor = new AverageDistanceSensor(
                hardwareMap.get(DistanceSensor.class, "rightDistance"),
                DistanceUnit.CM,
                25);

         */

        //Set start point
        positionLocation = start;

        this.opMode = op;

        currentMovement = direction.stationary;
        movePID = new PidController(.0625, 0, .125);
        headingPID = new PidController(.75, 0, .125);
    }

    private void calculateChange() {
        int dx1 = currentLeftPos - prevLeftPos,
                dx2 = currentRightPos - prevRightPos,
                dy = currentAuxPos - prevAuxPos;


        double theta = Math.toRadians((dx2-dx1)/(LENGETH_BETWEEN_VERTS)),
                //theta = meccanumDrive.getRadians() - positionLocation.getRadians(),
                fwd = (dx2+dx1)/2,
                str = dy - (DISTANCE_FROM_MIDPOINT * theta);

        System.out.println(theta);


        double relX = fwd, //500f
                relY = str; //100

        double angle = meccanumDrive.getRadians();//0
        double deltaX = (relX * Math.cos(angle)) - (relY * Math.sin(angle)), //500
                deltaY = (relY * Math.cos(angle)) + (relX * Math.sin(angle)); //100

        positionLocation.add(deltaX * CM_PER_TICK, deltaY * CM_PER_TICK,0);
        positionLocation.angle = meccanumDrive.getAngle();

        /*
        if (theta != 0) {


            double r0 = fwd / theta,
                    r1 = str / theta;
            System.out.println("COORDS: " + theta + " : " + str + " : " + r1);

            double relX = (r0 * Math.sin(theta)) - (r1 * (1 - Math.cos(theta))),
                    relY = (r1 * Math.sin(theta)) + (r0 * (1 - Math.cos(theta)));

            double angle = meccanumDrive.getRadians();
            double deltaX = (relX * Math.cos(angle)) - (relY * Math.sin(angle)),
                    deltaY = (relY * Math.cos(angle)) + (relX * Math.sin(angle));

            positionLocation.add(deltaX * CM_PER_TICK, deltaY * CM_PER_TICK, Math.toDegrees(theta));
        } else {
            double relX = fwd, //500f
                    relY = str; //100

            double angle = meccanumDrive.getRadians();//0
            double deltaX = (relX * Math.cos(angle)) - (relY * Math.sin(angle)), //500
                    deltaY = (relY * Math.cos(angle)) + (relX * Math.sin(angle)); //100

            System.out.println(deltaX + ", " + deltaY);
        }


         */
        //dx = x change
        //dy = y change
        //dt = delta theta
    }

    //Sets target
    public void setTargetPoint(Location target) {
        targetLocation = target;
        moving = true;
        movePID.reset();
        headingPID.reset();
        startAngle = meccanumDrive.getAngle();
        start = positionLocation;
        moveTowards(positionLocation.difference(targetLocation));
    }

    public void setTargetPoint(double x, double y, double h){
        setTargetPoint(new Location(x, y, h));
    }

    public void setTargetPoint(double x, double y, double h, boolean maintain){
        setTargetPoint(new Location(x, y, h));
        this.maintain = maintain;
    }

    public void cancelTarget() {
        targetLocation = null;
        moving = false;
    }

    //Rounds robot's rotation to the nearest 90 degrees and adds/subtracts 90
    public void next90degrees(int negPos) {
        Location target = new Location(positionLocation.x, positionLocation.y);

        if (negPos != -1 && negPos != 1)
            return;

        double angle = meccanumDrive.getAngle() + (negPos * 90);

        angle = Math.round(angle/90) * 90;

        target.angle = angle;
        setTargetPoint(target);
    }

    public double x, y, h;

    //Robot move towards target
    private void moveTowards(Location diff) {
        /*
        double robotMovementAngle = Math.atan2(diff.y, diff.x);
        h = robotMovementAngle - meccanumDrive.getRadians();

        x = Math.cos(h) + movePID.calculateResponse(diff.x);
        y = Math.sin(h) * .0005 + movePID.calculateResponse(diff.y);
        double rotate = Math.abs(diff.angle) < 5 ?
                0 : headingPID.calculateResponse(diff.angle);
        rotate = Range.clip(rotate, -1, 1);

        meccanumDrive.moveWithPower(
                x + y + rotate,
                x - y + rotate,
                x + y - rotate,
                x - y - rotate
        );

         */

        double robotMovementAngle = Math.atan2(diff.y, diff.x);
        h = robotMovementAngle - meccanumDrive.getRadians();

        double distance = positionLocation.distanceBetween(targetLocation);

        x = Math.cos(h) + movePID.calculateResponse(distance * Math.cos(h));
        y = Math.sin(h) + movePID.calculateResponse(distance * Math.sin(h));
        double rotate = Math.abs(diff.angle) < 5 ?
                0 : headingPID.calculateResponse(diff.angle);
        rotate = Range.clip(rotate, -.5, .5);

        if(distance < 2.5){
            x = 0;
            y = 0;
        } else {
            x = Range.clip(x, -1, 1);
            y = Range.clip(y, -1, 1);
        }

        meccanumDrive.moveWithPower(
                x + y + rotate,
                x - y + rotate,
                x + y - rotate,
                x - y - rotate
        );
    }

    public void update() {

        //Update previous & current position
        prevRightPos = currentRightPos;
        prevLeftPos = currentLeftPos;
        prevAuxPos = currentAuxPos;
        currentRightPos = meccanumDrive.getCurrentPositionMotor(1);
        currentLeftPos = -meccanumDrive.getCurrentPositionMotor(3);
        currentAuxPos = -meccanumDrive.getCurrentPositionMotor(0);
        other = meccanumDrive.getCurrentPositionMotor(2);

        //Update current point values
        calculateChange();

        //leftDisSensor.update();
        //rightDisSensor.update();

        //Check if at target position and heading
        if ((moving || maintain) && !atTarget()) {
            Location diff = positionLocation.difference(targetLocation);
            moveTowards(diff);

        } else if (moving || maintain) {
            moving = false;
            currentMovement = direction.stationary;
            meccanumDrive.brake();
        }
    }

    public void resetEncoders() {
        meccanumDrive.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        meccanumDrive.setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        prevRightPos = 0;
        prevLeftPos = 0;
        prevAuxPos = 0;
        currentRightPos = 0;
        currentLeftPos = 0;
        currentAuxPos = 0;

        positionLocation = new Location(0, 0);
    }

    public double getother() {
        return other;
    }

    //Moves the robot to the center of tile
    public void moveToOpenSpace(double x, double y, double r, boolean slowMode) {
        meccanumDrive.slowMode(slowMode);
        /*if (slowMode)
            meccanumDrive.moveTrueNorth(x, y, r);

        int xMovement = (int)Math.round(x),
            yMovement = (int) Math.round(y);


         */

        int tileX = (int) Math.round(positionLocation.x/60),
        tileY = (int) Math.round(positionLocation.y/60);

        double angle = Math.round(meccanumDrive.getAngle()/90) * 90;

        Location tar = new Location(tileX * 60, tileY * 60, angle);
        setTargetPoint(tar);

    }

    /*
    public double getLeftDistance() {
        return leftDisSensor.getDistance();
    }

    public double getRightDistance() {
        return rightDisSensor.getDistance();
    }

     */

    public String getLocation() {
        return Math.round(positionLocation.x) + ", " +
                Math.round(positionLocation.y) + ", " +
                Math.round(positionLocation.angle);
    }


    public Location getLocationClass() {
        return positionLocation;
    }

    public boolean atTarget() {
        return positionLocation.compareHeading(targetLocation, angleOffset) &&
                                        positionLocation.distanceBetween(targetLocation) < cmOffset;
    }

    private boolean compare(double a, double b, double offset) {
        return Math.abs(a - b) <= offset;
    }

    public int getCurrentLeftPos() {
        return currentLeftPos;
    }

    public int getCurrentRightPos() {
        return currentRightPos;
    }

    public int getCurrentAuxPos() {
        return currentAuxPos;
    }

    public direction getCurrentMovement() {
        return currentMovement;
    }

    public String getTargetLocation() {
        return Math.round(targetLocation.x) + " cm , " +
                Math.round(targetLocation.y) + " cm , " +
                Math.round(targetLocation.angle) + "°";
    }

    public boolean isMoving() {
        return moving;
    }
}