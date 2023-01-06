package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;

@TeleOp()
public class TestOdomDirections extends OpMode {
    private Encoder leftEncoder, rightEncoder, frontEncoder;
    @Override
    public void init() {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftEncoder"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightEncoder"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "frontEncoder"));
        frontEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    @Override
    public void loop() {
        telemetry.addData("front value", frontEncoder.getCurrentPosition());
        telemetry.addData("right value", rightEncoder.getCurrentPosition());
        telemetry.addData("left value", leftEncoder.getCurrentPosition());
    }
}
