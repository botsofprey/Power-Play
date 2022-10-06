package RookieBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class DrivetrainTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        boolean slowModeAlreadyOn = false;
        boolean speedModeAlreadyOn = false;
        boolean firstRun = true;
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            if ((gamepad1.right_bumper) && (slowModeAlreadyOn == false)) {
                motorFrontLeft.setPower(frontLeftPower / 4);
                motorBackLeft.setPower(backLeftPower / 4);
                motorFrontRight.setPower(frontRightPower / 4);
                motorBackRight.setPower(backRightPower / 4);
                telemetry.addData("Mode", "Slow");
                slowModeAlreadyOn = true;
                if (speedModeAlreadyOn) {
                    speedModeAlreadyOn = false;
                }
                if (firstRun) {
                    firstRun = false;
                }
            } else if ((gamepad1.left_bumper) && (speedModeAlreadyOn == false)) {
                motorFrontLeft.setPower(frontLeftPower);
                motorBackLeft.setPower(backLeftPower);
                motorFrontRight.setPower(frontRightPower);
                motorBackRight.setPower(backRightPower);
                telemetry.addData("Mode", "Speed");
                speedModeAlreadyOn = true;
                if (slowModeAlreadyOn) {
                    slowModeAlreadyOn = false;
                }
                if (firstRun) {
                    firstRun = false;
                }
            } else if ((gamepad1.y) || (firstRun)) {
                motorFrontLeft.setPower(frontLeftPower / 2);
                motorBackLeft.setPower(backLeftPower / 2);
                motorFrontRight.setPower(frontRightPower / 2);
                motorBackRight.setPower(backRightPower / 2);
                telemetry.addData("Mode", "Normal");
                if (slowModeAlreadyOn) {
                    slowModeAlreadyOn = false;
                }
                if (speedModeAlreadyOn) {
                    speedModeAlreadyOn = false;
                }
                if (firstRun) {
                    firstRun = false;
                }
            }
        }
    }
}