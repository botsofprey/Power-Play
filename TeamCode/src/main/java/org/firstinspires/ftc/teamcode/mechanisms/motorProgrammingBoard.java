package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class motorProgrammingBoard {
    private double ticksPerRotation;
    public DcMotor motorFrontLeft, motorBackRight,
                   motorBackLeft, motorFrontRight;

    public void init(HardwareMap hwMap) {
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        getTicksPerRotation(motorFrontLeft);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    //public void setMotorSpeed(double speed) {
    //    motor.setPower(speed);
    //}
    public double getMotorRotations(DcMotor motor) {
        return motor.getCurrentPosition() / ticksPerRotation;
    }
    public double getTicksPerRotation(DcMotor motor) {
        return ticksPerRotation = motor.getMotorType().getTicksPerRev();
    }
}

