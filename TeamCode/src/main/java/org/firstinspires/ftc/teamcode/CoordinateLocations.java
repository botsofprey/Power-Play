package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

//the coordinate plane is from the red alliance's perspective
public class CoordinateLocations {
   //robot starting positions:
   double startY = 63.75;

   //Starting positions for robot
   Pose2d leftBlueStart  = new Pose2d(0, startY, 180), //Quadrant 1
          rightBlueStart = new Pose2d(-0, startY, 180), //Quadrant 2
          leftRedStart   = new Pose2d(-0, -startY, 0), //Quadrant 3
          rightRedStart  = new Pose2d(28.5, -startY, 0); //Quadrant 4

   //cone stack coordinates:
   Vector2d leftBlueConeStack  = new Vector2d(66, -12), //Quadrant 1
            rightBlueConeStack = new Vector2d(-66,12), //Quadrant 2
            leftRedConeStack   = new Vector2d(-66, -12), //Quadrant 3
            rightRedConeStack  = new Vector2d(66, -12); //Quadrant 4

   //Junction coordinates
   Vector2d leftHighJunc          = new Vector2d(),
            rightHighJunc         = new Vector2d(),
            topHighJunc           = new Vector2d(),
            bottomHighJunc        = new Vector2d(),
            topLeftMidJunc        = new Vector2d(),
            topRightMidJunc       = new Vector2d(),
            bottomLeftMidJunc     = new Vector2d(),
            bottomRightMidJunc    = new Vector2d(),
            topLeftLowJunc        = new Vector2d(),
            topRightLowJunc       = new Vector2d(),
            topMidLeftLowJunc     = new Vector2d(),
            topMidRightLowJunc    = new Vector2d(),
            bottomMidLeftLowJunc  = new Vector2d(),
            bottomMidRightLowJunc = new Vector2d(),
            bottomLeftLowJunc     = new Vector2d(),
            bottomRightLowJunc    = new Vector2d(),
            topLeftGroundJunc     = new Vector2d(),
            topMidGroundJunc      = new Vector2d(),
            topRightGroundJunc    = new Vector2d(),
            midLeftGroundJunc     = new Vector2d(),
            midMidGroundJunc      = new Vector2d(),
            midRightGroundJunc    = new Vector2d(),
            bottomLeftGroundJunc  = new Vector2d(),
            bottomMidGroundJunc   = new Vector2d(),
            bottomRightGroundJunc = new Vector2d();


}
