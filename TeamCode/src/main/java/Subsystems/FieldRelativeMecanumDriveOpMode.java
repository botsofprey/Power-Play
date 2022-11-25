package Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp
public class FieldRelativeMecanumDriveOpMode extends OpMode {
    MecanumDrive drive = new MecanumDrive();
    BNO055IMU imu;

    @Override
    public void init() {
        drive.init(hardwareMap);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);
    }
    private void driveFieldRelative(double forward, double right, double rotate) {
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC,
                AxesOrder.ZYX, AngleUnit.RADIANS);
        double theta = Math.atan2(forward, right);
        double r = Math.hypot((forward), right);
        theta = AngleUnit.normalizeRadians(theta - orientation.firstAngle);

        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        drive.drive(newForward, newRight, rotate);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
        telemetry.addData("rotate", rotate);
        telemetry.addData("forward", forward);
        telemetry.addData("right", right);
        driveFieldRelative(forward, right, rotate);

        telemetry.update();
    }
}
