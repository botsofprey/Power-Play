package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class PID {

    public void PidControl(int position, DcMotor motor) {
        double time = System.currentTimeMillis();
        double error = position - motor.getCurrentPosition();
    }
}
