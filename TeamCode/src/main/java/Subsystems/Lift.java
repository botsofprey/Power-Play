package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift {
    private DcMotor liftMotorRight, liftMotorLeft;

    private boolean braking;

    //private TouchSensor limitSwitch;

    public Lift(HardwareMap hardwareMap) {
        liftMotorRight = hardwareMap.get(DcMotor.class, "liftRight");
        liftMotorLeft = hardwareMap.get(DcMotor.class, "liftLeft");
        liftMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //limitSwitch = hardwareMap.get(TouchSensor.class, "limitSwitch");
        liftMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setPower(double power) {
        if (power < 0 &&
                (liftMotorRight.getCurrentPosition() <= 5 || liftMotorLeft.getCurrentPosition() <= 5) ||
                power > 0 &&
                (liftMotorRight.getCurrentPosition() >= 1971 || liftMotorLeft.getCurrentPosition() >= 1971))
            brake();

        liftMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotorRight.setPower(power);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotorLeft.setPower(power);
        braking = false;
    }

    public void setPosition(int position, double power) {
        liftMotorRight.setTargetPosition(position);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(power);
        liftMotorLeft.setTargetPosition(position);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(power);
        braking = false;
    }

    public int getPositionRight() {
        return liftMotorRight.getCurrentPosition();
    }
    public int getPositionLeft() {
        return liftMotorLeft.getCurrentPosition();
    }


    public void brake() {
        if(!braking) {
            liftMotorRight.setTargetPosition(liftMotorRight.getCurrentPosition());
            liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftMotorRight.setPower(1);
            braking = true;
        }
    }

    public void zeroLift() {
        liftMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Change target positions

    public void coneStack(int coneNum) {
        liftMotorRight.setTargetPosition(coneNum*105 + 6);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(coneNum*105 + 6);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void Ground() {
        liftMotorRight.setTargetPosition(0);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(0);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void ljunction() {
        liftMotorRight.setTargetPosition(1400);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(1400);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void mjunction() {
        liftMotorRight.setTargetPosition(2280);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(2280);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void hjunction() {
        liftMotorRight.setTargetPosition(3300);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(3300);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void hjunctionScore() {
        liftMotorRight.setTargetPosition(3200);
        liftMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorRight.setPower(1);
        liftMotorLeft.setTargetPosition(3200);
        liftMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorLeft.setPower(1);
        braking = false;
    }

    public void update() {

    }

    public boolean isBusy(){
        return liftMotorRight.isBusy()
                && Math.abs(liftMotorRight.getTargetPosition()-liftMotorRight.getCurrentPosition()) > 300;
    }

    public double getPowerRight() {
        return liftMotorRight.getPower();
    }
    public double getPowerLeft() {
        return liftMotorLeft.getPower();
    }

    @Deprecated
    public boolean isPressed() {
        return true;
    }
}