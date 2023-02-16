package OpMode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Subsystems.AprilTagCamera;
import Subsystems.Claw;
import Subsystems.ClawArm;
import Subsystems.Lift;
import UtilityClasses.Controller;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous (name="Right Auto RR", group = "Autonomous")
public class RightAutoRoadrunner extends LinearOpMode {
    private Lift lift;
    private ClawArm clawArm;
    private Claw claw;


    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagCamera camera = new AprilTagCamera(hardwareMap);

        lift = new Lift(hardwareMap);
        clawArm = new ClawArm(hardwareMap);
        claw = new Claw(hardwareMap);
    }
}
