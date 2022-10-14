package Subsystems;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvPipeline;


public class CameraPipeline extends OpenCvPipeline{

    private boolean init = true;

    private String[] qrOutput = {
    };

    private QRCodeDetector detector;
    private Mat points = new Mat();
    private Mat image = new Mat();

    public volatile String data;

    private int parking = -1;

    @Override
    public Mat processFrame(Mat input) {
        System.out.println("1 I did this");


        //Core.rotate(input, image, Core.ROTATE_90_COUNTERCLOCKWISE);
        points = new Mat();
        detector  = new QRCodeDetector();

        Imgproc.cvtColor(input, image, Imgproc.COLOR_BGR2GRAY);
        data = detector.detectAndDecode(image, points);
        System.out.println("2 I did this");

        if (!points.empty()) {
            //prints out qr code data
            System.out.println("Decoded data: " + data);

            //Creates a square around the qr code
            for (int i = 0; i < points.cols(); i++) {
                Point pt1 = new Point(points.get(0, i));
                Point pt2 = new Point(points.get(0, (i + 1) % 4));
                Imgproc.line(input, pt1, pt2, new Scalar(255, 0, 0), 3);
            }
        }

        System.out.println("3 I did this");

        switch (data){
            case "flowcode.com/p/yvA9cOb4I?fc=0":
                parking = 0;
                break;
            case "flowcode.com/p/yvAB5ZOnv?fc=0":
                parking = 1;
                break;
            case "flowcode.com/p/yvABUjyTs?fc=0":
                parking = 2;
                break;
            default:
                break;
        }


        System.out.println("4 I did this");

        return input;
    }

    public String getLink(){
        return data;
    }
    public int getParking(){
        return parking;
    }
}

