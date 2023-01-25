package Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.Mat;

import DriveEngine.MecanumDrive;
import UtilityClasses.Location;
import UtilityClasses.PidController;
import UtilityClasses.StigmoidController;

public class threeWheelOdometry {

    private LinearOpMode opMode;

    private MecanumDrive meccanumDrive;

    //private DcMotor leftVert, rightVert, horizontal, other;
    private int currentLeftPos = 0, currentRightPos = 0, currentAuxPos = 0, other;
    private int prevLeftPos = 0, prevRightPos = 0, prevAuxPos = 0;

    public Location positionLocation;
    private Location targetLocation;
    private Location start, startLoc;

    //private AverageDistanceSensor leftDisSensor, rightDisSensor;
    private DistanceUnit distanceUnit = DistanceUnit.CM;

    private boolean moving = false, maintain = false, rotateOnly = false;
    private final double cmOffset = 1, angleOffset = 5;
    private final double slowDownOffset = 30;

    public PidController xPID, yPID, headingPID;
    private final double moveI = 0, maintainI = 0.005;
    private final double moveD = 0, maintainD = .25;// 0.001;
    private final double headingI = 0, maintainHeadingI = 0.00001;

    private StigmoidController stigmoidController = new StigmoidController(1, 7.9/10.0, 0);

    //Robot measurements in mm
    public final static double DISTANCE_FROM_MIDPOINT = 18.5;
    public final static double LENGETH_BETWEEN_VERTS = 42;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * Math.PI * 2;
    public final static double CM_PER_TICK = (3.5 * Math.PI) / 8192;
    public final static double xMult = 209.0/200.0,//205.0/200.0,
            yMult = 209.0/200.0;//205.0/200;

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
        startLoc = start;

        this.opMode = op;

        xPID = new PidController(.1, moveI, moveD);
        yPID = new PidController(.1, moveI, moveD);
        headingPID = new PidController(.75, headingI, .125);
    }

    private void calculateChange() {
        double dx1 = (currentLeftPos - prevLeftPos) * CM_PER_TICK,
                dx2 = (currentRightPos - prevRightPos) * CM_PER_TICK,
                dy = (currentAuxPos - prevAuxPos) * CM_PER_TICK;

        double theta = (dx2-dx1)/(LENGETH_BETWEEN_VERTS),
                fwd = (dx2+dx1)/2.0,
                str = dy - (DISTANCE_FROM_MIDPOINT * theta);

        double angle = meccanumDrive.getRadians();
        double deltaX = (fwd * Math.cos(angle)) - (str * Math.sin(angle)),
                deltaY = (str * Math.cos(angle)) + (fwd * Math.sin(angle));

        positionLocation.add(deltaX * xMult, deltaY * yMult,0);
        positionLocation.angle = meccanumDrive.getAngle();

        //dx = x change
        //dy = y change
    }

    //Sets target
    public void setTargetPoint(Location target) {
        targetLocation = target;
        moving = true;

        xPID.reset();
        yPID.reset();
        headingPID.reset();
        xPID.updateCoefficients(xPID.kp, moveI, moveD);
        yPID.updateCoefficients(yPID.kp, moveI, moveD);
        headingPID.updateCoefficients(headingPID.kp, headingI, headingPID.kd);

        start = positionLocation;
        moveTowards(positionLocation.difference(targetLocation));
    }

    public void setTargetPoint(Location target, boolean maintain){
        rotateOnly = false;
        this.maintain = maintain;
        setTargetPoint(target);
    }

    public void setTargetPoint(double x, double y, double h){
        rotateOnly = false;
        setTargetPoint(new Location(x, y, h));
    }

    public void setTargetPoint(double x, double y, double h, boolean maintain){
        rotateOnly = false;
        this.maintain = maintain;
        setTargetPoint(new Location(x, y, h));
    }

    public void rotateToAngle(double angle){
        rotateOnly = true;
        maintain = false;
        setTargetPoint(new Location(positionLocation.x, positionLocation.y, angle));
    }

    int time = 0;
    public void setTargetByOffset(Location offset, Location target, boolean maintain){
        this.maintain = maintain;

        //Finds offset position after turn
        Location posOfOff = new Location(
                (Math.cos(meccanumDrive.getRadians()) * offset.x) - (Math.sin(meccanumDrive.getRadians()) * offset.y),
                (Math.sin(meccanumDrive.getRadians()) * offset.x) + (Math.cos(meccanumDrive.getRadians()) * offset.y));
        posOfOff.add(positionLocation);

        //Calculates differences of offset and target
        Location targetDiff = posOfOff.difference(target);

        //Sets target
        Location tar = new Location(positionLocation.x + targetDiff.x,
                positionLocation.y + targetDiff.y,
                meccanumDrive.getAngle());

        time++;
        System.out.println("Times target offset: " + time);
        setTargetPoint(tar);
    }

    public void cancelTarget() {
        targetLocation = null;
        moving = false;
    }

    //Rounds robot's rotation to the nearest 90 degrees and adds/subtracts 90
    public void next90degrees(int negPos) {

        if (negPos != -1 && negPos != 1)
            return;

        double angle = meccanumDrive.getAngle() + (negPos * 90);

        angle = Math.round(angle/90) * 90;

        rotateToAngle(angle);
    }

    public double x, y, h;

    //Robot move towards target
    private void moveTowards(Location diff) {
        if(!rotateOnly){
            //Calculates needed movement angle
            double robotMovementAngle = Math.atan2(diff.y, diff.x); //angle of movement
            h = robotMovementAngle - meccanumDrive.getRadians(); //angle of movement relative to robot's current angle

            double distance = positionLocation.distanceBetween(targetLocation);

            if(distance <= slowDownOffset) {
                if(xPID.kd == moveD){
                    xPID.updateCoefficients(xPID.kp, maintainI, maintainD);
                    yPID.updateCoefficients(yPID.kp, maintainI, maintainD);
                    headingPID.updateCoefficients(yPID.kp, maintainHeadingI, yPID.kd);
                    xPID.reset();
                    yPID.reset();
                    headingPID.reset();

                    System.out.println("Undershot and had to reset");
                }

                x = xPID.calculateResponse(distance * Math.cos(h));
                y = yPID.calculateResponse(distance * Math.sin(h));


                //x *= distance / slowDownOffset;
                //y *= distance / slowDownOffset;
            }else{
                if(xPID.kd == maintainD){
                    xPID.updateCoefficients(xPID.kp, moveI, moveD);
                    yPID.updateCoefficients(yPID.kp, moveI, moveD);;
                    headingPID.updateCoefficients(yPID.kp, headingI, yPID.kd);
                    xPID.reset();
                    yPID.reset();
                    headingPID.reset();

                    System.out.println("Overshot and had to reset");
                }

                x = xPID.calculateResponse(distance * Math.cos(h));
                y = yPID.calculateResponse(distance * Math.sin(h));
            }

            //x = (1./.44) * (stigmoidController.calculateResponse(x) - .562);
            // y = (1./.44) * (stigmoidController.calculateResponse(y) - .562);

            double length = Math.hypot(x, y);
            if(length > 1){
                x /= length;
                y /= length;
            }

        } else{
            x = 0;
            y = 0;
        }


        double rotate = Math.abs(diff.angle) < 5 ?
                0 : headingPID.calculateResponse(diff.angle);
        rotate = Range.clip(rotate, -.5, .5);

        meccanumDrive.moveWithPower(
                x + y + rotate,
                x - y + rotate,
                x + y - rotate,
                x - y - rotate
        );
    }

    public void update(boolean move) {

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
            if(move){
                moveTowards(diff);
            }

        } else if (moving || maintain) {
            moving = false;
            rotateOnly = false;
            meccanumDrive.brake();
        }
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
        if ((moving || maintain) && (!atTarget() || !atTargetAngle())) {
            Location diff = positionLocation.difference(targetLocation);
            moveTowards(diff);

        } else if (moving || maintain) {
            moving = false;
            meccanumDrive.brake();
        }
    }

    public void setRotateOnly(boolean t){
        rotateOnly = t;
    }

    public void setMaintain(boolean t){
        maintain = t;
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

        positionLocation = startLoc;
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
        return targetLocation == null || positionLocation.distanceBetween(targetLocation) < cmOffset && positionLocation.compareHeading(targetLocation, angleOffset);
    }

    public boolean atTargetAngle(){
        return rotateOnly && positionLocation.compareHeading(targetLocation, angleOffset);
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

    public String getTargetLocation() {
        return Math.round(targetLocation.x) + " cm , " +
                Math.round(targetLocation.y) + " cm , " +
                Math.round(targetLocation.angle) + "°";
    }

    public boolean isMoving() {
        return moving;
    }

    public void changeStartLocation(Location s){
        startLoc = s;
    }

    public Location getTargetLocationClass() {
        return targetLocation;
    }
}