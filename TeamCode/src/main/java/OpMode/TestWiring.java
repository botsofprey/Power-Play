package OpMode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import DriveEngine.MecanumDrive;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.AverageDistanceSensor;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@TeleOp
public class TestWiring extends LinearOpMode {
    AverageDistanceSensor forward; //3
    AverageDistanceSensor horizontal; //2

    private final Location fOffset =new Location(0,0);// new Location(-18.5, 1);
    private final Location hOffset = new Location(0,0);//new Location(-11,15);

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Location predicttedLocation, posOfBack, posOfSide;

    @Override
    public void runOpMode() throws InterruptedException {
        forward = new AverageDistanceSensor(hardwareMap.get(DistanceSensor.class, "backDis"),
                DistanceUnit.CM, 50);
        horizontal =  new AverageDistanceSensor(hardwareMap.get(DistanceSensor.class, "horiDis"),
                DistanceUnit.CM, 100);

        drive = new MecanumDrive(hardwareMap, 0);
        drive.setCurrentSpeed(.5);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        Controller con = new Controller(gamepad1);
        predicttedLocation = new Location(0,0,0);

        waitForStart();

        while(opModeIsActive()){
            con.update();
            drive.moveWithPower(
                    con.leftStick.y + con.leftStick.x + con.rightStick.x,
                    con.leftStick.y - con.leftStick.x + con.rightStick.x,
                    con.leftStick.y + con.leftStick.x - con.rightStick.x,
                    con.leftStick.y - con.leftStick.x - con.rightStick.x
            );

            forward.update();
            horizontal.update();
            calculatePosition();
            odometry.update();

            telemetry.addData("back Distance", forward.getDistance());
            telemetry.addData("side Distance", horizontal.getDistance());
            telemetry.addLine();

            telemetry.addData("Actual Position", odometry.positionLocation.toString(4));
            telemetry.addData("Guessed Position", predicttedLocation.toString(4));
           // telemetry.addData("back", posOfBack.toString(4));
           // telemetry.addData("side", posOfSide.toString(4));

            telemetry.update();
        }


    }

    private void calculatePosition(){
        double back = forward.getDistance(), side = horizontal.getDistance(),
        curRadians = drive.getRadians();
        posOfBack = new Location((back * Math.cos(curRadians)),
                                            (back * Math.sin(curRadians)));
        posOfSide = new Location((side * Math.sin(curRadians)),
                                    (side * Math.cos(curRadians)));

        posOfBack.add(-(fOffset.x * Math.cos(curRadians)), -(fOffset.y * Math.sin(curRadians)), 0);
        posOfSide.add(-(hOffset.x * Math.sin(curRadians)), -(hOffset.y * Math.cos(curRadians)), 0);

        predicttedLocation = new Location(posOfBack.x + posOfSide.x, posOfSide.y + posOfBack.y);
    }

    private double currentBack, prevBack;
    private double currentSide, prevSide;
    private double currentTheta, prevTheta;
    private void calculateChange() {
        prevBack = currentBack;
        prevSide = currentSide;
        prevTheta = currentTheta;
        currentBack = forward.getDistance();
        currentSide = horizontal.getDistance();
        currentTheta = drive.getRadians();

        double dx = currentBack - prevBack,
                dy = currentSide - prevSide,
                dt = currentTheta - prevTheta;

        double deltaX = (dx * Math.cos(currentTheta)) - (dy * Math.sin(currentTheta)),
                deltaY = (dy * Math.cos(currentTheta)) + (dx * Math.sin(currentTheta));

        System.out.println("Back guess: " + currentBack + ", " + prevBack);
        System.out.println("Side guess: " + currentSide + ", " + prevSide);
        System.out.println("deltas guess: " + deltaX + ", " + deltaY);

        predicttedLocation.add(deltaX, deltaY,0);

        //dx = x change
        //dy = y change
    }
}
