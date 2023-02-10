package org.firstinspires.ftc.teamcode.DistanceSensor;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class angleFromDistance {

    //in millimeters
    public final double DISTANCE_TO_DISTANCE_LENGTH = 0;

    public DistanceSensor FL, FR, BL, BR;

    private Telemetry telem;

    public angleFromDistance(HardwareMap hwMap, Telemetry telem) {
        FL = hwMap.get(DistanceSensor.class, "DistanceFrontLeft");
        FR = hwMap.get(DistanceSensor.class, "DistanceFrontRight");
        BL = hwMap.get(DistanceSensor.class, "DistanceBackLeft");
        BR = hwMap.get(DistanceSensor.class, "DistanceBackRight");
        this.telem = telem;
    }

    public double getAngleOfRobot() {

       double angle = 0,
              FLdistance,
              FRdistance,
              BLdistance,
              BRdistance;

       String sideOfInterest;

       FLdistance = FL.getDistance(DistanceUnit.MM);
       FRdistance = FL.getDistance(DistanceUnit.MM);
       BLdistance = FL.getDistance(DistanceUnit.MM);
       BRdistance = FL.getDistance(DistanceUnit.MM);

       if (FLdistance == 0 || BLdistance == 0)
           sideOfInterest = "Right";
       else if (FRdistance == 0 || BRdistance == 0)
           sideOfInterest = "Left";
       else {
           telem.addLine("Couldn't find a wall.");
           return 1;
       }

       if (sideOfInterest == "Right")
           angle = calcAngle(FRdistance, BRdistance);
       else if (sideOfInterest == "Left")
           angle = calcAngle(FLdistance, BLdistance);

       return angle;
    }

    private double calcAngle(double d1, double d2) {
        double leg;
        double result = 0;

        leg = d1 - d2;

        result += leg < 0 ? 0 : 360;

        result += Math.toDegrees(Math.atan2(leg, DISTANCE_TO_DISTANCE_LENGTH));
        return result;
    }
}