package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ControlHubApChannelManager;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;

import DriveEngine.MecanumDrive;
import Subsystems.CameraPipeline;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Camera;
import UtilityClasses.Location;

@Autonomous (name="Auto Test", group = "Autonomous")
public class AutonomousTest extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Location[] parkingLocations = {
            new Location(60, 60), //parking spot 1
            new Location(0, 60), //2
            new Location(-60, 60), //3
            new Location(-75, 0) //terminal, when qr code is not found
    };

    @Override
    public void runOpMode() throws InterruptedException {
        CameraPipeline cameraPipeline = new CameraPipeline();
        Camera camera = new Camera(hardwareMap, cameraPipeline);

        drive = new MecanumDrive(hardwareMap, this, -90);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this);

        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("QR Code Found", cameraPipeline.getParking() != 3);
            if(cameraPipeline.getParking() != 3)
                telemetry.addData("Parking Spot", cameraPipeline.getParking()+1);
            odometry.update();
            telemetry.addData("left", odometry.getCurrentLeftPos());
            telemetry.addData("right", odometry.getCurrentRightPos());
            telemetry.addData("aux", odometry.getCurrentAuxPos());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.update();
        }
        camera.stop();


        int parking = cameraPipeline.getParking();
        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget()){
            odometry.update();
            drive.update();

            telemetry.addData("Position", odometry.getLocation());
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
