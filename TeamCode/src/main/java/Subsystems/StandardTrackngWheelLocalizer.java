package Subsystems;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.Encoder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.easyopencv.PipelineRecordingParameters;

import java.util.Arrays;
import java.util.Base64;

public class StandardTrackngWheelLocalizer extends ThreeTrackingWheelLocalizer{
    public static double TICKS_PER_REV = 0;
    public static double WHEEL_RADIUS = 2;
    public static double GEAR_RATIO = 1;

    public static double LATERAL_DISTANCE = 12;
    public static double FORWARD_OFFSET = 2; //idk how to measure it

    private Base64.Encoder leftEncoder, rightEncoder, frontEncoder;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public StandardTrackngWheelLocalizer(HardwareMap hardwareMap) throws Throwable {
        super(Arrays.asList(
        new Pose2d(0, LATERAL_DISTANCE / 2,0),
        new Pose2d(0,-LATERAL_DISTANCE / 2,0),
        new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90))
        ));

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftEncoder"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightEncoder"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "frontEncoder"));
    }
    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    //frontEncoder.setDirection(Encoder.Direction.REVERSE);
    /*@NonNull
    @Override
    public List<Double> getWheelVelocities() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getRawVelocity()),
                encoderTicksToInches(rightEncoder.getRawVelocity()),
                encoderTicksToInches(frontEncoder.getRawVelocity())
        );
    }*/
}
