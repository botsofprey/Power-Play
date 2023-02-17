package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo left;

    private Servo right;
    public static final double CLOSE_POSITION = 0.5, OPEN_POSITION = 0;

    public Claw(HardwareMap hardwareMap){
        left = hardwareMap.get(Servo.class, "leftClaw");
        right = hardwareMap.get(Servo.class, "rightClaw");
    }

    public void setPosition(double position) {
        left.setPosition(1-position);
        right.setPosition(position);
    }

    public double getPosition(){
        return right.getPosition();
    }

    public Servo getLeft() {
        return left;
    }

    public Servo getRight() {
        return right;
    }
}
