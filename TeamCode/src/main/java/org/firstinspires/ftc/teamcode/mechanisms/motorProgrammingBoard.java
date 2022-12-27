package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class motorProgrammingBoard {
    private double ticksPerRotation;
    public DcMotor motorFrontLeft, motorBackRight,
                   motorBackLeft, motorFrontRight,
                   odomLeft, odomRight, odomMiddle;

    public void init(HardwareMap hwMap) {
        //get yellowjacket motors
        motorFrontLeft = hwMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hwMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hwMap.dcMotor.get("motorBackLeft");
        motorBackRight = hwMap.dcMotor.get("motorBackRight");
        //get odom motors
        odomLeft = hwMap.dcMotor.get("odomLeft");
        odomRight = hwMap.dcMotor.get("odomRight");
        odomMiddle = hwMap.dcMotor.get("odomMiddle");

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}

