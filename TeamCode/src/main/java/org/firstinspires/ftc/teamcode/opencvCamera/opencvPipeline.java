package org.firstinspires.ftc.teamcode.opencvCamera;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;

public class opencvPipeline extends OpenCvPipeline {
    Mat submat;

    @Override
    public void init(Mat initFrame) {
        submat = initFrame.submat();
    }
    @Override
    public Mat processFrame(Mat input) {
        return submat;
    }
}
