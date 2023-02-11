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
import UtilityClasses.Location;

@TeleOp
public class TestWiring extends LinearOpMode {
    AverageDistanceSensor forward; //3
    AverageDistanceSensor horizontal; //2

    private final Location fOffset = new Location(-18.5, 1);
    private final Location hOffset = new Location(-11,15);

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Location predicttedLocation;

    @Override
    public void runOpMode() throws InterruptedException {
        forward = new AverageDistanceSensor(hardwareMap.get(DistanceSensor.class, "backDis"), DistanceUnit.CM, 50);
        horizontal =  new AverageDistanceSensor(hardwareMap.get(DistanceSensor.class, "horiDis"), DistanceUnit.CM, 50);

        drive = new MecanumDrive(hardwareMap, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        waitForStart();

        while(opModeIsActive()){
            forward.update();
            horizontal.update();
            calculatePosition();
            odometry.update();

            telemetry.addData("back Distance", forward.getDistance());
            telemetry.addData("side Distance", horizontal.getDistance());
            telemetry.addLine();

            telemetry.addData("Actual Position", odometry.positionLocation.toString(4));
            telemetry.addData("Guessed Position", predicttedLocation.toString(4));

            telemetry.update();
        }


    }

    private void calculatePosition(){
        double back = forward.getDistance(), side = horizontal.getDistance(),
        curRadians = drive.getRadians();
        Location posOfBack = new Location((back * Math.cos(curRadians)) - fOffset.x * Math.cos(curRadians),
                                            (back * Math.sin(curRadians)) - fOffset.y * Math.sin(curRadians)),
            posOfSide = new Location((side * Math.sin(curRadians)) - hOffset.x * Math.sin(curRadians),
                                    (side * Math.cos(curRadians)) - hOffset.y * Math.cos(curRadians));

        predicttedLocation = new Location(posOfBack.x + posOfSide.x, posOfSide.y + posOfBack.y);
    }
}
