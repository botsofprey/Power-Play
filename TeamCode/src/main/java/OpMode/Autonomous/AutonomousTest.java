package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ControlHubApChannelManager;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
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
            new Location(60, 60), //parking spot 1
            new Location(60, 0), //2
            new Location(60, -60), //3
            new Location(0, 90) //terminal, when qr code is not found
    };

    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        Controller con = new Controller(gamepad1);

        int parking = 3;

        drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        while (!isStarted() && !isStopRequested()) {
            odometry.update();
            telemetry.addData("left", odometry.getCurrentLeftPos());
            telemetry.addData("right", odometry.getCurrentRightPos());
            telemetry.addData("aux", odometry.getCurrentAuxPos());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addLine();

            //For testing purposes, setting parking without needing cone
            con.update();
            if(con.xPressed){
                parking = 0;
            } else if(con.aPressed){
                parking = 1;
            } else if(con.bPressed){
                parking = 2;
            }
            telemetry.addData("Tag found", camera.tagFound());
            if(camera.tagFound()){
                telemetry.addData("Parking", camera.getParking()+1);
            }
            telemetry.addLine();

            //telemetry.addData("Right Distance", odometry.getRightDistance());
            //telemetry.addData("Left Distance", odometry.getLeftDistance());

            telemetry.update();
        }

        //Resets position and encoders
        odometry.resetEncoders();

        while(opModeIsActive());

        //If camera is too far away to see qr, robot gets closer
        if(!camera.tagFound()){
            odometry.setTargetPoint(parkingLocations[1]);

            while(!odometry.atTarget() && !camera.tagFound()){
                odometry.update();

                telemetry.addData("QR Code Found", camera.tagFound());
                if(camera.tagFound())
                    telemetry.addData("Parking Spot", camera.getParking()+1);
                telemetry.update();
            }
            drive.brake();
        }

        //Turns camera off
        camera.stop();

        //Sets target to parking spot
        parking = camera.getParking();
        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget()){
            odometry.update();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addData("Current State", odometry.getCurrentMovement());
            telemetry.addData("Powers", drive.getPowers());

            con.update();
            if(con.aPressed){
                System.out.println("Position of Bot: " + odometry.getLocation());
            }
            telemetry.update();
        }

        drive.brake();

        sleep(5000);

        while (opModeIsActive()){
            odometry.update();

            telemetry.addData("Position", odometry.getLocation());
            telemetry.addData("Parking Spot", camera.getParking()+1);
            telemetry.update();
        }

        ElapsedTime timer = new ElapsedTime();
        int conesDropped = 0;
        while(opModeIsActive() && timer.seconds() < 25 && conesDropped != 5){
            //odometry.setTargetPoint(Cone stack);
            while(!odometry.atTarget())
                odometry.update();
            sleep(1000);

            //pick up
            sleep(1000);

            //odometry.setTargetPoint(High Jun);
            while(!odometry.atTarget())
                odometry.update();
            sleep(1000);

            //Lift, drop, reset lift
            conesDropped++;
            sleep(1000);
        }

        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget())
            odometry.update();


    }
}
