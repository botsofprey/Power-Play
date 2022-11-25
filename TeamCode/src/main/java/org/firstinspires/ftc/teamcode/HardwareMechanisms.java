package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.ArrayList;

public class HardwareMechanisms {

    MecanumDrive drive = new MecanumDrive();

  //  DcMotor lift;
  //  private Servo claw;
    BNO055IMU imu;
    double y;
    double x;
    double rx;

    public void init(HardwareMap hwMap) {
        drive.init(hwMap);
//        lift = hwMap.dcMotor.get("lift");
//        claw = hwMap.servo.get("claw");
        imu = hwMap.get(BNO055IMU.class, "imu");

//        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);
    }

 //   public double getLift() {
 //       return lift.getCurrentPosition();
 //   }

 //   public void setLift(double liftSpeed) {
 //       lift.setPower(liftSpeed);
 //   }

 //   public void setClaw(double clawPosit) {
 //       claw.setPosition(clawPosit);
 //   }

    public ArrayList<TestItem> getTests() {
        ArrayList<TestItem> tests = new ArrayList<>();
        tests.add(new TestMotor("FrontRight", 0.5, drive.motorFrontRight));
        tests.add(new TestMotor("FrontLeft", 0.5, drive.motorFrontLeft));
        tests.add(new TestMotor("BackRight", 0.5, drive.motorBackRight));
        tests.add(new TestMotor("BackLeft", 0.5, drive.motorBackLeft));
//        tests.add(new TestMotor("Lift", 0.5, lift));
//        tests.add(new TestServo("Claw", claw, 0.0, 1.0));
        return tests;
    }

    public void driveFieldRelative(double y, double x, double rx) {
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        double theta = Math.atan2(y, x);
        double r = Math.hypot(y, x);
        theta = AngleUnit.normalizeRadians(theta - orientation.firstAngle);
        double newY = r * Math.sin(theta);
        double newX = r * Math.cos(theta);
        drive.drive(newY, newX, rx);
    }
}