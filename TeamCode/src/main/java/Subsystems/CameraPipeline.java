package Subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvPipeline;


public class CameraPipeline extends OpenCvPipeline{

    private boolean init = true;

    private String[] qrOutput = {
    };

    //x - colors
    //y - bounds

    QRCodeDetector detector = new QRCodeDetector();

    private Mat points = new Mat();
    private Mat image = new Mat();

    public volatile String data;

    private int parking = 3;

    private Rect rect = new Rect(160, 42, 100, 100);
    private Scalar colorVision = new Scalar(0,255,0);

    private double startTime;
    private int frames;
    public int FRAMES_PER_SECOND;

    public CameraPipeline(){
        startTime = System.currentTimeMillis();
    }

    @Override
    public Mat processFrame(Mat input) {

        if(input.empty())
            return null;

        points = new Mat();
        image = input;


        System.out.println("1 I did this");

        Imgproc.rectangle(image, rect, colorVision);

        if(detector.detect(image, points)) {
            data = detector.decode(image, points);
        }

        if (!points.empty()) {
            //prints out qr code data
            System.out.println("Decoded data: " + data);

            //Creates a square around the qr code
            for (int i = 0; i < points.cols(); i++) {
                Point pt1 = new Point(points.get(0, i));
                Point pt2 = new Point(points.get(0, (i + 1) % 4));
                Imgproc.line(image, pt1, pt2, new Scalar(255, 0, 0), 3);
            }

            switch (data) {
                case "1":
                    parking = 0;
                    break;
                case "2":
                    parking = 1;
                    break;
                case "3":
                    parking =2;
                    break;
            }

            System.out.println("2 I did this");
        }

        if(image.empty())
            return input;

        return image;
    }

    public void processLast(){
        data = detector.detectAndDecode(image, points);
        switch (data) {
            case "1":
                parking = 0;
                break;
            case "2":
                parking = 1;
                break;
            case "3":
                parking = 2;
                break;
        }
    }

    public String getLink(){
        return data;
    }
    public int getParking(){
        return parking;
    }

}



