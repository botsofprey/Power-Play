package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import DriveEngine.MecanumDrive;
import Subsystems.threeWheelOdometry;

@Autonomous(name="Auto Test2", group = "Autonomous")
public abstract class CornerPark extends LinearOpMode {
    private MecanumDrive drive;
    private threeWheelOdometry odometry;
    private DcMotor motor;
        public void moveCenti(double centimeters, String direction){


         drive.moveCenti(36, MecanumDrive.FORWARD);
    }}

