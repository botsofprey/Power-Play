package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
@Autonomous
public class TuneLiftPid extends LinearOpMode {
    static private double targetPosition = 0;
    static private boolean targetDescent = false;
    private DcMotorEx lift;
    static private double
            kp = 0.01,
            kI = 0,
            kD = 0;
    PIDCoefficients coeffs = new PIDCoefficients(kp, kI, kD);

    PIDFController liftController = new PIDFController(coeffs);

    private void setLift(int position) {
        liftController.setTargetPosition(position);
        double correction = liftController.update(lift.getCurrentPosition(), lift.getVelocity());
        lift.setPower(correction);
    }

    public void runOpMode() {
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (!isStopRequested()) {
            if (targetPosition <= 4000 && !targetDescent) {
                targetPosition += 10;
            } else if (targetPosition >= 0) {
                targetPosition -= 10;
                targetDescent = true;
            } else {
                targetDescent = false;
            }
            setLift((int) targetPosition);
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Position", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}


