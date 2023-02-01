package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
import Subsystems.Claw;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Location;

@Autonomous (name="Only Parking Auto", group = "Autonomous")
public class ParkingAuto extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private final double tile = 2.54*24.0;
    private Location[] parkingLocations = {
            new Location(tile, -tile), //parking spot 1
            new Location(tile, 0), //2
            new Location(tile, tile), //3
            new Location(0, 90) //terminal, when qr code is not found
    };

    private String startFileName = "TeleStartLocation.JSON";
    private File startFile = AppUtil.getInstance().getSettingsFile(startFileName);
    private String sideFileName = "AutoStartSide.JSON";
    private File sideFile = AppUtil.getInstance().getSettingsFile(sideFileName);


    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        int parking = 1;

        drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(-10, 13.5, 90), this, drive);

        ReadWriteFile.writeFile(sideFile, "R");

        while (!isStarted() && !isStopRequested()) {

            odometry.update();

            updatePosition();

            telemetry.addData("Position", odometry.getLocation());
            telemetry.addLine();

            telemetry.addData("Parking", parking + 1);
            telemetry.addData("P Loc", parkingLocations[parking].y);

            telemetry.addData("Tag found", camera.tagFound());
            if (camera.tagFound()) {
                telemetry.addData("Parking", camera.getParking() + 1);
            }

            telemetry.addLine();

            telemetry.update();
        }


        //Resets position and encoders
        odometry.resetEncoders();

        odometry.setTargetPoint(0,0,0);
        while(opModeIsActive() && !odometry.atTarget()) {
            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());

            telemetry.addData("left", odometry.getCurrentLeftPos());
            telemetry.addData("right", odometry.getCurrentRightPos());
            telemetry.addData("aux", odometry.getCurrentAuxPos());

            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        odometry.setTargetPoint(tile, 0, 0);
        while(opModeIsActive() && !odometry.atTarget()) {
            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());

            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        camera.stop();
        parking = camera.getParking();

        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget()) {
            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());

            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        while(opModeIsActive()){
            odometry.update();
            updatePosition();
        }
    }

    private void updatePosition(){
        ReadWriteFile.writeFile(startFile, odometry.getLocation());
        System.out.println("Positions of bot: " + ReadWriteFile.readFile(startFile));
    }
}
