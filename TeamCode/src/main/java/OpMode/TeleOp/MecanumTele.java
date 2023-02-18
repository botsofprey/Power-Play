package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
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
    private double[] closestJunction;

    private
    threeWheelOdometry odometry;

    private Location startLoc = new Location(4,0,4);

    private boolean slowModeOn = true;
    private boolean overrideDrivers = false, grid = false;
    private boolean scoring = false;

    private String filename = "TeleStartLocation.JSON";
    private File file = AppUtil.getInstance().getSettingsFile(filename);
    private String sideFileName = "AutoStartSide.JSON";
    private File sideFile = AppUtil.getInstance().getSettingsFile(sideFileName);

    private int negateScoring;

    private ElapsedTime endGameTimer;
    private boolean rumble = false;

    @Override
    public void runOpMode() {
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);
        claw = new Claw(hardwareMap);
        lift = new Lift(hardwareMap);
        lift.zeroLift();
        claw.setPosition(Claw.CLOSE_POSITION);

        endGameTimer = new ElapsedTime();

        startLoc = settingStart(ReadWriteFile.readFile(file));
        drive = new MecanumDrive(hardwareMap, this, startLoc.angle);
        odometry = new threeWheelOdometry(hardwareMap, startLoc, this, drive);

        negateScoring = Objects.equals(ReadWriteFile.readFile(sideFile), "R") ? 1 : -1;

        while(!isStarted() && !isStopRequested()){
            controller1.update();

            if(controller1.startPressed){
                odometry.changeStartLocation(new Location(0,0));
            }
        }

        endGameTimer.reset();

        while (opModeIsActive()) {
            //Checks for new inputs
            controller1.update();
            controller2.update();

            //Check that override can be stopped
            if(overrideDrivers){

                if (odometry.atTarget() && !scoring || odometry.atTargetAngle() && !scoring
                        || (controller1.bPressed || controller2.bPressed)) {
                    telemetry.addData("Drivers", "Overridden");
                    telemetry.addData("Press B to cancel", 0);
                    overrideDrivers = false;
                    odometry.cancelTarget();
                }
            }

            //Automatic scoring
            //if(controller1.downPressed)
            //    scoring = !scoring;

            //automatic scoring from substation to high junction
            while(scoring && !(controller1.bPressed || controller2.bPressed)){
                odometry.setTargetPoint(0, -60 * negateScoring, 180 * negateScoring);
                whileMoving(1);

                odometry.setTargetPoint(60, -60 * negateScoring, -45 * negateScoring);
                whileMoving(1);
            }

            //Turns the robot to the nearest next 90 degree
            if (controller1.leftPressed) {
                odometry.next90degrees(-1);
                overrideDrivers = true;
            } else if (controller1.rightPressed) {
                odometry.next90degrees(1);
                overrideDrivers = true;
            }

            //slows robot
            drive.slowMode(controller1.leftTriggerHeld);

            telemetry.addData("Slow mode", controller1.leftTriggerHeld);

            //Driver 1 uses joysticks for movement, duh
            if (!overrideDrivers) {
                // Robot-oriented movement unrestricted
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
            //drive.moveTrueNorth(leftInput.y, leftInput.x, rightInput.x);
            }


            //Driver 1 controls claw
            if(controller1.aPressed){
                claw.setPosition(claw.getPosition() != Claw.CLOSE_POSITION ?
                        Claw.CLOSE_POSITION : Claw.OPEN_POSITION);
                if(claw.getPosition()==Claw.OPEN_POSITION) {
                    controller1.rumble(1);
                    controller2.rumble(1);
                }
            }
            telemetry.addData("Claw Pos", claw.getPosition());

            // Changes lift preset once bumper pressed (may change to a different button)
            if (controller2.rightBumperPressed) {
                liftPreset += 1;
            }
            if (controller2.leftBumperPressed) {
                liftPreset -= 1;
            }

            //Driver 2 uses triggers to control manually lift
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
            double[][][] junctionPositions = {
            { // Low junctions
                    {30,-30},
                    {30,-150},
                    {90,30},
                    {90,-210},
                    {210,30},
                    {210,-210},
                    {270,-150},
                    {270,-30}
            },
            { // Medium Junctions
                    {90,-30},
                    {90,-150},
                    {210,-30},
                    {210,-150}
            },
            { // High junctions
                    {90,-90},
                    {150,-30},
                    {150,-150},
                    {210,-90}
            }};

            double[] robotPos = {odometry.positionLocation.x,odometry.positionLocation.y};

            // Find closest junction to the robot's position then find distance and angle to get to junction
            if (controller1.yPressed) {
                double[][] absDif = new double[16][4];
                for(int a = 0; a < junctionPositions.length; a++) {
                    for(int b = 0; b < junctionPositions[a].length; b++) {
                        for(int c = 0; c < absDif.length; c++) {
                            absDif[c][0] = Math.abs(junctionPositions[a][b][0] - robotPos[0]);
                            absDif[c][1] = Math.abs(junctionPositions[a][b][1] - robotPos[1]);
                            absDif[c][2] = a;
                            absDif[c][3] = b;
                            }
                        }
                    }
                double[] smallestAbsDif = absDif[0];
                for(int a = 0; a < absDif.length; a++) {
                    if(absDif[a][0] < smallestAbsDif[0] && absDif[a][1] < smallestAbsDif[1]) {
                        smallestAbsDif = absDif[a];
                    }
                }
                int typeOfJunction = (int) smallestAbsDif[2];
                int junctionNumber = (int) smallestAbsDif[3];
                closestJunction = junctionPositions[typeOfJunction][junctionNumber];
                double distanceToJunction = Math.sqrt(Math.pow(smallestAbsDif[0],2) + Math.pow(smallestAbsDif[1],2));
                double angleToJunction = Math.toDegrees(Math.atan2(smallestAbsDif[1], smallestAbsDif[0]));
                odometry.rotateToAngle(angleToJunction);
                if (typeOfJunction == 0) {
                    lift.hjunction();
                } else if (typeOfJunction == 1) {
                    lift.mjunction();
                } else if (typeOfJunction == 2) {
                    lift.ljunction();
                }
                while (!odometry.atTargetAngle() && (!controller1.bPressed || !controller2.bPressed)) {
                    odometry.update();
                }
                /*
                odometry.setTargetPoint(((distanceToJunction-20)*closestJunction[0])/distanceToJunction,((distanceToJunction-20)*closestJunction[1])/distanceToJunction,angleToJunction);
                */
            }

            //Controllers rumble at start of end game
            if(endGameTimer.seconds() >= 30 && !rumble){
                rumble = true;
                controller1.rumble(3);
                controller2.rumble(3);
            }
            if(closestJunction != null) {
                //telemetry.addData("Closest junction", "Junction type:" + closestJunction[0] + ", " + "Junction number:" + closestJunction[1]);
            }
            telemetry.addData("Lift preset", liftPreset);

            telemetry.addData("Cones", coneNum);

            telemetry.addData("Lift Position", lift.getPosition());
            telemetry.addData("Lift Position in cm", lift.ticksToCenti());
            telemetry.addData("Lift Current", lift.getCurrent());

            telemetry.addData("Powers", drive.getPowers());

            odometry.update();
            lift.update();

            telemetry.addData("Robot position", odometry.getLocation());
            if(overrideDrivers) {
                if (!odometry.atTarget())
                    telemetry.addData("Target", odometry.getTargetLocation());
            }

            telemetry.update();
        }
    }

    /*public static class gridM {
        private static final int TILE_SIZE = 60;
        private static final int GRID_SIZE = 6;
        private int xPos;
        private int yPos;
        private int currentTileX;
        private int currentTileY;

        public gridM() {
            xPos = 0;
            yPos = 0;
            currentTileX = 0;
            currentTileY = 0;
        }

        public void moveToTile(int x, int y) {
            if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) {
                System.out.println("Invalid tile coordinates");
                return;
            }

            currentTileX = x;
            currentTileY = y;
            xPos = x * TILE_SIZE + TILE_SIZE / 2;
            yPos = y * TILE_SIZE + TILE_SIZE / 2;

            System.out.println("Robot moved to tile " + x + "," + y);
            System.out.println("Current position: " + xPos + "," + yPos);
        }

        public static void main(String[] args) {
            gridM system = new gridM();
            system.moveToTile(2, 3);
            system.moveToTile(5, 5);
        }
    }*/

    private void overrideDrivers(Object grid) {

    }

    //Setting start position for Tele based on Auto end position
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
            telemetry.addData("Powers", drive.getPowers());

            telemetry.update();
        }

        drive.brake();
        sleep(1000 * secondsOfSleep);
    }
}
