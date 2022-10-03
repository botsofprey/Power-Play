package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.ProgrammingBoard1;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous()
public class Chapter12Practice2 extends OpMode {
    enum State {START, DONE}

    ProgrammingBoard1 board = new ProgrammingBoard1();
    State state = State.START;
    double lastTime;

    @Override()
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void start() {
        state = Chapter12Practice2.State.START;
        resetRuntime();
        lastTime = getRuntime();
    }

    @Override
    public void loop() {
        telemetry.addData("State", state);
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Time in State", getRuntime() - lastTime);
        switch (state) {
            case START:
                board.setMotorSpeed(0.25);
                if ((getRuntime() >= 5) || (board.getDistance(DistanceUnit.CM) < 10)) {
                    state = Chapter12Practice2.State.DONE;
                    lastTime = getRuntime();
                }
                break;
            default:
                telemetry.addData("Auto", "Finished");
                board.setMotorSpeed(0);
        }
    }
}