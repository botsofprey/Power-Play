package Subsystems;

import android.os.Build;

import androidx.annotation.RequiresApi;

//import com.acmerobotics.roadrunner.geometry;

//import com.acmerobotics.roadrunner.geometry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValue;

import java.util.Arrays;

import DriveEngine.MecanumDrive;
import OpMode.Autonomous.LeftToLeft;
import UtilityClasses.Location;

@TeleOp //(name="Auto Test", group = "Autonomous")
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 0;
    public static double WHEEL_RADIUS = 2;
    public static double GEAR_RATIO = 1;

    public static double LATERAL_DISTANCE = 12;
    public static double FORWARD_OFFSET = 2; //idk how to measure it so heres a guess ig

    public EncodedValue leftEncoder;
    public EncodedValue rightEncoder;
    public EncodedValue frontEncoder;
    public Location location;
    public Object getLocation;
    public Object moveToTargetPostion;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap, Location location, LeftToLeft leftToLeft, MecanumDrive drive) throws Throwable {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0),
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0),
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90))
        ));

        leftEncoder = new EncodedValue(hardwareMap.get((byte[].class), "leftEncoder"));
        rightEncoder = new EncodedValue(hardwareMap.get((byte[].class), "rightEncoder"));
        frontEncoder = new EncodedValue(hardwareMap.get((byte[].class), "frontEncoder"));
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public void location(int x, int y) {
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

    private enum direction {
        x,
        y,
        angle,
        stationary
    }

    public Object getMoveToTargetPostion() {
        return moveToTargetPostion;
    }

    private static class Pose2d {
        public Pose2d(int i, double v, int i1) {
        }

        public Pose2d(double forwardOffset, int v, double toRadians) {
        }
    }
}
