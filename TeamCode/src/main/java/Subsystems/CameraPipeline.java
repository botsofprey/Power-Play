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

    private int parking = -1;

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

        colorVision = Core.mean(image);

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

        System.out.println("3 I did this");

        System.out.println("4 I did this");

        frames++;
        if(System.currentTimeMillis() - startTime >= 1000){
            FRAMES_PER_SECOND = frames;
            frames = 0;
            startTime = System.currentTimeMillis();
        }

        double blueAmount = 0;
        Rect bluest = null;

        for(int x = 0; x < 16; x++){
            for(int y = 0; y < 16; y++){
                Rect r = new Rect(x * 40, y * 30, 40, 30);
                Mat sub = input.submat(r);

                if(Core.mean(sub).val[0] > blueAmount){
                    blueAmount = Core.mean(sub).val[0];
                    bluest = r;
                }
            }
        }

        Imgproc.rectangle(image, rect, new Scalar(0, 0, 255));

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



