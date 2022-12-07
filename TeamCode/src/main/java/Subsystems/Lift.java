package Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ProgrammingBoard7;

public class Lift {
    private DcMotor liftMotor;

    private boolean braking;

    public Lift (HardwareMap hardwareMap){
        liftMotor = hardwareMap.get(DcMotor.class, "lift");
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power){
            liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftMotor.setPower(power);
    }

    public void setPosition(int position, double power){
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(power);
    }

    public int getPosition(){
        return liftMotor.getCurrentPosition();
    }

    public void brake(){
        liftMotor.setTargetPosition(liftMotor.getCurrentPosition());
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(.5);
    }

    public void zeroLift(){
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update(HardwareMap hwMap) {
        ProgrammingBoard7 board = new ProgrammingBoard7();
        telemetry.addData("Touch Sensor", board.getClass());
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}