package Subsystems;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class ThreeTrackingWheelLocalizer {
    public <T> ThreeTrackingWheelLocalizer(List<T> asList) {
    }

    //frontEncoder.setDirection(Encoder.Direction.REVERSE);
    @NonNull
    public abstract List<Double> getWheelVelocities();

    public abstract String leftEncoder();

    public abstract String rightEncoder();

    public abstract String frontEncoder();

    public abstract void getMoveToTargetPostion(int x, int y);
}
