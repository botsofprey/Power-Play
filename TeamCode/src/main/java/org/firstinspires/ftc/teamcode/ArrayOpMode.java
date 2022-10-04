package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;

@TeleOp()
public class ArrayOpMode extends OpMode {
    ArrayList<String> lines = new ArrayList<>();
    int lineIndex;
    double DELAY_SECS = 0.5;

    double nextTime;

    @Override
    public void init() {
        lineIndex = 0;
        lines.clear();
        lines.add("Zeroth Line");
        lines.add("First Line");
        lines.add("Second Line");
        lines.add("Third Line");
        lines.add("Fourth Line");
        lines.add("Fifth Line");
        lines.add("And Then Restart");
    }

    @Override
    public void loop() {
        if (nextTime < getRuntime()) {
            lineIndex++;
            if (lineIndex >= lines.size()) {
                lineIndex = 0;
            }
            nextTime = getRuntime() + DELAY_SECS;
        }
        telemetry.addLine(lines.get(lineIndex));
    }

}
