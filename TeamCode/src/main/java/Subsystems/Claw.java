package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo claw;

    public Claw(HardwareMap hardwareMap){
        claw = hardwareMap.get(Servo.class, "Claw");
    }

    public void setPosition(double position){
        claw.setPosition(position);
    }

    public double getPosition(){
        return claw.getPosition();
    }
}
