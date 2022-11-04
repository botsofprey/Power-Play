package org.firstinspires.ftc.teamcode.opmodes;
//general imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
//easyopencv imports
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
//teamcode imports
import org.firstinspires.ftc.teamcode.driveBase.mainDriveBase;
import org.firstinspires.ftc.teamcode.mechanisms.ProgrammingBoard1;

/*  This is the auto opMode. It is responsible for autonomous control of the robot during the first
**  section of a match.
**  Teamcode files required for this program to function:
**      /org.firstinspires.ftc.teamcode
**          /drivebase
**              /mainDriveBase
**          /mechanisms
**              /ProgrammingBoard1
**          /opencvCamera
**               /opencvPipeline
 */

@TeleOp()
public class auto extends OpMode{
    @Override
    public void init() {
        //initialize live viewport
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
        "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //create webcam name variable (used to specify name of webcam)
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "");

        //use camera factory to create OpenCvCamera webcam instance
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName,
        cameraMonitorViewId);

        //connect to webcam
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            //initialize webcam
            @Override
            public void onOpened()
            {
                camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            //error return code
            @Override
            public void onError(int errorCode)
            {

            }
        });
    }
    @Override
    public void loop() {
        
    }
}
