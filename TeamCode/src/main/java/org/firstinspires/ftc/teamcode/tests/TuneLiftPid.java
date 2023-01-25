package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@Autonomous
class TuneLiftPid extends OpMode {
    static private double targetPosition = 0;
    static private boolean targetDescent = false;
    private DcMotorEx lift;
    static private double
            kp = 0.01,
            kI = 0,
            kD = 0;
    PIDCoefficients coeffs = new PIDCoefficients(kp, kI, kD);

    PIDFController liftController = new PIDFController(coeffs);

    @Override
    public void init() {
        lift = hardwareMap.get(DcMotorEx.class, "lift");
    }

    private void setLift(int position) {
        liftController.setTargetPosition(position);
        double correction = liftController.update(lift.getCurrentPosition(), lift.getVelocity());
        lift.setPower(correction);
    }

    @Override
    public void loop() {
        if (targetPosition >= 1000 && !targetDescent) {
            targetPosition += 30;
        } else if (targetPosition <= 30) {
            targetPosition -= 30;
            targetDescent = true;
        } else {
            targetDescent = false;
        }
        setLift((int) targetPosition);
        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Position", lift.getCurrentPosition());
    }
}

