package org.firstinspires.ftc.teamcode.opmodes;
//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//easyopencv imports
//teamcode imports
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;
import org.firstinspires.ftc.teamcode.opencvCamera.AprilTagPipelineEXAMPLECOPY;
import org.firstinspires.ftc.teamcode.driveBase.driveBaseMovement;
import org.firstinspires.ftc.teamcode.mechanisms.motorProgrammingBoard;

@Autonomous()
public class autodriveTEST extends OpMode{
    motorProgrammingBoard mpb = new motorProgrammingBoard();
    driveBaseMovement autoDriveBase = new driveBaseMovement();
    double[] moveRobotReturn = new double[4];

    @Override
    public void init() {
        mpb.init(hardwareMap);
        autoDriveBase.normalMode = 2;
        autoDriveBase.setFastMode();
        autoDriveBase.setDriveMode("normal");
    }
    @Override
    public void loop() {

    }
}

