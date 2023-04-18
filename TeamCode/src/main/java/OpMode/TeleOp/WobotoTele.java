package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import DriveEngine.MecanumDrive;
import Subsystems.Claw;
import Subsystems.ClawArm;
import Subsystems.Lift;
import UtilityClasses.Controller;

@TeleOp(name = "Woboto Tele", group = "TeleOp")
public class WobotoTele extends LinearOpMode {
    public static int LIFT_MAX_LEFT = 2500;    // TODO configure value
    public static int LIFT_MAX_RIGHT = 2500;    // TODO configure value
    public static int TURRET_MAX = 1300;    // empirically determined value
    public static double ELBOW_SAFE_FOR_LIFT = 0.2;

    private MecanumDrive drive;

    private Lift lift;
    private ClawArm arm;
    private Claw claw;
    private double elbowPosition = 0.0;  // units are fraction of the servo arm swing [0..1]
    private double wristPosition = 1.0;  // units are fraction of the servo position [0..1]
    private double elbowVelocity = 0.02;
    private double wristVelocity = 0.02;
    private double elbowMinSafePosition = ELBOW_SAFE_FOR_LIFT;
    private int turretPositionTics = 0;
    private int turretVelocity = 25;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, 0);

        lift = new Lift(hardwareMap);
        arm = new ClawArm(hardwareMap);
        claw = new Claw(hardwareMap);

        Controller con1 = new Controller(gamepad1);
        Controller con2 = new Controller(gamepad2);

        waitForStart();

        ElapsedTime endGameTimer = new ElapsedTime();

        // Control scheme
        //
        // User 1
        // Left joystick = drive & strafe power/velocity (override User 2)
        // Right joystick left/right = turn power/velocity (override User 2)
        // Right joystick up/down = lift power/velocity up/down
        // Left trigger = elbow velocity in
        // Right trigger = elbow velocity out
        // D-pad up = turret fixed
        // D-pad down = turret in
        // D-pad left/right = slow turn left/right
        // Right bumper = fast drive mode, press=fast, release=normal
        // Left bumper = slow drive mode, press=slower, release=normal
        // A = close claw
        // B = open claw

        while (opModeIsActive()) {
            con1.update();
            con2.update();

            if (con1.rightBumperHeld) {
                drive.setFastSpeed();
            } else if (con1.leftBumperHeld || con2.leftBumperHeld) {
                drive.setSlowSpeed();
            } else {
                drive.setNormalSpeed();
            }

            double forward = con1.leftStick.y == 0 ? con2.leftStick.y : con1.leftStick.y;
            double strafe = con1.leftStick.x == 0 ? con2.leftStick.x : con1.leftStick.x;
            double turn = con1.rightStick.x == 0 ? con2.rightStick.x : con1.rightStick.x;

            if (turn == 0.0 && (con1.leftHeld || con1.rightHeld)) {
                drive.setSlowSpeed();
                turn = (con1.rightHeld ? 1 : 0) - (con1.leftHeld ? 1 : 0);
            }

            // Robot-oriented movement
            drive.moveWithPower(
                    forward + strafe + turn,
                    forward - strafe + turn,
                    forward + strafe - turn,
                    forward - strafe - turn
            );

            // Control of lift
            if (con2.leftTriggerHeld) { // lift down
                if (lift.getPositionLeft() <= 0 || lift.getPositionRight() <= 0) {
                    lift.brake();
                } else {
                    lift.setPower(-con2.leftTrigger * 0.5);
                }
            } else if (con2.rightTriggerHeld) { // lift up
                if (lift.getPositionLeft() >= LIFT_MAX_LEFT || lift.getPositionRight() >= LIFT_MAX_RIGHT) {
                    lift.brake();
                } else {
                    lift.setPower(con2.rightTrigger * 0.5);
                }
            } else if (con1.rightStick.y < 0 && (lift.getPositionLeft() > 0 && lift.getPositionRight() > 0)) {
                lift.setPower(con1.rightStick.y * 0.5);
            } else if (con1.rightStick.y > 0 && (lift.getPositionLeft() < LIFT_MAX_LEFT && lift.getPositionRight() < LIFT_MAX_RIGHT)) {
                lift.setPower(con1.rightStick.y * 0.5);
            } else {
                lift.brake();
            }

            // Control of turret
            turretPositionTics += turretVelocity * ((con1.upHeld ? 1 : 0) - (con1.downHeld ? 1 : 0));
            turretPositionTics = Math.max(0, Math.min(turretPositionTics, TURRET_MAX));
            arm.setTurretPosition(turretPositionTics);

            // Control of elbow
            elbowPosition += elbowVelocity * (con1.rightTrigger - con1.leftTrigger);
            elbowPosition = Math.max(0.0, Math.min(elbowPosition, 1.0));
            // keep arm away from lift
            if (elbowPosition < elbowMinSafePosition && (lift.getPositionLeft() > 50 || lift.getPositionRight() > 50 || turretPositionTics > 50)) {
                elbowPosition = elbowMinSafePosition;
            }
            arm.setPositionElbow(elbowPosition);

            if (con1.yHeld && elbowPosition >= elbowMinSafePosition) {
                wristPosition = Math.max(0.0, Math.min(wristPosition + wristVelocity, 1.0));
            } else if (con1.xHeld && elbowPosition >= elbowMinSafePosition) {
                wristPosition = Math.max(0.0, Math.min(wristPosition - wristVelocity, 1.0));
            }
            else if (con1.rightTriggerHeld || con1.leftTriggerHeld) {
                wristPosition = 1 - elbowPosition;
            }
            arm.setWristPosition(wristPosition);

            // Control of claw
            if (con1.aPressed) {
                claw.setPosition(Claw.CLOSE_POSITION);
                telemetry.addData("claw", "closed");
            } else if (con1.bPressed) {
                claw.setPosition(Claw.OPEN_POSITION);
                telemetry.addData("claw", "opened");
            }

            telemetry.addData("right servo", arm.getRightArmPos());
            telemetry.addData("left servo", arm.getLeftArmPos());
            telemetry.addData("wrist servo", arm.getServoWrist().getPosition());
            telemetry.addLine();

            telemetry.addData("Turret position", arm.getTurretPos());
            telemetry.addData("Lift position lt", lift.getPositionLeft());
            telemetry.addData("Lift position rt", lift.getPositionRight());
            telemetry.addData("powers", drive.getPowers());
            telemetry.update();

            if (endGameTimer.seconds() >= 90) {
                con1.rumble(3);
                con2.rumble(3);
                endGameTimer.reset();
            }
        }
    }
}
