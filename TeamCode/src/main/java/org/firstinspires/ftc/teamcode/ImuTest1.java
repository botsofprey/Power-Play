package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@TeleOp
public class ImuTest1 extends OpMode {
    HardwareMechanisms board = new HardwareMechanisms();


    @Override
    public void init() {
        board.init(hardwareMap);
    }

    @Override
    public void loop() {
        String filename = "ImuValue.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, String.valueOf(board.getHeading(AngleUnit.DEGREES)));
        ReadWriteFile.readFile(file);
        telemetry.addData("angle", ReadWriteFile.readFile(file));

    }
}
