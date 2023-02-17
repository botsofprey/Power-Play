package org.firstinspires.ftc.teamcode.util;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

public class opencvPipeline extends OpenCvPipeline {
    public Mat wholeScreen;
    public Mat subWholeScreenMat;

    @Override
    public Mat processFrame(Mat input) {
        return input;
    }
}