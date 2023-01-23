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

    private Location liftLoc = new Location(23, 0);
    private final double tile = 2.54*24.0;

    @Override
    public void runOpMode() throws InterruptedException {
        Controller con = new Controller(gamepad1);

        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(-10.5,0), this, drive);
        Lift lift = new Lift(hardwareMap);
        drive.setCurrentSpeed(.5);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.update();
        }

        //odom.setTargetByOffset(liftLoc, new Location(tile/2., -tile/2.), true);
        odom.setTargetByOffset(liftLoc, new Location(tile/2., 0), true);

        while(opModeIsActive() && !odom.atTarget()){
            odom.update();
            telemetry.addData("Pos", odom.getLocation());
            telemetry.addData("Target", odom.getTargetLocation());
            telemetry.addData("left", odom.getCurrentLeftPos());
            telemetry.addData("right", odom.getCurrentRightPos());
            telemetry.addData("aux", odom.getCurrentAuxPos());
            telemetry.update();
        }

        while(opModeIsActive()){
            odom.update();
            telemetry.addData("Pos", odom.getLocation());
            telemetry.addData("Target", odom.getTargetLocation());
        }

    }
}
