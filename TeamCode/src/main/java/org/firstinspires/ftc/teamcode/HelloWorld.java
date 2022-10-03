package org.firstinspires.ftc.teamcode;

//imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode ;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
//@Autonomous
public class HelloWorld extends OpMode {

    @Override
    public void init() {
        //System.out.println("Hello World")
        telemetry.addData("Hello", "Natalia");
    }

    @Override
    public void loop() {
    }
}