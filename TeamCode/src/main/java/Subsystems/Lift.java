package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Lift {
    private DcMotorEx liftRight, liftLeft;

    private boolean braking;

    public Lift(HardwareMap hardwareMap) {
        liftRight = hardwareMap.get(DcMotorEx.class, "liftRight");
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);

        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftLeft = hardwareMap.get(DcMotorEx.class, "liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        zeroLift();
    }

    public void setPower(double power) {
        if(power < 0 && liftRight.getCurrentPosition() <= 0 ||
        power > 0 && liftRight.getCurrentPosition() >= 1971) {
            power = 0;
            braking = true;
        }

        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setPower(power);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftLeft.setPower(power);
        braking = false;
    }

    public void setTargetPosition(int position, double power) {
        liftRight.setTargetPosition(position);
        liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftRight.setPower(power);
        liftLeft.setTargetPosition(position);
        liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftLeft.setPower(power);
        braking = false;
    }

    public int getPosition() {
        return liftRight.getCurrentPosition();
    }

    public void brake() {
        if(!braking) {
            liftRight.setTargetPosition(liftRight.getCurrentPosition());
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setPower(1);
            liftLeft.setTargetPosition(liftLeft.getCurrentPosition());
            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftLeft.setPower(1);
            braking = true;
        }
    }

    public void zeroLift() {
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Change target positions

    public void coneStack(int coneNum) {
        setTargetPosition(coneNum*105 + 6, 1);
    }

    public void Ground() {
        setTargetPosition(0, 1);
    }

    public void ljunction() {
        setTargetPosition(1400, 1);
    }

    public void mjunction() {
        setTargetPosition(2280, 1);
    }

    public void hjunction() {
        setTargetPosition(3300, 1);
    }

    public void hjunctionScore() {
        setTargetPosition(3000, 1);
    }

    public void update() {

    }

    public boolean isBusy(){
        return liftRight.isBusy()
                && Math.abs(liftRight.getTargetPosition()-liftRight.getCurrentPosition()) > 300;
    }

    public double getPower() {
        return liftRight.getPower();
    }
}