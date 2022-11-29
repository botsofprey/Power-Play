package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class motorProgrammingBoard {
    private DcMotor motor;
    private double ticksPerRotation;
    private DcMotor motorFrontRight, motorBackRight,
                    motorBackLeft, motorFrontLeft;

    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "motor");
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        ticksPerRotation = motor.getMotorType().getTicksPerRev();
    }

    public void setMotorSpeed(double speed) {
        motor.setPower(speed);
    }
    public double getMotorRotations() {
        return motor.getCurrentPosition() / ticksPerRotation;
    }
}

