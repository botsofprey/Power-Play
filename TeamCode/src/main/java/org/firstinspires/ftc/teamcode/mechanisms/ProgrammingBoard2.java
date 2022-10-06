package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ProgrammingBoard2 {
    private DigitalChannel touchSensor;

    public void init(HardwareMap hwMap) {
        touchSensor = hwMap.get(DigitalChannel.class, "touch_sensor");
        touchSensor.setMode(DigitalChannel.Mode.INPUT);
    }
    public boolean isTouchSensorPressed() {
        return !touchSensor.getState();  //if the state is false, then the touch sensor is pressed
    }
    public boolean isTouchSensorReleased() {
        return touchSensor.getState();  //if the state is true then the touch sensor is released
        // i think??
    }
}
