package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class motorProgrammingBoard {
    private DcMotor motor;
    private double ticksPerRotation;
    public DcMotor motorFrontLeft, motorBackRight,
                   motorBackLeft, motorFrontRight;

    public void init(HardwareMap hwMap) {
        motor = hwMap.get(DcMotor.class, "motor");
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        ticksPerRotation = motor.getMotorType().getTicksPerRev();
    }

    //public void setMotorSpeed(double speed) {
    //    motor.setPower(speed);
    //}
    public double getMotorRotations() {
        return motor.getCurrentPosition() / ticksPerRotation;
    }
}

