package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;
import UtilityClasses.Vector2D;

@Autonomous
public class TestMovement extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Controller con = new Controller(gamepad1);

        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0, 0), this, drive);
        drive.setCurrentSpeed(.5);

        while(!isStarted() && !isStopRequested()){
            odom.update();

            telemetry.addData("Position", odom.getLocation());
            telemetry.update();
        }


        while(opModeIsActive()){
            odom.update();
            telemetry.addData("Pos", odom.getLocation());
            telemetry.addData("left", odom.getCurrentLeftPos());
            telemetry.addData("right", odom.getCurrentRightPos());
            telemetry.addData("aux", odom.getCurrentAuxPos());
            telemetry.update();
        }


    }
}
