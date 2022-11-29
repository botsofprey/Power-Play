package org.firstinspires.ftc.teamcode.opencvCamera;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

public class opencvPipeline extends OpenCvPipeline {

    @Override
    public void init(Mat firstFrame) {

    }

    @Override
    public Mat processFrame(Mat input) {
        return input;
    }
}
