package OpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Subsystems.Claw;
import Subsystems.ClawArm;
import Subsystems.Lift;
import DriveEngine.MecanumDrive;
import UtilityClasses.Controller;

@TeleOp
public class TestWiring extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Lift lift = new Lift(hardwareMap);
        ClawArm arm = new ClawArm(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);

        Controller con1 = new Controller(gamepad1),
                    con2 = new Controller(gamepad2);

        drive.setCurrentSpeed(.5);
        boolean armUp = false;

        waitForStart();

        while(opModeIsActive()){
            drive.moveWithPower(
                    con1.leftStick.y + con1.leftStick.x + con1.rightStick.x,
                    con1.leftStick.y - con1.leftStick.x + con1.rightStick.x,
                    con1.leftStick.y + con1.leftStick.x - con1.rightStick.x,
                    con1.leftStick.y - con1.leftStick.x - con1.rightStick.x
            );

            if(con2.aPressed){
                if(!armUp){
                    arm.setPositionElbow(arm.UP_POSITION);
                    arm.setPositionWrist(arm.getWristPosition() == 1 ? 0 : 1);
                    armUp = true;
                } else{
                    arm.setPositionElbow(arm.DOWN_POSITION);
                    armUp = false;
                }
            }

            if(armUp){
                if(con2.rightTriggerHeld)
                    lift.setPower(con2.rightTrigger);
                else if(con2.leftTriggerHeld)
                    lift.setPower(-con2.leftTrigger);
            } else {
                if(con2.rightTriggerHeld)
                    lift.setPower(con2.rightTrigger);
                else if(con2.leftTriggerHeld)
                    lift.setPower(-con2.leftTrigger);
            }

            telemetry.update();
        }
    }
}
