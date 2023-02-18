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
    public static double a2wrist = 0;
    public static double b1wrist = 1;
    public static double b2wrist = 1;

    public static double a1claw = 0;
    public static double a2claw = 0;
    public static double b1claw = 0;
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
            //fullElbowBend(a1wrist, a2wrist, b1wrist, b2wrist);
            clawOpenClose(a1claw, a2claw, b1claw, b2claw);}
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

    }
}
