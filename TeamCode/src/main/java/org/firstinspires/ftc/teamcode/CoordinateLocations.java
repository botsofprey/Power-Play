package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

//the coordinate plane is from the red alliance's perspective
public class CoordinateLocations {
   //robot starting positions:
   double startX;
   double startY = 63.75;
   Pose2d leftBlueStart = new Pose2d(startX, startY, 180); //Quadrant 1
   Pose2d rightBlueStart = new Pose2d(-startX, startY, 180); //Quadrant 2
   Pose2d leftRedStart = new Pose2d(-startX, -startY, 0); //Quadrant 3
   Pose2d rightRedStart = new Pose2d(startX, -startY, 0); //Quadrant 4
   //cone stack coordinates:
   Vector2d leftBlueConeStack = new Vector2d(66, -12); //Quadrant 1
   Vector2d rightBlueConeStack = new Vector2d(-66,12); //Quadrant 2
   Vector2d leftRedConeStack = new Vector2d(-66, -12); //Quadrant 3
   Vector2d rightRedConeStack = new Vector2d(66, -12); //Quadrant 4

}
