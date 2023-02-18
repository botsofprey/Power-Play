package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawArm {
    public static final double UP_POSITION = 1, DOWN_POSITION = 0;
    private Servo servoLeft, servoRight;
    private Servo servoWrist;

    private DcMotorEx turrent;

    double servoLeft_MinLimit =0,
           servoLeft_MaxLimit=1,
           servoRight_MinLimit=0,
           servoRight_MaxLimit=1;

    public ClawArm(HardwareMap hardwareMap) {
         servoLeft = hardwareMap.get(Servo.class, "leftArm");
         servoRight = hardwareMap.get(Servo.class, "rightArm");
         servoWrist = hardwareMap.get(Servo.class, "wrist");
         turrent = hardwareMap.get(DcMotorEx.class, "turrent");
         turrent.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         turrent.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //this is the claw moving up and down, der
    public void setPositionElbow(double position) {
        servoLeft.setPosition(((1 - position) * servoLeft_MinLimit) + (position * servoLeft_MaxLimit));
        servoRight.setPosition(((1 - position) * servoRight_MinLimit) + (position * servoRight_MaxLimit));
    }
    public void flipElbow(){
        setPositionElbow(servoRight.getPosition() == 0 ? 1 : 0);
    }

    //this is the wrist wristing or how ever wrists wrist
    public void setPositionWrist(double position) {
        servoWrist.setPosition(position);
    }

    public void flipWrist(){
        servoWrist.setPosition(servoWrist.getPosition() != UP_POSITION ? UP_POSITION : DOWN_POSITION);
    }

    public void setTurrentPower(double power){
        turrent.setPower(power);
    }

    public int getTurrentPos(){
        return turrent.getCurrentPosition();
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
