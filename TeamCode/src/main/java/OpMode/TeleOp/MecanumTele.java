package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ClockWarningSource;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.util.Objects;

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
    private int liftPreset = 0;
    private int coneNum = 5;

    private threeWheelOdometry odometry;

    private Location startLoc = new Location(4,0,4);

    private boolean slowModeOn = true;
    private boolean overrideDrivers = false;
    private boolean scoring = false;

    private String filename = "TeleStartLocation.JSON";
    private File file = AppUtil.getInstance().getSettingsFile(filename);
    private String sideFileName = "AutoStartSide.JSON";
    private File sideFile = AppUtil.getInstance().getSettingsFile(sideFileName);

    private int negateScoring;

    @Override
    public void runOpMode() {
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);
        claw = new Claw(hardwareMap);
        lift = new Lift(hardwareMap);
        lift.zeroLift();
        claw.setPosition(Claw.CLOSE_POSITION);

        startLoc = settingStart(ReadWriteFile.readFile(file));
        drive = new MecanumDrive(hardwareMap, this, startLoc.angle);
        odometry = new threeWheelOdometry(hardwareMap, startLoc, this, drive);

        negateScoring = Objects.equals(ReadWriteFile.readFile(sideFile), "R") ? 1 : -1;

        odometry.setTargetPoint(startLoc);

        waitForStart();

        while (opModeIsActive()) {
            //Checks for new inputs
            controller1.update();
            controller2.update();

            //Check that override can be stopped
            if(overrideDrivers){
                if (odometry.atTarget() && !scoring || (controller1.bPressed || controller2.bPressed)) {
                    overrideDrivers = false;
                    odometry.cancelTarget();
                }
            }

            //Automatic scoring
            if(controller1.downPressed)
                scoring = !scoring;

            while(scoring && !(controller1.bPressed || controller2.bPressed)){
                odometry.setTargetPoint(0, -60 * negateScoring, 180 * negateScoring);
                whileMoving(1);

                odometry.setTargetPoint(60, -60 * negateScoring, -45 * negateScoring);
                whileMoving(1);
            }

            if (controller1.upPressed) {
                odometry.moveToOpenSpace(0, 0, 0, true);
                overrideDrivers = true;
            }

            if (controller1.leftPressed) {
                odometry.next90degrees(-1);
                overrideDrivers = true;
            } else if (controller1.rightPressed) {
                odometry.next90degrees(1);
                overrideDrivers = true;
            }

            if (controller1.startPressed) {
                drive.slowMode();
                slowModeOn = !slowModeOn;
            } else {
                drive.slowMode(controller1.leftTriggerHeld && !slowModeOn);
            }

            //Driver 1 uses joysticks for movement, duh
            if (!overrideDrivers) {
                Vector2D leftInput = controller1.leftStick,
                        rightInput = controller1.rightStick;

                double newForward = controller1.leftStick.y;
                double newRight = controller1.leftStick.x;
                double rotate = controller1.rightStick.x;
                drive.moveWithPower(
                        newForward + newRight + rotate,
                        newForward - newRight + rotate,
                        newForward + newRight - rotate,
                        newForward - newRight - rotate
                );

                 /*
                drive.moveTrueNorth(leftInput.y, leftInput.x, rightInput.x);*/
            }


            //Driver 1 controls claw
            if(controller1.aPressed){
                claw.setPosition(claw.getPosition() != Claw.CLOSE_POSITION ?
                        Claw.CLOSE_POSITION : Claw.OPEN_POSITION);
            }
            telemetry.addData("Claw Pos", claw.getPosition());

            // Changes lift preset once bumper pressed (may change to a different button)
            if (controller2.rightBumperPressed) {
                liftPreset += 1;
            }
            if (controller2.leftBumperPressed) {
                liftPreset -= 1;
            }

            //Driver 2 uses triggers to control lift
            if(liftPreset == 0) {
                if (controller2.rightTriggerHeld) {
                    lift.setPower(controller2.rightTrigger);
                } else if (controller2.leftTriggerHeld) {
                    lift.setPower(-controller2.leftTrigger);
                } else {
                    lift.brake();
                }
            }

            // Checks lift preset and sets the preset
            if(liftPreset != 0) {
                // If trigger is pressed, it will switch from presets to manual
                if (controller2.leftTriggerPressed || controller2.rightTriggerPressed) {
                    liftPreset = 0;
                }
                if (liftPreset == 1) {
                    lift.Ground();
                } else if (liftPreset == 2) {
                    lift.coneStack(coneNum);
                    if (controller2.upPressed) {
                        coneNum += 1;
                    } else if (controller2.downPressed) {
                        coneNum -= 1;
                    }
                } else if (liftPreset == 3) {
                    lift.ljunction();
                } else if (liftPreset == 4) {
                    lift.mjunction();
                } else if (liftPreset == 5) {
                    lift.hjunction();
                } else if (liftPreset == 6) {
                    liftPreset = 0;
                } else if (liftPreset == -1) {
                    liftPreset = 5;
                }
            }
            // Find positions of junctions
            /*
            int[][] lJunctions = {
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {}
            };
            int[][] mJunctions = {
                    {},
                    {},
                    {},
                    {},
            };
            int[][] hJunctions = {
                    {},
                    {},
                    {},
                    {}
            };

            if (controller1.yPressed) {

            }
            */

            telemetry.addData("Lift preset", liftPreset);

            telemetry.addData("Cones", coneNum);

            telemetry.addData("Lift Position", lift.getPosition());

            telemetry.addData("Powers", drive.getPowers());

            odometry.update();
            //lift.update();

            telemetry.addData("Robot position", odometry.getLocation());
            if(overrideDrivers) {
                if (!odometry.atTarget())
                    telemetry.addData("Target", odometry.getTargetLocation());
            }

            telemetry.update();
        }
    }

    private Location settingStart(String lString){
        String[] pos = lString.split(", ");

        return new Location(
                Double.parseDouble(pos[0]),
                Double.parseDouble(pos[1]),
                Double.parseDouble(pos[2])
        );
    }

    private void whileMoving(long secondsOfSleep){
        while(opModeIsActive() && !odometry.atTarget()) {
            odometry.update();

            controller1.update();
            controller2.update();

            telemetry.addData("Target", odometry.getTargetLocation());
            telemetry.addData("Position", odometry.getLocation());
            telemetry.addData("Current State", odometry.getCurrentMovement());
            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        drive.brake();
        sleep(1000 * secondsOfSleep);
    }
}
