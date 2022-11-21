package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareMechanisms {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotor lift;
    private Servo claw;

    public void init(HardwareMap hwMap) {
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        lift = hwMap.dcMotor.get("lift");
        claw = hwMap.servo.get("claw");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getLift() {
        return lift.getCurrentPosition();
    }

    public void setLift(double liftSpeed) {
        lift.setPower(liftSpeed);
    }

    public Servo getClaw() {
        return claw;
    }

    public void setClaw(double clawPosit) {
        claw.setPosition(clawPosit);
    }
}