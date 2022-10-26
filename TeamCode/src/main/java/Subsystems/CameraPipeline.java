package Subsystems;
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

    private int parking = -1;

    private Rect rect = new Rect(160, 42, 40, 40);
    private Scalar colorVision = new Scalar(0,255,0);

    @Override
    public Mat processFrame(Mat input) {

        if(input.empty())
            return null;

        //Core.rotate(input, image, Core.ROTATE_90_COUNTERCLOCKWISE);
        points = new Mat();
        image = new Mat();

        System.out.println("1 I did this");

        data = detector.detectAndDecode(input, points);

        if (!points.empty()) {
            //prints out qr code data
            System.out.println("Decoded data: " + data);

            //Creates a square around the qr code
            for (int i = 0; i < points.cols(); i++) {
                Point pt1 = new Point(points.get(0, i));
                Point pt2 = new Point(points.get(0, (i + 1) % 4));
                Imgproc.line(image, pt1, pt2, new Scalar(255, 0, 0), 3);
            }

            System.out.println("2 I did this");
        }

        System.out.println("3 I did this");

        System.out.println("4 I did this");

        if(image.empty())
            return input;

        return image;
    }

    private Mat findCone(Mat input){
        //Filter out everything that isn't red
        Scalar minRed = new Scalar(255, 0, 0), maxRed = new Scalar(255, 115, 115);
        Core.inRange(input, minRed, maxRed, input);

        //Filter out noise
        Mat hierarchy = new Mat();
        List<MatOfPoint> matPoints = new ArrayList<>();
        Imgproc.findContours(input, matPoints, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

       Imgproc.drawContours(input, matPoints, -1, new Scalar(0, 255,0), 2);

       return input;
       
    }

    public String getLink(){
        return data;
    }
    public int getParking(){
        return parking;
    }

    public String getColor(){
        return colorVision.val[0] + ", " +colorVision.val[1] + ", " + colorVision.val[2];
    }
}



