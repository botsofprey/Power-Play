package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous
public class TestMovement extends LinearOpMode {

    private Location liftLoc = new Location(23, 1, 0);
    private final double tile = 2.54*24.0;
    Lift lift;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(-14,13.5), this, drive);
        lift = new Lift(hardwareMap);
        drive.setCurrentSpeed(1);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));

            telemetry.update();
        }

        odom.setTargetByOffset(liftLoc, new Location(tile/2., tile/2.), true);
        while(opModeIsActive()){
            odom.update();
        }

        while(opModeIsActive()){
            odom.rotateToAngle(0);
            while(!odom.atTargetAngle()){
                odom.update();
                telemetry.addData("Rotation", drive.getAngle());
                telemetry.addData("Rotation tar", odom.getTargetLocationClass().angle);
                telemetry.update();
            }
            drive.brake();
            sleep(1000);

            odom.rotateToAngle(180);
            while(!odom.atTargetAngle()){
                odom.update();
                telemetry.addData("Rotation", drive.getAngle());
                telemetry.addData("Rotation tar", odom.getTargetLocationClass().angle);
                telemetry.update();
            }
            drive.brake();
            sleep(1000);
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

            odom.setTargetPoint(tile,0,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            odom.setTargetPoint(tile,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }

            odom.setTargetPoint(0,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());;
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }
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
