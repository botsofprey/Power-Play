package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import DriveEngine.MecanumDrive;
import Subsystems.Claw;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;
import UtilityClasses.Vector2D;

@TeleOp(name="Basic TeelOp", group="TeleOp")
public class MecanumTele extends LinearOpMode {

    private MecanumDrive drive;
    private Controller controller1;
    private Claw claw;
    private threeWheelOdometry odometry;

    private Location startLoc = new Location(4,0,4);

    @Override
    public void runOpMode() throws InterruptedException {
        controller1 = new Controller(gamepad1);
        drive = new MecanumDrive(hardwareMap);
        claw = new Claw(hardwareMap);
        odometry = new threeWheelOdometry(hardwareMap, startLoc, this);

        while(opModeIsActive()){
            //Checks for new inputs
           controller1.update();

           //Movement
            if(controller1.leftStick.x != 0 && controller1.leftStick.y != 0){
                Vector2D leftInput = controller1.leftStick,
                         rightInput = controller1.rightStick;

                drive.move(leftInput.x + leftInput.y + rightInput.x,
                        leftInput.x - leftInput.y + rightInput.x,
                        leftInput.x + leftInput.y - rightInput.x,
                        leftInput.x - leftInput.y - rightInput.x);

            }
            odometry.update();

            if(controller1.aPressed && claw.getPosition() < 1)
                claw.setPosition(claw.getPosition() + .1);
            else if(controller1.bPressed && claw.getPosition() > 0)
                claw.setPosition(claw.getPosition() - .1);
            telemetry.addData("Claw Pos", claw.getPosition());

            telemetry.addData("Robot position", odometry.getPoint());
            telemetry.update();
        }
    }
}
