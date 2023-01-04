package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;


import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
import Subsystems.Claw;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous (name="Left Auto", group = "Autonomous")
public class LeftAuto extends LinearOpMode {

    private MecanumDrive drive;
    private threeWheelOdometry odometry;

    private Lift lift;
    private Claw claw;

    private Location[] parkingLocations = {
            new Location(60, -60), //parking spot 1
            new Location(60, 0), //2
            new Location(60, 60), //3
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

        int parking = 3;

        drive = new MecanumDrive(hardwareMap, this, 0);
        odometry = new threeWheelOdometry(hardwareMap, new Location(-6,0), this, drive);

        ReadWriteFile.writeFile(sideFile, "L");

        lift.setPower(-.25);
        while(!isStarted() && !isStopRequested() && !lift.isPressed()){
            updatePosition();
            lift.update();

            telemetry.addData("touch", lift.isPressed());
            telemetry.addData("touch weight", lift.getTouchValue());
            telemetry.update();
        };
        lift.zeroLift();
        lift.setPower(0);

        claw.setPosition(Claw.CLOSE_POSITION);

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


/*
        double fps=0, frameCounter = 0;
        double startTime = System.currentTimeMillis();

        odometry.setTargetPoint(new Location(60, 0));

        while(opModeIsActive()){
            telemetry.addData("Positon", odometry.getLocation());
            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addLine();

            telemetry.addData("x movement", odometry.x);
            telemetry.addData("y movement", odometry.y);
            telemetry.addData("angle of movement", Math.toDegrees(odometry.h));
            telemetry.update();

            if(System.currentTimeMillis() - startTime >= 1){
                fps = frameCounter;
                frameCounter = 0;
                startTime = System.currentTimeMillis();
            }
            frameCounter++;

            if(fps == 0)
                continue;

            con.update();
            double deltaTime = 1/fps;

            Vector2D movement = con.leftStick;

            odometry.positionLocation.add(movement.x * deltaTime, movement.y * deltaTime, 0);
            odometry.update();
        }

*/

        //Resets position and encoders
        odometry.resetEncoders();

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

        //Scoring pre-loaded cone
        odometry.setTargetPoint(0, 60, 0);
        whileMoving(2);

        lift.hjunction();
        odometry.setTargetPoint(60, 60, 45);
        whileMoving(1);

        claw.setPosition(claw.OPEN_POSITION);
        sleep(2500);

        //Getting in position to go to cone stack
        odometry.setTargetPoint(60, 60, -90);
        whileMoving(2);

        odometry.setTargetPoint(new Location(120, 60, -90));
        whileMoving(1);

        //Scoring all cones
        int times = 0;
        int coneStack = 5;

        while(opModeIsActive() && times < 5){
            //Picks up cone
            lift.coneStack(coneStack);
            odometry.setTargetPoint(new Location(120, -60, -90));
            whileMoving(2);

            claw.setPosition(Claw.CLOSE_POSITION);
            sleep(2500);

            //Backs away from cone stack before turning
            odometry.setTargetPoint(120, -50, -90);
            whileMoving(0);

            //Turns toward high junction
            lift.hjunction();
            odometry.setTargetPoint(120, -60, 45);
            whileMoving(0);

            //Scores
            odometry.setTargetPoint(120,0,45);
            whileMoving(2);

            claw.setPosition(Claw.OPEN_POSITION);
            sleep(2500);

            //Turns to cone stack
            odometry.setTargetPoint(120, 0, -90);
            whileMoving(0);

            times++;
            coneStack--;
        }

        lift.Ground();
        odometry.setTargetPoint(60, 0, -90);
        whileMoving(1);

        odometry.setTargetPoint(parkingLocations[parking]);
        whileMoving(1);

        while(opModeIsActive()) {
            updatePosition();
        }
    }

    private void whileMoving(long secondsOfSleep){
        while(opModeIsActive() && !odometry.atTarget()) {
            odometry.update();

            updatePosition();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        drive.brake();
        sleep(1000 * secondsOfSleep);
    }

    private void updatePosition(){
        ReadWriteFile.writeFile(startFile, odometry.getLocation());
        System.out.println("Positions of bot: " + ReadWriteFile.readFile(startFile));
    }
}

