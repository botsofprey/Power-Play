package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous
public class TurnTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0,0), this, drive);

        Controller con = new Controller(gamepad1);
        boolean left = true;

        boolean turning = false;

        waitForStart();

        drive.rotatewithPower(.25, -.25);

        while(opModeIsActive()){
            odom.update();

            if(Math.abs(odom.positionLocation.angle) % 90 > 1)
                turning = true;

            telemetry.addData("Position", odom.positionLocation.toString(4));
            telemetry.addData("left", odom.getCurrentLeftPos());
            telemetry.addData("right", odom.getCurrentRightPos());
            telemetry.addData("aux", odom.getCurrentAuxPos());
            telemetry.addData("Vert diff", odom.getVerticalDiff());
            telemetry.update();

            con.update();
            if(con.leftPressed) {
                left = true;

                drive.rotatewithPower(.25, -.25);
            }
            else if(con.rightPressed) {
                left = false;
                drive.rotatewithPower(-.25, .25);
            }

            if(turning && Math.abs(odom.positionLocation.angle) % 90. <= 1){
                drive.brake();
                System.out.println("Position has stopped at: " + odom.positionLocation.toString(4));
                System.out.println("Position has stopped while turning left: " + left);

                sleep(5000);
                if(left)
                    drive.rotatewithPower(.25, -.25);
                else
                    drive.rotatewithPower(-.25, .25);
            }
        }
    }
}
