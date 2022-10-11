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

@TeleOp(name="Basic TeelOp", group="TeleOp")
public class MecanumTele extends LinearOpMode {

    private MecanumDrive drive;
    private Controller controller1, controller2;
    private Claw claw;
    private Lift lift;

    private double closePos = 1, openPos = 0;
    private threeWheelOdometry odometry;

    private Location startLoc = new Location(4,0,4);

    @Override
    public void runOpMode() throws InterruptedException {
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);
        drive = new MecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        odometry = new threeWheelOdometry(hardwareMap, startLoc, this);
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


           if(controller1.startPressed){
               drive.slowMode();
           }

        //Driver 1 uses joysticks for movement, duh
            Vector2D leftInput = controller1.leftStick,
                     rightInput = controller1.rightStick;

            drive.moveTrueNorth(
                    leftInput.x + leftInput.y + rightInput.x,
                    leftInput.x - leftInput.y + rightInput.x,
                    leftInput.x + leftInput.y - rightInput.x,
                    leftInput.x - leftInput.y - rightInput.x,
                    leftInput.angle);
            telemetry.addData("left Angle", leftInput.angle);

          //  odometry.update();

            //Driver 1 controls claw
            if(controller1.aPressed){
                claw.setPosition(claw.getPosition() != closePos ? closePos : openPos);
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

            telemetry.addData("Robot position", odometry.getLocation());
            telemetry.update();
        }
    }
}
