package org.firstinspires.ftc.teamcode.opencvCamera;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

public class opencvPipeline extends OpenCvPipeline {
    Mat wholeScreen;

    @Override
    public void init(Mat firstFrame) {
        //wholescreen
    }

    @Override
    public Mat processFrame(Mat input) {
        return input;
    }
}
