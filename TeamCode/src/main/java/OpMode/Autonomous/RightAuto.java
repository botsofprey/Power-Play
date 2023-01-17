package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
import Subsystems.Claw;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous (name="Right Auto", group = "Autonomous")
public class RightAuto extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Lift lift;
    private Claw claw;

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

        Controller con = new Controller(gamepad1);

        lift = new Lift(hardwareMap);
        claw = new Claw(hardwareMap);

        int parking = 1;

        drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(-10.5,13.5), this, drive);

        ReadWriteFile.writeFile(sideFile, "R");

        lift.setPower(-.25);
        while(!isStarted() && !isStopRequested() && !lift.isPressed()){
            updatePosition();
            lift.update();

            telemetry.addData("touch", lift.isPressed());
            telemetry.update();
        };
        lift.setPower(0);
        lift.zeroLift();

        while (!isStarted() && !isStopRequested()) {

            odometry.update();

            updatePosition();

            telemetry.addData("left", odometry.getCurrentLeftPos());
            telemetry.addData("right", odometry.getCurrentRightPos());
            telemetry.addData("aux", odometry.getCurrentAuxPos());
            telemetry.addData("other", odometry.getother());
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

            telemetry.addData("Parking", parking+1);
            telemetry.addData("P Loc", parkingLocations[parking].y);
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

        //Close around preset cone
        claw.setPosition(Claw.CLOSE_POSITION);
        sleep(1000);

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

            //Turns camera off
            camera.stop();

            //Sets target to parking spot
            parking = camera.getParking();

        }else{
            //Turns camera off
            camera.stop();

            //Sets target to parking spot
            parking = camera.getParking();

            //Scoring pre-loaded cone
            odometry.setTargetPoint(0, -tile, 0, true);
            lift.hjunction();
            whileMoving(1);
        }

        odometry.setTargetPoint(tile, -tile, -45, true);
        whileMoving(0);
        while(lift.isBusy() && opModeIsActive());

        drive.setCurrentSpeed(.25);
        odometry.setTargetPoint(71, -67, -41, true);
        whileMoving(0);

        //Scores
        lift.hjunctionScore();
        while(lift.isBusy() && opModeIsActive());

        sleep(500);
        claw.setPosition(Claw.OPEN_POSITION);
        sleep(500);

        //Getting in position to go to cone stack
        drive.setCurrentSpeed(.25);
        lift.coneStack(5);
        odometry.setTargetPoint(tile, -tile, -45, true);
        whileMoving(0);

        drive.setCurrentSpeed(.5);
        odometry.rotateToAngle(0);
        whileRotating(0);
        while(lift.isBusy() && opModeIsActive());

        odometry.setTargetPoint(tile*2, -tile, 0, true);
        whileMoving(0);

        odometry.rotateToAngle(90);
        whileRotating(0);

        //Scoring all cones
        int times = 0;
        int coneStack = 5;

        while(opModeIsActive() && times < 5){
            //Picks up cone
            lift.coneStack(coneStack);
            while(lift.isBusy() && opModeIsActive());

            odometry.setTargetPoint(125, 52, 92, true);
            whileMoving(0);

            claw.setPosition(Claw.CLOSE_POSITION);
            sleep(1000);

            //Backs away from cone stack before turning
            odometry.setTargetPoint(tile*2, 32, 92, true);
            whileMoving(0);

            //Turns toward high junction
            lift.hjunction();
            odometry.rotateToAngle(-45);
            whileRotating(0);

            //Scores
            odometry.setTargetPoint(tile*2,0,-45, true);
            whileMoving(0);

            lift.hjunctionScore();
            while(lift.isBusy());

            claw.setPosition(Claw.OPEN_POSITION);
            sleep(1000);

            odometry.setTargetPoint(134, -3, -46);
            lift.Ground();
            whileMoving(0);

            //Turns to cone stack
            odometry.rotateToAngle(90);
            whileRotating(0);

            times++;
            coneStack--;
        }

        lift.Ground();
        odometry.setTargetPoint(tile, 0, 90);
        whileMoving(0);

        odometry.setTargetPoint(parkingLocations[parking]);
        whileMoving(0);

        while(opModeIsActive()) {
            updatePosition();
        }
    }

    private void whileMoving(long secondsOfSleep){
        long startTime = System.currentTimeMillis();

        while(opModeIsActive() && !odometry.atTarget()) {
            telemetry.addData("FPS", 1000.0/(System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();

            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            
            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        drive.brake();
    }

    private void whileRotating(long secondsOfSleep){
        while(opModeIsActive() && !odometry.atTargetAngle()) {
            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            
            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        drive.brake();
    }

    private void updatePosition(){
        ReadWriteFile.writeFile(startFile, odometry.getLocation());
        System.out.println("Positions of bot: " + ReadWriteFile.readFile(startFile));
    }

    private double getTile(int numOfTiles){
        return (numOfTiles * tile) + (numOfTiles * 2.54);
    }
}

