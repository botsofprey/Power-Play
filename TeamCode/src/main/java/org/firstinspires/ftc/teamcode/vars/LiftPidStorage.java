package org.firstinspires.ftc.teamcode.vars;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LiftPidStorage {

    PIDCoefficients coeffs = new PIDCoefficients(0.01, 0, 0);
    PIDFController controller = new PIDFController(coeffs);
    DcMotorEx lift;

    /**
     * A method used to control the lift using PIDF control
     *
     * @param position The target position
     */
    public void setLiftPid(int position) {
        controller.setTargetPosition(position);
        double correction = controller.update(lift.getCurrentPosition(), lift.getVelocity());
        lift.setPower(correction);
    }
}
