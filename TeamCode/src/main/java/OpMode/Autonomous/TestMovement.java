package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Location;

@Autonomous
public class TestMovement extends LinearOpMode {

    private Location liftLoc = new Location(25, 2, 0);
    private final double tile = 2.54*24.0;
    Lift lift;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        //threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(-14,13.5), this, drive);
        lift = new Lift(hardwareMap);
        drive.setCurrentSpeed(.5);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));

            telemetry.update();
        }

        while(opModeIsActive()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.addData("Diff", odom.positionLocation.difference(new Location(tile/2., -tile/2.)).toString());
            telemetry.update();
        }

        while(opModeIsActive()){
            odom.setTargetPoint(0,0,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            sleep(5000);

            odom.setTargetPoint(tile,0,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            sleep(5000);

            odom.setTargetPoint(tile,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            sleep(5000);


            odom.setTargetPoint(0,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            sleep(5000);

        }

    }

    public String getLiftPos(threeWheelOdometry odom, MecanumDrive drive){
        Location posOfOff = new Location(
                (Math.cos(drive.getRadians()) * liftLoc.x) - (Math.sin(drive.getRadians()) * liftLoc.y),
                (Math.sin(drive.getRadians()) * liftLoc.x) + (Math.cos(drive.getRadians()) * liftLoc.y));
        posOfOff.add(odom.positionLocation);

        return Math.round(posOfOff.x) + " cm , " +
                Math.round(posOfOff.y) + " cm , " +
                Math.round(posOfOff.angle) + "°";
    }
}
