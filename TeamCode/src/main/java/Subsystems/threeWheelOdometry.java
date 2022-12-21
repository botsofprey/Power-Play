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

    private Location positionLocation, targetLocation;

    private AverageDistanceSensor leftDisSensor, rightDisSensor;
    private DistanceUnit distanceUnit = DistanceUnit.CM;

    private boolean moving = false, maintain = false;

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
    public final static double DISTANCE_FROM_MIDPOINT = 6.8;
    public final static double LENGETH_BETWEEN_VERTS = 29.2, LEFT_DISTANCE_FROM_MID = 13.5, RIGHT_DISTANCE_FROM_MID = 10.4;
    public final static double ANGLE_CIRCUMFERENCE = DISTANCE_FROM_MIDPOINT * Math.PI * 2;
    public final static double CM_PER_TICK = (3.5 * Math.PI) / 1977;

    public threeWheelOdometry(HardwareMap hardwareMap, Location start, LinearOpMode op, MecanumDrive drive) {
        meccanumDrive = drive;

        leftDisSensor = new AverageDistanceSensor(
                hardwareMap.get(DistanceSensor.class, "leftDistance"),
                DistanceUnit.CM,
                25);
        rightDisSensor = new AverageDistanceSensor(
                hardwareMap.get(DistanceSensor.class, "rightDistance"),
                DistanceUnit.CM,
                25);

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
/*
        double angleDiff = meccanumDrive.getAngle() - positionLocation.angle;
        positionLocation.angle = meccanumDrive.getAngle();
        double theta = Math.toRadians(meccanumDrive.getAngle()),
                x = (dx1 * Math.cos(theta)) - (dy * Math.sin(theta)),
                y = (dx1 * Math.sin(theta)) + (dy * Math.cos(theta));
        positionLocation.add(x * CM_PER_TICK, y * CM_PER_TICK, 0);
        positionLocation.angle = meccanumDrive.getAngle();


        */
        double //theta = Math.toRadians((dx2-dx1)/(15.24*2)),
                theta = Math.toRadians(meccanumDrive.getRadians() - positionLocation.getRadians()),
                fwd = ((dx2 * LEFT_DISTANCE_FROM_MID) - (dx1 * RIGHT_DISTANCE_FROM_MID)) / LENGETH_BETWEEN_VERTS,
                str = dy - (7.5 * theta); //100

        if (theta != 0) {

            double r0 = fwd / theta,
                    r1 = str / theta;
            System.out.println("COORDS: " + theta + " : " + str + " : " + r1);

            double relX = (r0 * Math.sin(theta)) - (r1 * (1 - Math.cos(theta))),
                    relY = (r1 * Math.sin(theta)) + (r0 * (1 - Math.cos(theta)));

            double angle = meccanumDrive.getRadians();
            double deltaX = (relX * Math.cos(angle)) - (relY * Math.sin(angle)),
                    deltaY = (relY * Math.cos(angle)) + (relX * Math.sin(angle));

            System.out.println(deltaX + ", " + -deltaY);
            positionLocation.add(deltaX * CM_PER_TICK, deltaY * CM_PER_TICK, Math.toDegrees(theta));
        } else {
            double relX = fwd, //500
                    relY = str; //100

            double angle = meccanumDrive.getRadians();//0
            double deltaX = (relX * Math.cos(angle)) - (relY * Math.sin(angle)), //500
                    deltaY = (relY * Math.cos(angle)) + (relX * Math.sin(angle)); //100

            System.out.println(deltaX + ", " + deltaY);
            positionLocation.add(deltaX * CM_PER_TICK, deltaY * CM_PER_TICK, Math.toDegrees(theta));
        }
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
        moveTowards(positionLocation.difference(targetLocation));
    }

    //Rounds robot's rotation to the nearest 90 degrees and adds/subtracts 90
    public void next90degrees(int negPos) {
        Location target = new Location(positionLocation.x, positionLocation.y);

        if (negPos != -1 && negPos != 1)
            return;

        double angle = meccanumDrive.getAngle() + (negPos * 90),
                abs = Math.abs(angle);

        if (abs > 0 && abs <= 45) {
            angle = 0;
        } else if (abs > 45 && abs <= 135) {
            angle = 90;
        } else if (abs > 135 && abs <= 225) {
            angle = 180;
        } else {
            angle = 270;
        }

        angle *= negPos;
        target.angle = angle;
        setTargetPoint(target);
    }

    //Robot move towards target
    private void moveTowards(Location diff) {
        /*
        if((!compare(diff.x, 0, 10) && currentMovement != direction.y && currentMovement != direction.angle) ||
            !compare(diff.x, 0, 20)){
            double power = movePID.calculateResponse(diff.x)/2;
            meccanumDrive.moveWithPower(power);

            if(currentMovement != direction.x){
                System.out.println("Current state change: x at " + getLocation());
            }
            currentMovement = direction.x;

        }else if((!compare(diff.y, 0, 10) && currentMovement != direction.x && currentMovement != direction.angle) ||
                    !compare(diff.y, 0, 20)){
            double power = movePID.calculateResponse(diff.y)/2;
            meccanumDrive.moveWithPower(power, -power, power, -power);

            if(currentMovement != direction.y){
                System.out.println("Current state change: y at " + getLocation());
            }
            currentMovement = direction.y;

        }else if ((!compare(diff.angle, 0, 10) && currentMovement != direction.y && currentMovement != direction.x) ||
                  !compare(diff.angle, 0, 20)){
            double power = headingPID.calculateResponse(diff.angle);
            power = Range.clip(power, -1, 1);

            meccanumDrive.moveWithPower(-power,-power,power,power);
            if(currentMovement != direction.angle){
                System.out.println("Current state change: angle at " + getLocation());
            }
            currentMovement = direction.angle;
        } else {
            currentMovement = direction.stationary;
            moving = false;
            meccanumDrive.brake();
        }

         */
        double x, y, h;

        double robotMovementAngle = Math.atan2(diff.y, diff.x);
        h = robotMovementAngle - meccanumDrive.getRadians();

        x = Math.cos(h);// * movePID.calculateResponse(diff.x);
        y = Math.sin(h);//  * movePID.calculateResponse(diff.y);
        double rotate = positionLocation.compareHeading(targetLocation, 10) ?
                0 : headingPID.calculateResponse(diff.angle);

        if (Math.abs(diff.x) < 5 && Math.abs(diff.y) < 5) {
            x = 0;
            y = 0;
        }

        meccanumDrive.moveWithPower(
                x + y + rotate, //
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
        currentRightPos = meccanumDrive.motors[0].getCurrentPosition();
        currentLeftPos = meccanumDrive.motors[3].getCurrentPosition();
        currentAuxPos = meccanumDrive.motors[2].getCurrentPosition();
        other = meccanumDrive.motors[1].getCurrentPosition();

        //Update current point values
        calculateChange();

        leftDisSensor.update();
        rightDisSensor.update();

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
        meccanumDrive.motors[3].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        meccanumDrive.motors[0].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        meccanumDrive.motors[1].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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

    public double getLeftDistance() {
        return leftDisSensor.getDistance();
    }

    public double getRightDistance() {
        return rightDisSensor.getDistance();
    }

    public String getLocation() {
        return Math.round(positionLocation.x) + " cm, " +
                Math.round(positionLocation.y) + " cm, " +
                Math.round(positionLocation.angle) + "°";
    }

    public boolean atTarget() {
        return positionLocation.compareAll(targetLocation, 15, 10);
    }

    private boolean compare(double a, double b, double offset) {
        return Math.abs(a - b) <= offset;
    }

    private boolean equalAndNotZero(double a, double b) {
        return compare(a, b, 2.5) && a != 0;
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