package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Encoder;

public class motorProgrammingBoard {
    public DcMotor motorFrontLeft, motorBackRight,
                   motorBackLeft, motorFrontRight;

    public Encoder odomLeft, odomRight, odomMiddle;

    public BNO055IMU imu;

    public void init(HardwareMap hwMap) {
        //get yellowjacket motors
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        //get imu
        imu = hwMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        //get odom
        odomLeft = new Encoder(hwMap.get(DcMotorEx.class, "odomLeft"));
        odomRight = new Encoder(hwMap.get(DcMotorEx.class, "odomRight"));
        odomMiddle = new Encoder(hwMap.get(DcMotorEx.class, "odomMiddle"));

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void getOdom(HardwareMap hwMap) {

    }
}

