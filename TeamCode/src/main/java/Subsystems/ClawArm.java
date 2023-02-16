package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawArm {
    public static final double UP_POSITION = 1, DOWN_POSITION = 0;
    private Servo servoLeft, servoRight;
    private Servo servoWrist;

    private DcMotorEx turrent;

    public ClawArm(HardwareMap hardwareMap) {
         servoLeft = hardwareMap.get(Servo.class, "Left servo");
         servoRight = hardwareMap.get(Servo.class, "Right servo");
         servoWrist = hardwareMap.get(Servo.class, "Wrist");
         turrent = hardwareMap.get(DcMotorEx.class, "turrent");
    }
    //this is the claw moving up and down, der
    public void setPositionElbow(double position) {
        servoLeft.setPosition(1-position);
        servoRight.setPosition(position);
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
}
