package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo claw;
    public static final double CLOSE_POSITION = 0, OPEN_POSITION = .5;

    public Claw(HardwareMap hardwareMap){
        claw = hardwareMap.get(Servo.class, "claw");
    }

    public void setPosition(double position) {
        claw.setPosition(position);
    }

    public double getPosition(){
        return claw.getPosition();
    }
}
