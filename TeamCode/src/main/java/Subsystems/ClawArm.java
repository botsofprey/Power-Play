package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawArm {
    public static final double UP_POSITION = 1, DOWN_POSITION = 0;
    public static double TURRET_MAX_POWER = 1.0;

    private Servo servoLeft, servoRight;
    private Servo servoWrist;

    private DcMotorEx turret;

    double servoLeft_MinLimit = 0.3,
            servoLeft_MaxLimit = 1,
            servoRight_MinLimit = 0.7,
            servoRight_MaxLimit = 0;

    public ClawArm(HardwareMap hardwareMap) {
        servoLeft = hardwareMap.get(Servo.class, "leftArm");
        servoRight = hardwareMap.get(Servo.class, "rightArm");
        servoWrist = hardwareMap.get(Servo.class, "wrist");
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setTargetPosition(0);
//        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    //this is the claw moving up and down, der
    public void setPositionElbow(double position) {
        servoLeft.setPosition(((1 - position) * servoLeft_MinLimit) + (position * servoLeft_MaxLimit));
        servoRight.setPosition(((1 - position) * servoRight_MinLimit) + (position * servoRight_MaxLimit));
    }

    public void flipElbow() {
        setPositionElbow(servoRight.getPosition() == 0 ? 1 : 0);
    }

    //this is the wrist wristing or how ever wrists wrist
    public void setPositionWrist(double position) {
        servoWrist.setPosition(position);
    }

    public void flipWrist() {
        servoWrist.setPosition(servoWrist.getPosition() != UP_POSITION ? UP_POSITION : DOWN_POSITION);
    }

    public void setWristPosition(double position) {
        servoWrist.setPosition(position);
    }

    public void setTurretPosition(int targetPosition) {
        turret.setTargetPosition(targetPosition);
        turret.setPower(TURRET_MAX_POWER);
    }

    public void setTurretPower(double power) {
        turret.setPower(power);
    }

    public int getTurretPos() {
        return turret.getCurrentPosition();
    }

    public boolean turretIn() {
        return turret.getCurrentPosition() <= 5;
    }

    public double getRightArmPos() {
        return servoRight.getPosition();
    }

    public double getLeftArmPos() {
        return servoLeft.getPosition();
    }

    public Servo getServoLeft() {
        return servoLeft;
    }

    public Servo getServoRight() {
        return servoRight;
    }

    public Servo getServoWrist() {
        return servoWrist;
    }
}
