package OpMode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import Subsystems.Claw;
import Subsystems.ClawArm;
import UtilityClasses.Controller;

@Config
@TeleOp
public class findServoLimits extends LinearOpMode {
    public static double a1arm = 0;
    public static double a2arm = 0;
    public static double b1arm = 1;
    public static double b2arm = 1;

    public static double a1claw = 0.52; //left
    public static double a2claw = 0.48; //right
    public static double b1claw = 1;
    public static double b2claw = 0;

    public static double a1wrist = 0;
    public static double b1wrist = 1;

    Claw claw;
    ClawArm clawArm;

    Servo leftArm, rightArm, wrist;
    Servo leftClaw, rightClaw;

    @Override
    public void runOpMode() throws InterruptedException {
        claw = new Claw(hardwareMap);
        clawArm = new ClawArm(hardwareMap);

        leftArm = clawArm.getServoLeft();
        rightArm = clawArm.getServoRight();
        wrist = clawArm.getServoWrist();

        leftClaw = claw.getLeft();
        rightClaw = claw.getRight();

        while (!isStopRequested()) {
            /*if (gamepad1.a)
                leftArm.setPosition(0);
            if (!gamepad1.a)
                leftArm.setPosition(1);
            if (gamepad1.b)
                rightArm.setPosition(1);
            if (!gamepad1.b)
                rightArm.setPosition(0);*/
            /*
            if (gamepad1.a) {
            fullElbowBend(a1arm, a2arm, b1arm, b2arm);
            clawOpenClose(a1claw, a2claw, b1claw, b2claw);
            wristFlip(a1wrist, b1wrist);}
            */
            double power = ((gamepad1.right_trigger - gamepad1.left_trigger) * 0.5);
            if ((clawArm.getTurrentPos() < 1300 && power > 0) || (clawArm.getTurrentPos() > 0 && power < 0)) {
                clawArm.setTurrentPower(power);
            } else {
                clawArm.setTurrentPower(0);
            }

            telemetry.addData("right trigger", gamepad1.right_trigger);
            telemetry.addData("left trigger", gamepad1.left_trigger);
            telemetry.addData("slide pos", clawArm.getTurrentPos());
            telemetry.addData("A is pressed", gamepad1.a);
            telemetry.addData("servo position:", leftArm.getPosition());
            telemetry.update();
        }
    }

    public void fullElbowBend(double a1, double a2, double b1, double b2) {
        leftArm.setPosition(leftArm.getPosition() == b1 ? 0 : b1);
        rightArm.setPosition(rightArm.getPosition() == a2 ? 1 : a2);
        sleep(1000);
        leftArm.setPosition(leftArm.getPosition() == a1 ? 1 : a1);
        rightArm.setPosition(leftArm.getPosition() == b2 ? 0 : b2);
        sleep(1000);
    }

    public void clawOpenClose(double a1, double a2, double b1, double b2) {
        leftClaw.setPosition(a1);
        rightClaw.setPosition(a2);
        sleep(1000);
        leftClaw.setPosition(b1);
        rightClaw.setPosition(b2);
        sleep(1000);
    }

    public void wristFlip(double a1, double b1) {
        wrist.setPosition(a1);
        sleep(1000);
        wrist.setPosition(b1);
        sleep(1000);
    }
}
