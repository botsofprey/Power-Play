package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ProgrammingBoard1;

@Autonomous()
public class Chapter12Practice1 extends OpMode {
    enum State {START, SECOND_STEP, THIRD_STEP, FOURTH_STEP, DONE}

    ProgrammingBoard1 board = new ProgrammingBoard1();
    State state = State.START;
    double lastTime;

    @Override()
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void start() {
        state = State.START;
        resetRuntime();
        lastTime = getRuntime();
    } @Override
    public void loop() {
        telemetry.addData("State", state);
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Time in State", getRuntime() - lastTime);
        switch (state) {
            case START:
                board.setMotorSpeed(0.25);
                if (getRuntime() >= 0.25) {
                    state = State.SECOND_STEP;
                    lastTime = getRuntime();
                }
                break;
            case SECOND_STEP:
                board.setMotorSpeed(0.5);
                if (getRuntime() >= lastTime + 0.25) {
                    state = State.THIRD_STEP;
                    lastTime = getRuntime();
                }
                break;
            case THIRD_STEP:
                board.setMotorSpeed(0.75);
                if (getRuntime() >= lastTime + 0.25) {
                    state = State.FOURTH_STEP;
                    lastTime = getRuntime();
                }
            case FOURTH_STEP:
                board.setMotorSpeed(1);
                if (board.isTouchSensorPressed()) {
                    state = State.DONE;
                    lastTime = getRuntime();
                }
                break;
            default:
                telemetry.addData("Auto", "Finished");
                board.setMotorSpeed(0);
        }
    }
}

