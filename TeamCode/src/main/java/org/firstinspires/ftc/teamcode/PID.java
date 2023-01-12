package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class PID {
    final double kP = 0;
    final private double kI = 0;
    final private double Kd = 0;
    private double totalI;
    private double lastError;

    public void PidControl(int position, DcMotor motor) {
        double time = System.currentTimeMillis();
        double error = position - motor.getCurrentPosition();
        double p = kP * error;
        double i = kI * error * time;
        totalI += i;
        double d = Kd * (error - lastError) / time;
        motor.setPower(p + totalI + d);
        lastError = position - motor.getCurrentPosition();
    }
}
