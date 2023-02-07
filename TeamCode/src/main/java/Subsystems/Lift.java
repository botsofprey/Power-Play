package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import UtilityClasses.Location;

public class Lift {
    private DcMotorEx liftMotor;
    public Location OFFSET_ON_BOT = new Location(29, 0);

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

        if(power < 0)
            power /= powerDecrease;

        liftMotor.setPower(power);
        braking = false;
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
        liftMotor.setTargetPosition(coneNum*105 + 6);
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

    public void update() {
        if (limitSwitch.isPressed()) {
            zeroLift();
            if(liftMotor.getPower() < 0){
                liftMotor.setPower(0);
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