package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import DriveEngine.MecanumDrive;
import OpMode.Autonomous.LeftToLeft;
import UtilityClasses.Location;

public class ThreeTrackingWheelLocalizer {
    public <T> ThreeTrackingWheelLocalizer(List<T> asList) {
    }

    public ThreeTrackingWheelLocalizer(HardwareMap hardwareMap, Location location, LeftToLeft leftToLeft, MecanumDrive drive) {

    }

    public void update() {
    }

    public String leftEncoder() {
        return null;
    }

    public String rightEncoder() {
        return null;
    }

    public String frontEncoder() {
        return null;
    }

    public void setTargetPosition(int x, int y) {
    }
}
