package OpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Subsystems.Lift;

@TeleOp
public class TestWiring extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Lift lift = new Lift(hardwareMap);

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("is pressed", lift.isPressed());
            telemetry.update();
        }
    }
}
