package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class FirstPipeline extends OpenCvPipeline {
    public Scalar nonSelectedColor = new Scalar(0, 255, 0);
    public Scalar selectedColor = new Scalar(0, 0, 255);

    public Rect rect1 = new Rect(110, 42, 40, 40);
    public Rect rect2 = new Rect(160, 42, 40, 40);
    public Rect rect3 = new Rect(210, 42, 40, 40);
    public int selectedRect = 0;


    Mat hsvMat = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        selectedRect = findRectangle(input);
        drawRectangles(input);

        return input;
    }

    int findRectangle(Mat input) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        double satRect1 = getAvgSaturation(hsvMat, rect1);
        double satRect2 = getAvgSaturation(hsvMat, rect2);
        double satRect3 = getAvgSaturation(hsvMat, rect3);


        if ((satRect1 > satRect2) && (satRect1 > satRect3)) {
            return 1;
        } else if ((satRect2 > satRect1) && (satRect2 > satRect3)) {
            return 2;
        }
        return 3;
    }

    protected double getAvgSaturation(Mat input, Rect rect) {
        Mat submat = input.submat(rect);
        Scalar color = Core.mean(submat);
        return color.val[1];
    }

    public void drawRectangles(Mat input) {
        Imgproc.rectangle(input, rect1, nonSelectedColor);
        Imgproc.rectangle(input, rect2, nonSelectedColor);
        Imgproc.rectangle(input, rect3, nonSelectedColor);

        switch (selectedRect) {
            case 1:
                Imgproc.rectangle(input, rect1, selectedColor);
                break;
            case 2:
                Imgproc.rectangle(input, rect2, selectedColor);
                break;
            case 3:
                Imgproc.rectangle(input, rect3, selectedColor);
                break;
        }
    }
}
