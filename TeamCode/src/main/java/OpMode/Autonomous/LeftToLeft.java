/*This teleop is for when the robot begins on the LEFT square-ish thing (audience
orentation)and is trying to go to the left same colored ?storage? (idk man whatever scores the
points)
//IT HAS TO START ON THE LEFT SIDE OF THE RED ALLIANCE FOR THIS TO WORK
actually what idk what im doing
i dont think it matters
it might matter if the qr code isnt found
cause it has to park in the terminal
 *//*
thought process
scan qr code
pick up THE cone
score it or just bring it with you to the storage triangle thing
     (if scoring prolly on the medium or low or even ground junction)
reverse back to the original starting position
go left into the half square
wait til the drivers drive around
ta-da all done
*/
//but you should ignore this because its prolly wrong
//actually its wrong
//maybe
package OpMode.Autonomous;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import DriveEngine.MecanumDrive;
import Subsystems.AprilTagCamera;
import Subsystems.StandardTrackingWheelLocalizer;
import UtilityClasses.Controller;
import UtilityClasses.Location;

@Autonomous (name = "AutonLeftToLeft", group = "Autonomous")
public abstract class LeftToLeft extends LinearOpMode {
    private MecanumDrive drive;
    private StandardTrackingWheelLocalizer odometry;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void runOpMode() throws InterruptedException{
        Controller con = new Controller(gamepad1);
        AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        drive = new MecanumDrive(hardwareMap, this, 0);
        try {
            odometry = new StandardTrackingWheelLocalizer(hardwareMap, new Location(0,0), this, drive);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        while (!isStarted() && !isStopRequested()) {
            odometry.update();
            telemetry.addData("leftEncoder", odometry.leftEncoder());
            telemetry.addData("rightEncoder", odometry.rightEncoder());
            telemetry.addData("frontEncoder", odometry.frontEncoder());
            //telemetry.addData("angle", odometry.());
            //telemetry.addData("rotation", odometry.());
            telemetry.addLine();
        }
        Location[] parkingLocations = {
                new Location (1),//= (60, -60), // tha 1st parking spot
                new Location (2), //= (60, 0), //tha 2nd parking spot
                new Location (3), //= (60, 60), //tha 3rd parking spot
                new Location (4) //= (0, 90)
        };

        boolean tagFound;

        int getParking;

        //parking space time
        telemetry.addData("Tag found", camera.tagFound());
        if(camera.tagFound()){
            telemetry.addData("Parking", camera.getParking()+1);
        }
        telemetry.addLine();
        telemetry.update();
        //assume it found the tag for my sake plz
    }
}