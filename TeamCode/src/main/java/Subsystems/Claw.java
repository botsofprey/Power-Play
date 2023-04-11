package Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo left;

    private Servo right;
    public static double CLOSE_POSITION_LEFT = 0.5;
    public static double CLOSE_POSITION_RIGHT = 0.5;
    public static double OPEN_POSITION_LEFT = 1.0;
    public static double OPEN_POSITION_RIGHT = 0.35;
    public static final double OPEN_POSITION = 0.0, CLOSE_POSITION = 1.0;

    public Claw(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, "leftClaw");
        right = hardwareMap.get(Servo.class, "rightClaw");
    }

    public void open() {
        left.setPosition(OPEN_POSITION_LEFT);
        right.setPosition(OPEN_POSITION_RIGHT);
    }

    public void close() {
        left.setPosition(CLOSE_POSITION_LEFT);
        right.setPosition(CLOSE_POSITION_RIGHT);
    }

    /**
     * Set the position of the claw where 0 is open and 1 is closed and a decimal is in between
     * @param position 0..1 position of the claw between open and closed
     */
    public void setPosition(double position) {
        left.setPosition((1 - position) * OPEN_POSITION_LEFT + position * CLOSE_POSITION_LEFT);
        right.setPosition((1 - position) * OPEN_POSITION_RIGHT + position * CLOSE_POSITION_RIGHT);
    }

    public double getPosition() {
        return right.getPosition();
    }

    public Servo getLeft() {
        return left;
    }

    public Servo getRight() {
        return right;
    }
}
