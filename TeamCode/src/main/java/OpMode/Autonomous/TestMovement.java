package OpMode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.threeWheelOdometry;
import UtilityClasses.Controller;
import UtilityClasses.Location;

public class TestMovement extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Controller con = new Controller(gamepad1);

        MecanumDrive drive = new MecanumDrive(hardwareMap, 0);
        threeWheelOdometry odom = new threeWheelOdometry(hardwareMap, new Location(0, 0), this, drive);
        drive.setCurrentSpeed(.5);

        Location target = new Location(0, 0);

        while (!isStarted() && !isStopRequested()) {
            con.update();

            if (con.leftStick.x != 0) {
                double y = Math.round(con.leftStick.x) * 60;
                target.y = y;
            }
            if (con.leftStick.y != 0) {
                double x = Math.round(con.leftStick.y) * 60;
                target.x = x;
            }

            if (con.upPressed) {
                target.angle = 0;
            } else if (con.downPressed) {
                target.angle = 180;
            } else if (con.rightPressed) {
                target.angle = 90;
            } else if (con.leftPressed) {
                target.angle = -90;
            }

            if (con.bPressed) {
                target = new Location(0, 0, 0);
            }

            telemetry.addData("Target", target.toString());
            telemetry.update();
        }

        if (target.x != 0 || target.y != 0) {
            while (opModeIsActive()) {
                odom.setTargetPoint(target);
                while (!odom.atTarget() && opModeIsActive()) {
                    odom.update();

                    telemetry.addData("Target", target);
                    telemetry.addData("Position", odom.getLocation());
                    telemetry.addData("Powers", drive.getPowers());
                    telemetry.update();
                }

                odom.setTargetPoint(0, 0, 0);
                while (!odom.atTarget() && opModeIsActive()) {
                    odom.update();

                    telemetry.addData("Target", target);
                    telemetry.addData("Position", odom.getLocation());
                    telemetry.addData("Powers", drive.getPowers());
                    telemetry.update();
                }
            }
        } else {
            while (opModeIsActive()) {
                odom.rotateToAngle(target.angle);
                while (!odom.atTargetAngle() && opModeIsActive()) {
                    odom.update();

                    telemetry.addData("Target Angle", target.angle);
                    telemetry.addData("Position", odom.getLocation());
                    telemetry.addData("Powers", drive.getPowers());
                    telemetry.update();
                }

                odom.rotateToAngle(0);
                while (!odom.atTargetAngle() && opModeIsActive()) {
                    odom.update();

                    telemetry.addData("Target", target.angle);
                    telemetry.addData("Position", odom.getLocation());
                    telemetry.addData("Powers", drive.getPowers());
                    telemetry.update();
                }
            }

            drive.brake();
        }
    }
}
