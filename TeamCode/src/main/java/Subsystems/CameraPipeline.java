package Subsystems;
import android.provider.ContactsContract;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptTensorFlowObjectDetection;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;


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

        System.out.println("Matrixes: " + input.empty() + ", " + image.empty());

        if(input.empty())
            return null;

        //Core.rotate(input, image, Core.ROTATE_90_COUNTERCLOCKWISE);
        points = new Mat();
        image = input;

        System.out.println("1 I did this");

        //image = input.submat(rect);
        if(detector.detect(image, points))
            data = detector.decode(image, points);

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

    public String getLink(){
        return data;
    }
    public int getParking(){
        return parking;
    }

}



