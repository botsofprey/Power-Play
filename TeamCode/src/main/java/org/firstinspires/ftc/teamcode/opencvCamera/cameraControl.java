package org.firstinspires.ftc.teamcode.opencvCamera;

//easyopencv imports
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

//general imports
import org.firstinspires.ftc.teamcode.opmodes.auto;

/* This is the camera initialization class. It sets out how the webcam for the robot is to be
** set up for use. No pipelining is used here, this class only sets up and destroys camera instances
*/

public class cameraControl extends auto{
    //create necessary variables
    WebcamName webcamName;
    OpenCvCamera camera;

    public void createCameraInstance() {
        //initialize live viewport
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //create webcam name variable (used to specify name of webcam)
        webcamName = hardwareMap.get(WebcamName.class, "Webcam");

        //use camera factory to create OpenCvCamera webcam instance
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName,
            cameraMonitorViewId);

        //connect to webcam
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                //initialize webcam
                @Override
                public void onOpened() {
                    camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                    camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                }
                //error return code
                @Override
                public void onError(int errorCode) {
                    telemetry.addData("webcam","could not be opened");
                }
            }
        );
    }

    public void destroyCameraInstance() {
        camera.stopStreaming();
        camera = null;
        webcamName = null;
        System.gc();
    }
}