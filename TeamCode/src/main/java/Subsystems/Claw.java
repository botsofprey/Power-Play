package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo leftArm, rightArm;
    public static final double CLOSE_POSITION = 0.5, OPEN_POSITION = 0;

    public Claw(HardwareMap hardwareMap){
        leftArm = hardwareMap.get(Servo.class, "leftArm");
        rightArm = hardwareMap.get(Servo.class, "rightArm");
    }

    public void setPosition(double position) {
        leftArm.setPosition(1.-position);
        rightArm.setPosition(position);
    }

    public double getPosition(){
        return rightArm.getPosition();
    }
}
