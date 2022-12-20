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
        autoDriveBase.setSlowMode();
        autoDriveBase.setDriveMode("normal");
        // remember: System.currentTimeMillis();
    }
    @Override
    public void loop() {
        double[] moveReturn = autoDriveBase.moveRobotAUTO(180,0,0);
        mpb.motorFrontLeft.setTargetPosition((int)(moveReturn[0]));
        mpb.motorFrontRight.setTargetPosition((int)(moveReturn[1]));
        mpb.motorBackLeft.setTargetPosition((int)(moveReturn[2]));
        mpb.motorBackRight.setTargetPosition((int)(moveReturn[3]));
    }
}

