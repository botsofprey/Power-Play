package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

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

    public void setClaw(double clawPosit) {
        claw.setPosition(clawPosit);
    }

    public ArrayList<TestItem> getTests() {
        ArrayList<TestItem> tests = new ArrayList<>();
        tests.add(new TestMotor("FrontRight", 0.5, motorFrontRight));
        tests.add(new TestMotor("FrontLeft", 0.5, motorFrontLeft));
        tests.add(new TestMotor("BackRight", 0.5, motorBackRight));
        tests.add(new TestMotor("BackLeft", 0.5, motorBackLeft));
        tests.add(new TestServo("Claw", claw, 0.0, 1.0));
        return tests;
    }
}