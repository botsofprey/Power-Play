package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ControlHubApChannelManager;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;

import DriveEngine.MecanumDrive;
import Subsystems.CameraPipeline;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Camera;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous (name="Auto Test", group = "Autonomous")
public class AutonomousTest extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Location[] parkingLocations = {
            new Location(30, 60), //parking spot 1
            new Location(30, 0), //2
            new Location(30, -60), //3
            new Location(0, -90) //terminal, when qr code is not found
    };

    @Override
    public void runOpMode() throws InterruptedException {
        CameraPipeline cameraPipeline = new CameraPipeline();
        Camera camera = new Camera(hardwareMap, cameraPipeline);

        int parking;

        drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("QR Code Found", cameraPipeline.getParking() != 3);
            if(cameraPipeline.getParking() != 3)
                telemetry.addData("Parking Spot", cameraPipeline.getParking()+1);
            odometry.update();
            telemetry.addData("left", odometry.getCurrentLeftPos());
            telemetry.addData("right", odometry.getCurrentRightPos());
            telemetry.addData("aux", odometry.getCurrentAuxPos());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addLine();

            telemetry.update();
        }
        camera.stop();


        parking = cameraPipeline.getParking();
        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget()){
            odometry.update();
            //drive.update();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addData("Current State", odometry.getCurrentMovement());
            telemetry.addData("Powers", drive.getPowers());
            telemetry.update();
        }

        sleep(5000);

        while (opModeIsActive()){
            odometry.update();

            telemetry.addData("Position", odometry.getLocation());
            telemetry.update();
        }
    }
}
