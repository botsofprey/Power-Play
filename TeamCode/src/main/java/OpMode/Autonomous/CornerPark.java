package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import DriveEngine.MecanumDrive;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Location;

@Autonomous(name="lord", group = "Autonomous")
public class CornerPark extends LinearOpMode {
    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    public void runOpMode (){
        drive = new MecanumDrive(hardwareMap,0);
        odometry = new threeWheelOdometry (hardwareMap, new Location(0,0), this, drive);

        if(opModeIsActive()){
            drive.moveCenti(65, MecanumDrive.FORWARD);
            while(opModeIsActive() && drive.isBusy()
            ) {

            }
            sleep(5000);
        }
    }
}

