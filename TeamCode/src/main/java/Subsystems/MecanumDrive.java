package Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrive {
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    public void init(HardwareMap hardwareMap) {
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftDriveMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightDriveMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftDriveMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightDriveMotor");

        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void setPowers(double frontLeftPower, double frontRightPower,double
            backLeftPower, double backRightPower) {
        double maxSpeed = 1.0;
        maxSpeed = Math.max(maxSpeed, Math.abs(frontLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(frontRightPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backLeftPower));
        maxSpeed = Math.max(maxSpeed, Math.abs(backRightPower));

        frontLeftPower /=maxSpeed;
        frontRightPower /=maxSpeed;
        backRightPower /=maxSpeed;
        backLeftPower /=maxSpeed;

        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower((backRightPower));

        //public void drive(double forward, double right, double rotate) {
        //  double frontLeftPower = forward + right + rotate;
        //double frontRightPower = forward - right - rotate;
        //double backRightPower = forward + right - rotate;
        //double backLeftPower = forward - right + rotate;
        //setPowers(frontLeftPower, frontRightPower, backRightPower, backLeftPower);
        //}
    }

    public void drive(double forward, double right, double rotate) {
    }
}
