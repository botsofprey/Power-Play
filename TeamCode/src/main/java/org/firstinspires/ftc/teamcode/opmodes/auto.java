package org.firstinspires.ftc.teamcode.opmodes;
//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//easyopencv imports
//teamcode imports
import org.firstinspires.ftc.teamcode.opencvCamera.cameraControl;

/*  This is the auto opMode. It is responsible for autonomous control of the robot during the first
**  section of a match.
*/

@TeleOp()
public class auto extends OpMode{
    cameraControl autocam = new cameraControl();
    @Override
    public void init() {
        autocam.createCameraInstance();

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {autocam.destroyCameraInstance();
    }
}
