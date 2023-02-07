package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Location;

@Autonomous
public class TestMovement extends LinearOpMode {

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

            telemetry.addLine();
            telemetry.addData("Left", odom.getCurrentLeftPos());
            telemetry.addData("Right", odom.getCurrentRightPos());
            telemetry.addData("Aux", odom.getCurrentAuxPos());

            telemetry.addLine();
            telemetry.addData("fl", drive.getCurrentPositionMotor(0));
            telemetry.addData("bl", drive.getCurrentPositionMotor(1));
            telemetry.addData("fr", drive.getCurrentPositionMotor(2));
            telemetry.addData("br", drive.getCurrentPositionMotor(3));

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
            drive.brake();

            sleep(5000);

            odom.setTargetPoint(tile,0,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }
            drive.brake();

            sleep(5000);

            odom.setTargetPoint(tile,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }
            drive.brake();

            sleep(5000);


            odom.setTargetPoint(0,-tile,0);
            while(!odom.atTarget()){
                odom.update();
                telemetry.addData("Position", odom.getLocation());
                telemetry.addData("X Integral", odom.xPidIntegralSum());
                telemetry.addData("Y Integral", odom.yPidIntegralSum());
                telemetry.update();
            }
            drive.brake();

            sleep(5000);

        }

    }

    public String getLiftPos(threeWheelOdometry odom, MecanumDrive drive){
        Location posOfOff = new Location(
                (Math.cos(drive.getRadians()) * lift.OFFSET_ON_BOT.x) - (Math.sin(drive.getRadians()) * lift.OFFSET_ON_BOT.y),
                (Math.sin(drive.getRadians()) * lift.OFFSET_ON_BOT.x) + (Math.cos(drive.getRadians()) * lift.OFFSET_ON_BOT.y));
        posOfOff.add(odom.positionLocation);

        return Math.round(posOfOff.x) + " cm , " +
                Math.round(posOfOff.y) + " cm , " +
                Math.round(posOfOff.angle) + "°";
    }
}
