package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.CameraPipeline;
import UtilityClasses.Camera;

@Autonomous (name="Auto Test", group = "Autonomous")
public class AutonomousTest extends LinearOpMode {

    private MecanumDrive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        CameraPipeline cameraPipeline = new CameraPipeline();
        Camera camera = new Camera(hardwareMap, cameraPipeline);

        drive = new MecanumDrive(hardwareMap, this, -90);

        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("QR Code", cameraPipeline.getParking());
            telemetry.addData("QR Code Link", cameraPipeline.getLink());
            telemetry.update();
        }
        camera.stop();
    }
}
