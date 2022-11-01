package UtilityClasses;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class Camera {
    private OpenCvCamera camera;

    private boolean open = false;
    public volatile boolean isStopped = false;

    public Camera(HardwareMap hw, OpenCvPipeline pipeline) {
        WebcamName webcamName = hw.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hw.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        System.out.println("Camera pipeline camera: " + camera);

        camera.setPipeline(pipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                open = true;
            }

            @Override
            public void onError(int errorCode) {
            }
        });


    }

    public boolean isOpen() {
        return open;
    }

    public void stop() {
        if (!isStopped) {
            camera.closeCameraDeviceAsync(()->{});
            open = false;
            isStopped = true;
        }
    }
}
