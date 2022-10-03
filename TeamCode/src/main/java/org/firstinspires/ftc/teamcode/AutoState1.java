package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ProgrammingBoard1;

@Autonomous()
public class AutoState1 extends OpMode {
    enum State {START, WAIT_FOR_SENSOR_RELEASE, WAIT_FOR_POT_TURN, STOP, DONE}

    ProgrammingBoard1 board = new ProgrammingBoard1();
    State state = State.START;

    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void start() {
        state = State.START;
    }

    @Override
    public void loop() {
        telemetry.addData("State", state);
        switch (state) {
            case START:
                board.setServoPosition(0.5);
                if (board.isTouchSensorPressed()) {
                    state = State.WAIT_FOR_SENSOR_RELEASE;
                }
                break;
            case WAIT_FOR_SENSOR_RELEASE:
                board.setServoPosition(0.0);
                if (!board.isTouchSensorPressed()) {
                    state = State.WAIT_FOR_POT_TURN;
                }
                break;
            case WAIT_FOR_POT_TURN:
                board.setServoPosition(1.0);
                board.setMotorSpeed(0.5);
                if (board.getPotAngle() > 90) {
                    state = State.STOP;
                }
                break;
            case STOP:
                board.setMotorSpeed(0.0);
                state = State.DONE;
                break;
            default:
                telemetry.addData("Auto", "Finished");
        }
    }
}
