package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptCompassCalibration;

import java.util.ArrayList;

import DriveEngine.MecanumDrive;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous
public class TestMovement extends LinearOpMode {

    private final double tile = 2.54*24.0;
    Lift lift;

    private Location changeLift;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);
        //threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, threeWheelOdometry.autoStart, this, drive);
        lift = new Lift(hardwareMap);
        changeLift = lift.OFFSET_ON_BOT;
        drive.setCurrentSpeed(.5);

        Controller con = new Controller(gamepad1);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));

            con.update();
            if(con.downPressed)
                changeLift.x--;
            if(con.upPressed)
                changeLift.x++;
            if(con.leftPressed)
                changeLift.y--;
            if(con.rightPressed)
                changeLift.y++;
            telemetry.addData("Offset", changeLift);



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

        ArrayList<String> wayPoints = new ArrayList<>();
        while(opModeIsActive()){
            odom.update();
            telemetry.addData("Position", odom.getLocation());

            if(odom.positionLocation.angle - 90 < 1 ||
                odom.positionLocation.angle - 180 < 1 ||
                odom.positionLocation.angle + 90 < 1||
                odom.positionLocation.angle + 180 < 1){
                wayPoints.add(odom.getLocation());
            }

            for(String pt : wayPoints){
                telemetry.addData("y", pt);
            }

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
                (Math.cos(drive.getRadians()) * changeLift.x) - (Math.sin(drive.getRadians()) * changeLift.y),
                (Math.sin(drive.getRadians()) * changeLift.x) + (Math.cos(drive.getRadians()) * changeLift.y));
        posOfOff.add(odom.positionLocation);

        return Math.round(posOfOff.x) + " cm , " +
                Math.round(posOfOff.y) + " cm , " +
                Math.round(posOfOff.angle) + "°";
    }
}
