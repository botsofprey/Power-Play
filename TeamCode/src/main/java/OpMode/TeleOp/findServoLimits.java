package OpMode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import Subsystems.Claw;
import Subsystems.ClawArm;

@Config
@TeleOp
public class findServoLimits extends LinearOpMode {
    public static double a1wrist = 0;
    public static double a2wrist = 1;
    public static double b1wrist = 1;
    public static double b2wrist = 0;

    public static double a1claw = 0;
    public static double a2claw = 1;
    public static double b1claw = 1;
    public static double b2claw = 0;

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
            if (gamepad1.a) {
                leftArm.setPosition(-leftArm.getPosition());
                rightArm.setPosition(-rightArm.getPosition());
                fullElbowBend(a1wrist, a2wrist, b1wrist, b2wrist);
                clawOpenClose(a1claw, a2claw, b1claw, b2claw);
            }
        }
    }

    public void fullElbowBend(double a1, double a2, double b1, double b2) {
        while (leftArm.getPosition() != a1 && rightArm.getPosition() != a2) {
            leftArm.setPosition(a1);
            rightArm.setPosition(a2);
        }

        while (leftArm.getPosition() != b1 && rightArm.getPosition() != b2) {
            leftArm.setPosition(b1);
            rightArm.setPosition(b2);
        }
    }

    public void clawOpenClose(double a1, double a2, double b1, double b2) {
        while (leftClaw.getPosition() != a1 && rightClaw.getPosition() != a2) {
            leftClaw.setPosition(a1);
            rightClaw.setPosition(a2);
        }

        while (leftClaw.getPosition() != b1 && rightClaw.getPosition() != b2) {
            leftClaw.setPosition(b1);
            rightClaw.setPosition(b2);
        }
    }
}
