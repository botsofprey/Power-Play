package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
               //odometry.moveToOpenSpace(0,0,0, true);
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

                double newForward = controller1.leftStick.y;
                double newRight = -controller1.leftStick.x;
                double rotate = controller1.rightStick.x;
                drive.moveWithPower(
                        newForward + newRight + rotate,
                        newForward - newRight + rotate,
                        newForward + newRight - rotate,
                        newForward - newRight - rotate
                );
            //drive.moveTrueNorth(leftInput.y, -leftInput.x, rightInput.x);

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

            int liftPreset = 0;
            if(controller2.rightBumperPressed) {
                liftPreset = 1;
            }
            if(controller2.leftBumperPressed) {
                liftPreset = 5;
            }
            while(liftPreset != 0) {
                if(controller2.rightBumperPressed) {
                    liftPreset += 1;
                }
                if(controller2.leftBumperPressed) {
                    liftPreset -= 1;
                }
                if(controller2.leftTriggerPressed || controller2.rightTriggerPressed) {
                    liftPreset = 0;
                }
                if(liftPreset == 1) {
                    lift.Bottom();
                } else if(liftPreset == 2) {
                    lift.Quarter();
                } else if(liftPreset == 3) {
                    lift.Half();
                } else if(liftPreset == 4) {
                    lift.ThreeQuarters();
                } else if(liftPreset == 5) {
                    lift.Top();
                } else if(liftPreset == 6) {
                    liftPreset = 0;
                }
                telemetry.addData("Lift preset", liftPreset);
            }

            /*
            if(controller2.downPressed) {
                lift.Bottom();
            } else if(controller2.leftPressed) {
                lift.Quarter();
            } else if(controller2.upPressed) {
                lift.Half();
            } else if(controller2.rightPressed) {
                lift.Top();
            }
            */

            telemetry.addData("Lift Position", lift.getPosition());

            telemetry.addData("Powers", drive.getPowers());

            //odometry.update();
            lift.update();

            telemetry.addData("Robot position", odometry.getLocation());
            telemetry.update();
        }
    }
}}
