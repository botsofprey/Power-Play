package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Lift {
    private DcMotor liftMotor;

    private boolean braking;

    private TouchSensor limitSwitch;

    public Lift(HardwareMap hardwareMap) {
        liftMotor = hardwareMap.get(DcMotor.class, "lift");
        limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch");
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power) {
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setPower(power);
    }

    public void setPosition(int position, double power) {
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(power);
    }

    public int getPosition() {
        return liftMotor.getCurrentPosition();
    }

    public void brake() {
        liftMotor.setTargetPosition(liftMotor.getCurrentPosition());
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(.5);
    }

    public void zeroLift() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void Bottom() {
        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
    }

    // int position, double power
    public void Quarter() {
        liftMotor.setTargetPosition(6);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
    }

    public void Half() {
        liftMotor.setTargetPosition(12);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
    }

    public void ThreeQuarters() {
        liftMotor.setTargetPosition(27);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
    }

    public void Top() {
        liftMotor.setTargetPosition(36);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
    }

    public void update() {;
        if (limitSwitch.isPressed()) {
            zeroLift();
            if(liftMotor.getPower() < 0){
                liftMotor.setPower(0);
            }
        }

    }

    public boolean isBusy(){
        return liftMotor.isBusy();
    }
}