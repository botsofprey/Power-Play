package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.CameraPipeline;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Camera;
import UtilityClasses.Location;

@Autonomous (name="Auto Test", group = "Autonomous")
public class AutonomousTest extends LinearOpMode {

    private MecanumDrive drive;
    //private threeWheelOdometry odometry;

    @Override
    public void runOpMode() throws InterruptedException {
        CameraPipeline cameraPipeline = new CameraPipeline();
        Camera camera = new Camera(hardwareMap, cameraPipeline);

        //drive = new MecanumDrive(hardwareMap, this, -90);
        //odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this);

        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("QR Code", cameraPipeline.getParking());
            telemetry.addData("QR Code Link", cameraPipeline.getLink());
            telemetry.update();
        }
        camera.stop();

        while(opModeIsActive()){
            int parking = cameraPipeline.getParking();

            drive.moveWithPower(.5);
            sleep(2500);

            if(parking == 0){
                drive.moveWithPower(-.5, .5, -.5, .5);
                sleep(2500);
            }else if(parking == 1){

            }else if(parking == 2){
                drive.moveWithPower(.5, -.5, .5, -.5);
                sleep(2500);
            }

            telemetry.update();
        }
    }
}
