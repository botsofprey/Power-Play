package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import DriveEngine.MecanumDrive;
import Subsystems.Claw;
import Subsystems.Lift;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;
import UtilityClasses.Vector2D;

@TeleOp(name="Basic TeleOp", group="TeleOp")
public class MecanumTele extends LinearOpMode {

    private MecanumDrive drive;
    private Controller controller1, controller2;
    private Claw claw;
    private Lift lift;

    private threeWheelOdometry odometry;

    private final Location startLoc = new Location(4,0,4);

    private boolean slowModeOn = true;
    private boolean overrideDrivers = false;

    @Override
    public void runOpMode() {
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);
        drive = new MecanumDrive(hardwareMap, this, -90);
        claw = new Claw(hardwareMap);
        odometry = new threeWheelOdometry(hardwareMap, startLoc, this, drive);
        lift = new Lift(hardwareMap);

        while(!isStarted() && !isStopRequested()){
            controller2.update();

            if(controller2.rightTriggerHeld){
                lift.setPower(controller2.rightTrigger);
            } else if(controller2.leftTriggerHeld){
                lift.setPower(-controller2.leftTrigger);
            } else {
                lift.brake();
            }

            if(controller2.xPressed){
                lift.zeroLift();
            }
        }

        while(opModeIsActive()){
            //Checks for new inputs
           controller1.update();
           controller2.update();

            //Check that override can be stopped
            /*if(drive.getPower() == 0) {
                overrideDrivers = false;
            }*/

           if(controller1.upPressed){
               odometry.moveToOpenSpace(0,0,0, true);
               overrideDrivers = true;
           }

            if(controller1.leftPressed || controller2.leftPressed){
                odometry.next90degrees(-1);
                overrideDrivers = true;
            } else if(controller2.rightPressed || controller1.rightPressed){
                odometry.next90degrees(1);
                overrideDrivers = true;
            }

           if(controller1.startPressed){
               drive.slowMode();
               slowModeOn = !slowModeOn;
           }
           drive.slowMode(controller1.leftTriggerHeld && !slowModeOn);

        //Driver 1 uses joysticks for movement, duh
            if(!overrideDrivers){
                Vector2D leftInput = controller1.leftStick,
                         rightInput = controller1.rightStick;

            drive.moveTrueNorth(leftInput.y, -leftInput.x, rightInput.x);

            //Driver 1 controls claw
            if(controller1.aPressed){
                claw.setPosition(claw.getPosition() != Claw.CLOSE_POSITION ?
                        Claw.CLOSE_POSITION : Claw.OPEN_POSITION);
            }
            telemetry.addData("Claw Pos", claw.getPosition());

            //Driver 2 uses triggers to control lift
            if(controller2.rightTriggerHeld){
                lift.setPower(controller2.rightTrigger);
            } else if(controller2.leftTriggerHeld){
                lift.setPower(-controller2.leftTrigger);
            } else {
                lift.brake();
            }
            telemetry.addData("Lift Position", lift.getPosition());

            telemetry.addData("Powers", drive.getPowers());

            odometry.update();
            lift.update();

            telemetry.addData("Robot position", odometry.getLocation());
            telemetry.update();
        }
    }
}}
