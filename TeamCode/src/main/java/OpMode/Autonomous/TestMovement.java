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

    private Location liftLoc = new Location(20.5, 0);
    private final double tile = 2.54*24.0;

    @Override
    public void runOpMode() throws InterruptedException {
        Controller con = new Controller(gamepad1);

        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0,13.5), this, drive);
        Lift lift = new Lift(hardwareMap);
        drive.setCurrentSpeed(.5);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));
            telemetry.update();
        }

        //odom.setTargetByOffset(liftLoc, new Location(tile/2., -tile/2.), true);
        odom.setTargetByOffset(liftLoc, new Location(0, tile/2.), true);

        while(opModeIsActive() && !odom.atTarget()){
            odom.update();
            telemetry.addData("Pos", odom.getLocation());
            telemetry.addData("Target", odom.getTargetLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));
            telemetry.addData("left", odom.getCurrentLeftPos());
            telemetry.addData("right", odom.getCurrentRightPos());
            telemetry.addData("aux", odom.getCurrentAuxPos());
            telemetry.update();
        }

        while(opModeIsActive()){
            odom.update();
            telemetry.addData("Pos", odom.getLocation());
            telemetry.addData("Target", odom.getTargetLocation());
            telemetry.addData("Lift Pos", getLiftPos(odom, drive));
            telemetry.update();
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
