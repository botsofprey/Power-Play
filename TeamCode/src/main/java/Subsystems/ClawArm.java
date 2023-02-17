package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawArm {
    public static final double UP_POSITION = 1, DOWN_POSITION = 0;
    private Servo servoLeft, servoRight;
    private Servo servoWrist;

    private DcMotorEx turrent;

    double servoLeft_MinLimit,
           servoLeft_MaxLimit,
           servoRight_MinLimit,
           servoRight_MaxLimit;

    public ClawArm(HardwareMap hardwareMap) {
         servoLeft = hardwareMap.get(Servo.class, "leftArm");
         servoRight = hardwareMap.get(Servo.class, "rightArm");
         servoWrist = hardwareMap.get(Servo.class, "Wrist");
         turrent = hardwareMap.get(DcMotorEx.class, "turrent");
    }
    //this is the claw moving up and down, der
    public void setPositionElbow(double position) {
        servoLeft.setPosition(((1 - position) * servoLeft_MinLimit) + (position * servoLeft_MaxLimit));
        servoRight.setPosition(((position) * servoRight_MinLimit) + (position * servoRight_MaxLimit));
    }
    public void flipElbow(){
        setPositionElbow(servoRight.getPosition() == 0 ? 1 : 0);
    }

    //this is the wrist wristing or how ever wrists wrist
    public void setPositionWrist(double position) {
        servoWrist.setPosition(position);
    }

    public void flipWrist(){
        servoWrist.setPosition(servoWrist.getPosition() != 1 ? 1 : 0);
    }

    public void setTurrentPower(double power){
        turrent.setPower(power);
    }

    public boolean turrentIn(){
        return turrent.getCurrentPosition() <= 5;
    }

    public double getRightArmPos(){
        return servoRight.getPosition();
    }
    public double getLeftArmPos(){
        return servoLeft.getPosition();
    }
}
