package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import UtilityClasses.Location;

public class Lift {
    private DcMotorEx liftMotor;
    public Location OFFSET_ON_BOT = new Location (23, 1);// new Location(19, 1);

    private boolean braking;

    private TouchSensor limitSwitch;

    private double powerDecrease = 3.;

    public Lift(HardwareMap hardwareMap) {
        liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
        limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch");
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power) {
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if(liftMotor.getCurrentPosition() <= 0 && power < 0){
            power = 0;
        }

        if(power < 0)
            power /= powerDecrease;

        liftMotor.setPower(power);
        braking = power == 0;
    }

    public void setPosition(int position, double power) {
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(position < liftMotor.getCurrentPosition()){
            power /= powerDecrease;
        }
        liftMotor.setPower(power);
        braking = false;
    }


    private double centiPerTicks = (Math.PI * 6.0) / 3895.9;
    public double ticksToCenti(){
        return liftMotor.getCurrentPosition() * centiPerTicks;
    }

    public int getPosition() {
        return liftMotor.getCurrentPosition();
    }

    public double getCurrent() {
        return liftMotor.getCurrent(CurrentUnit.AMPS);
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
        if(coneNum < 1 || coneNum > 5)
            return;

        int target=0;

        switch(coneNum){
            case 1:
                break;
            case 2:
                target = 161;
                break;
            case 3:
                target = 288;
                break;
            case 4:
                target = 390;
                break;
            case 5:
                target = 537;
                break;
        }

        liftMotor.setTargetPosition(target);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);
        braking = false;
    }

    public void Ground() {
        setPosition(0, 1);
    }

    public void ljunction() {
        setPosition(1400, 1);
    }

    public void mjunction() {
        setPosition(2280, 1);
    }

    public void hjunction() {
        setPosition(3300, 1);
        braking = false;
    }

    public void hjunctionScore() {
        setPosition(3000, 1);
        braking = false;
    }

    public void addToTarget(int addOn){
        liftMotor.setTargetPosition(liftMotor.getTargetPosition() + addOn);
    }

    public void update() {
        if (limitSwitch.isPressed()) {
            zeroLift();
            if(getPower() < 0){
                setPower(0);
            }
        }

    }

    public boolean isPressed(){
        return limitSwitch.isPressed();
    }

    public double getTouchValue(){
        return limitSwitch.getValue();
    }

    public boolean isBusy(){
        return liftMotor.isBusy();
    }

    public boolean closeToTarget(){
        return Math.abs(liftMotor.getTargetPosition()-liftMotor.getCurrentPosition()) <= 300;
    }

    public void addTargetClearance(){
        liftMotor.setTargetPosition(liftMotor.getTargetPosition() + 300);
    }

    public double getPower() {
        return liftMotor.getPower();
    }

    public int getTarget() {
        return liftMotor.getTargetPosition();
    }
}