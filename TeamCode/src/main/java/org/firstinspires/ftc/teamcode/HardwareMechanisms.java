package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.ArrayList;

/**
 * This class acts as a common way for our codes to access and interact with the robot's hardware
 */
public class HardwareMechanisms {

    PIDCoefficients coeffs = new PIDCoefficients(0.01, 0, 0);
    PIDFController controller = new PIDFController(coeffs);

    MecanumDrive drive = new MecanumDrive();

    DcMotor lift;

    Servo claw;

    static BNO055IMU imu;

    public void init(HardwareMap hwMap) {
        drive.init(hwMap);
        lift = hwMap.dcMotor.get("lift");
        claw = hwMap.servo.get("claw");
        imu = hwMap.get(BNO055IMU.class, "imu");

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);
    }

    public double getLift() {
        return lift.getCurrentPosition();
    }

    public void setLiftPower(double liftPower) {
        lift.setPower(liftPower);
    }

    /**
     * A method used to control the lift using PIDF control
     *
     * @param position The target position
     */
    public void setLift(int position) {
        controller.setTargetPosition(position);
        double correction = controller.update(getLift());
        setLiftPower(correction);
    }

    public double getClaw() {
        return claw.getPosition();
    }

    public void setClaw(double clawPosit) {
        claw.setPosition(clawPosit);
    }

    /**
     * A method that obtains the imu's heading
     *
     * @param angleUnit Determines which angle unit is used
     * @return The imu's heading in the chosen angle
     */
    public double getHeading(AngleUnit angleUnit) {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, angleUnit);
        return angles.firstAngle + Static.imuValue;
    }

    /**
     * The method used in our TestWiring class, it builds off the TestItem, TestMotor, and TestServo classes
     *
     * @return The values applicable to the test listed, such as name and position
     */
    public ArrayList<TestItem> getTests() {
        ArrayList<TestItem> tests = new ArrayList<>();
        tests.add(new TestMotor("FrontRight", 0.5, drive.motorFrontRight));
        tests.add(new TestMotor("FrontLeft", 0.5, drive.motorFrontLeft));
        tests.add(new TestMotor("BackRight", 0.5, drive.motorBackRight));
        tests.add(new TestMotor("BackLeft", 0.5, drive.motorBackLeft));
        tests.add(new TestMotor("Lift", 0.5, lift));
        tests.add(new TestServo("Claw", claw, 0.0, 1.0));
        return tests;
    }

    /**
     * The method used for our driving, driving field relative is also known as true north driving, it allow us to drive in any direction with the robot facing any direction
     */
    public void driveFieldRelative(double y, double x, double rx) {
        double theta = Math.atan2(y, x);
        double r = Math.hypot(y, x);
        theta = AngleUnit.normalizeRadians(theta - getHeading(AngleUnit.RADIANS));
        double newY = r * Math.sin(theta);
        double newX = r * Math.cos(theta);
        drive.drive(newY, newX, rx);
    }
}