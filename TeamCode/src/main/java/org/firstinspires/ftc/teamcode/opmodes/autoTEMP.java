package org.firstinspires.ftc.teamcode.opmodes;

//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//easyopencv imports
//teamcode imports
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;

@Autonomous()
public class autoTEMP extends OpMode {
    cameraControl autocam;

    @Override
    public void init() {
        autocam = new cameraControl();
        //call createCameraInstance
        autocam.createCameraInstance(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        autocam.camera.getFps();
    }

    @Override
    public void stop() {
        autocam.destroyCameraInstance();
    }
}
