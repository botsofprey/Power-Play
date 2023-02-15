package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import DriveEngine.MecanumDrive;
import UtilityClasses.Controller;

@TeleOp (name="Woboto Tele", group = "TeleOp")
public class WobotoTele extends LinearOpMode {
    MecanumDrive drive;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, 0);
        Controller con = new Controller(gamepad1);

        waitForStart();

        while(opModeIsActive()){
            con.update();

            if(con.leftTriggerPressed)
                drive.slowMode();
            else if (con.leftTriggerReleased)
                drive.slowMode();

            drive.moveWithPower(
                    con.leftStick.y + con.leftStick.x + con.rightStick.x,
                    con.leftStick.y - con.leftStick.x + con.rightStick.x,
                    con.leftStick.y + con.leftStick.x - con.rightStick.x,
                    con.leftStick.y - con.leftStick.x - con.rightStick.x
            );

            telemetry.addData("powers", drive.getPowers());
            telemetry.update();
        }
    }
}
