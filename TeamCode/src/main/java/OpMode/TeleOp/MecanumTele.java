package OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import DriveEngine.MecanumDrive;

@TeleOp(name="Basic TeelOp", group="TeleOp")
public class MecanumTele extends LinearOpMode {

    private MecanumDrive drive;
    private Gamepad gamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        gamepad = new Gamepad();

        while(opModeIsActive()){
           // gamepad.update();


        }
    }
}
