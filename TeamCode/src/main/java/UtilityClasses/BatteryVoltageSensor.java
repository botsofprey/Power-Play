package UtilityClasses;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class BatteryVoltageSensor {
    private HardwareMap hardwareMap;

    public BatteryVoltageSensor(HardwareMap hm){
        hardwareMap = hm;
    }

    public double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0)
                result = Math.min(result, voltage);
        }
        return result;
    }
}
