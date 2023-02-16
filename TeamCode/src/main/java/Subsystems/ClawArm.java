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

    public ClawArm(HardwareMap hardwareMap) {
         servoLeft = hardwareMap.get(Servo.class, "Left servo");
         servoRight = hardwareMap.get(Servo.class, "Right servo");
         servoWrist = hardwareMap.get(Servo.class, "Wrist");
         turrent = hardwareMap.get(DcMotorEx.class, "turrent");

         turrent.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         turrent.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public void flipWrist() {
        setPositionWrist(getWristPosition() == 1 ? 0 : 1);
    }

    public double getArmPosition(){
        return servoRight.getPosition();
    }

    public double getWristPosition(){
        return servoWrist.getPosition();
    }

    public void setTurrentPower(double power) {
        if(turrentIn() && power < 0)
            power = 0;

        turrent.setPower(power);
    }

    public boolean turrentIn() {
        return turrent.getCurrentPosition() <= 0;
    }

}
