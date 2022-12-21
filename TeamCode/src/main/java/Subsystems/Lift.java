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
        braking = false;
    }

    public void setPosition(int position, double power) {
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(power);
        braking = false;
    }

    public int getPosition() {
        return liftMotor.getCurrentPosition();
    }

    public void brake() {
        if(!braking) {
            liftMotor.setTargetPosition(liftMotor.getCurrentPosition());
            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftMotor.setPower(1);
            braking = true;
        }
    }

    public void zeroLift() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Change target positions

    public void coneStack(int coneNum) {
        liftMotor.setTargetPosition(coneNum*400);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
        braking = false;
    }

    public void Ground() {
        liftMotor.setTargetPosition(0);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
        braking = false;
    }

    public void ljunction() {
        liftMotor.setTargetPosition(1000);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
        braking = false;
    }

    public void mjunction() {
        liftMotor.setTargetPosition(1600);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
        braking = false;
    }

    public void hjunction() {
        liftMotor.setTargetPosition(2150);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.75);
        braking = false;
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