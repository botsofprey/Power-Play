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
    public static double a1elbow = 0;
    public static double a2elbow = 0;
    public static double b1elbow = 1;
    public static double b2elbow = 1;

    public static double a1claw = 0;
    public static double a2claw = 0;
    public static double b1claw = 0;
    public static double b2claw = 0;

    public static double a1wrist = 0;
    public static double b1wrist = 0;

    Claw claw;
    ClawArm clawArm;

    double inc;
    double dec;
    boolean aIsToggled;
    boolean aprev;
    boolean anow;

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

            if (gamepad1.a) {
                leftArm.setPosition(0.5);
                rightArm.setPosition(0.5);
            }
            double leftTrig = gamepad1.left_trigger;
            double rightTrig = gamepad1.right_trigger;

            leftArm.setPosition(leftArm.getPosition() - (leftTrig / 100));
            rightArm.setPosition(rightArm.getPosition() + (leftTrig / 100));

            rightArm.setPosition(rightArm.getPosition() - (rightTrig / 100));
            leftArm.setPosition(leftArm.getPosition() + (rightTrig / 100));
            //fullElbowBend(a1elbow, a2elbow, b1elbow, b2elbow);
            clawOpenClose(a1claw, a2claw, b1claw, b2claw);

            telemetry.addData("A is toggled", aIsToggled);
            telemetry.addData("left servo position:", leftArm.getPosition());
            telemetry.addData("right servo position:", rightArm.getPosition());
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

    }

    public void Twistwist(double a1, double b1) {
        wrist.setPosition(wrist.getPosition() == b1? 0 : b1);

        sleep(1000);

        wrist.setPosition(wrist.getPosition() == a1? 0 : a1);

        sleep(1000);
    }
}
