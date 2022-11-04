package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ProgrammingBoard1 {
    private DigitalChannel touchsensor;
    private DcMotor motor;

    public void init(HardwareMap hwMap) {
        touchsensor = hwMap.get(DigitalChannel.class, "touchsensor");
        touchsensor.setMode(DigitalChannel.Mode.INPUT);
        motor.hwMap.get(DcMotor.class, "motor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
