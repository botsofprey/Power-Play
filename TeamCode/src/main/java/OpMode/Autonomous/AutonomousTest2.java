package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous (name="Auto Test2", group = "Autonomous")
public class AutonomousTest2 extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Location[] parkingLocations = {
            new Location(60, -60), //parking spot 1
            new Location(60, 0), //2
            new Location(60, 60), //3
            new Location(0, 70) //terminal, when qr code is not found
    };

    private Location[] coneLocations = {
            new Location(120, 0, 0), //cone park location 1 (starting location)
            new Location(120, 0, 90), //cone park location 2 (turns to face cone)
            new Location(120, 60), //cone park location 3 (where the cones are at)
            new Location(120, 0, -45) //cone park location 4 (facing large pole)
    };

    public void setconelocation(int x) {
        odometry.setTargetPoint(coneLocations[x]);
        while (!odometry.atTarget())
            odometry.update();
        drive.brake();
        sleep(1000);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //CameraPipeline cameraPipeline = new CameraPipeline();
        // Camera camera = new Camera(hardwareMap, cameraPipeline);
        AprilTagCamera camera = new AprilTagCamera(hardwareMap);
        drive = new MecanumDrive(hardwareMap, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        Controller con = new Controller(gamepad1);

        int parking = 3;

        //drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(19,13), this, drive);

        while (!isStarted() && !isStopRequested()) {
            //odometry.update();
            //telemetry.addData("left", odometry.getCurrentLeftPos());
            // telemetry.addData("right", odometry.getCurrentRightPos());
            //telemetry.addData("aux", odometry.getCurrentAuxPos());
            //telemetry.addData("Position", odometry.getLocation());
            telemetry.addLine();


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

        camera.stop();

        //Sets target to parking spot
        parking = camera.getParking();
        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget()){
            odometry.update();
            //drive.update();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
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
            for (int i = 0; i < 3; i++) {
                setconelocation(i);
            }

            //pick up
            setconelocation(0);
            setconelocation(3);

            //Lift, drop, reset lift
            conesDropped++;
            sleep(1000);
        }

        odometry.setTargetPoint(parkingLocations[parking]);
        while(opModeIsActive() && !odometry.atTarget())
            odometry.update();


    }
}
