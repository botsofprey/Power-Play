package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tests.TestItem;

public class TestServo extends TestItem {
    private Servo servo;
    double onValue;
    double offValue;

    public TestServo(String description, Servo servo, double offValue, double onValue) {
        super(description);
        this.servo = servo;
        this.onValue = onValue;
        this.offValue = offValue;
    }

    @Override
    public void run(boolean on, Telemetry telemetry) {
        if (on) {
            servo.setPosition(onValue);
        } else {
            servo.setPosition(offValue);
        }
    }

}