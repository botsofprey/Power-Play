package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ProgrammingBoardEX2 {
    private DigitalChannel touchSensor;
    private DcMotor motor;
    private Servo servo;
    private double ticksPerRotation;

    public void init(HardwareMap hwMap) {
        motor = (DcMotor) hwMap.get(DigitalChannel.class, "motor");
        touchSensor = hwMap.get(DigitalChannel.class, "touch_sensor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ticksPerRotation = motor.getMotorType().getTicksPerRev();

    }
}
