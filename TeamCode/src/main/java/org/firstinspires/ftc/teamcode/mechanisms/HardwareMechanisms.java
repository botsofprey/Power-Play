package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.tests.TestItem;
import org.firstinspires.ftc.teamcode.tests.TestMotor;
import org.firstinspires.ftc.teamcode.tests.TestServo;
import org.firstinspires.ftc.teamcode.vars.StaticImu;

import java.util.ArrayList;

/**
 * This class acts as a common way for the codes to access and interact with the robot's hardware
 */
public class HardwareMechanisms {

    MecanumDrive drive = new MecanumDrive();

    public DcMotor lift;

    public Servo claw;

    static BNO055IMU imu;

//    TouchSensor limitSwitch;

    public void init(HardwareMap hwMap) {
        drive.init(hwMap);
        lift = hwMap.dcMotor.get("lift");
        claw = hwMap.servo.get("claw");
        imu = hwMap.get(BNO055IMU.class, "imu");

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setTargetPosition(0);
        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);
    }

    public double getLift() {
        return lift.getCurrentPosition();
    }

    /**
     * A method used to control the lift using Run To Position
     *
     * @param position The target position
     */
    public void setLift(int position) {
        lift.setTargetPosition(position);
        lift.setPower(1);
    }

    /**
     * A method that sets the claw's position
     *
     * @param clawPosit The target position
     */
    public void setClaw(double clawPosit) {
        claw.setPosition(clawPosit);
    }

    public double getClaw() {
        return claw.getPosition();
    }

    /**
     * A method that obtains the imu's heading
     *
     * @param angleUnit Determines which angle unit is used
     * @return The imu's heading in the chosen angle
     */
    public double getHeading(AngleUnit angleUnit) {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, angleUnit);
        return angles.firstAngle + StaticImu.imuStatic;
    }

    public double getNormalizedDegrees() {
        return AngleUnit.normalizeDegrees((getHeading(AngleUnit.RADIANS) * 180) / Math.PI);
    }

    public void sleep(long milliseconds) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < milliseconds) {
            Thread.yield();
        }
    }

//    public boolean getLimitSwitch() {
//        return limitSwitch.isPressed();
//    }

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