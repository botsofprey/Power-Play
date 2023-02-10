package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 40, 10, Math.toRadians(180), 10.5553)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-40.5,64.5,270))
                                //preload
                                .addSpatialMarker(new Vector2d(-40.6, 64), () -> {})
                                .lineToLinearHeading(new Pose2d(-36, 64.5, toRAD(270)))
                                .lineToSplineHeading(new Pose2d(-36, 30, toRAD(270)))
                                .splineToSplineHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270 + 45))
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)

                                //getconeandscore
                                .back(4)
                                .splineToLinearHeading(new Pose2d(-36, 12, toRAD(180)), toRAD(180))
                                .lineToSplineHeading(new Pose2d(-64, 12, toRAD(180)))
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)
                                .lineToSplineHeading(new Pose2d(-36, 12, toRAD(180)))
                                .splineToLinearHeading(new Pose2d(-24 - 9, 9, toRAD(270 + 45)), toRAD(270+ 45))
                                .splineToLinearHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270+ 45))

                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)

                                //getconeandscore2
                                .back(4)
                                .splineToLinearHeading(new Pose2d(-36, 12, toRAD(180)), toRAD(180))
                                .lineToSplineHeading(new Pose2d(-64, 12, toRAD(180)))
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)
                                .lineToSplineHeading(new Pose2d(-36, 12, toRAD(180)))
                                .splineToLinearHeading(new Pose2d(-24 - 9, 9, toRAD(270 + 45)), toRAD(270+ 45))
                                .splineToLinearHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270+ 45))
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)

                                //getconeandscore3
                                /**/.back(4)
                                .splineToLinearHeading(new Pose2d(-36, 12, toRAD(180)), toRAD(180))
                                .lineToSplineHeading(new Pose2d(-64, 12, toRAD(180)))
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)
                                .lineToSplineHeading(new Pose2d(-36, 12, toRAD(180)))
                                .splineToLinearHeading(new Pose2d(-24 - 9, 9, toRAD(270 + 45)), toRAD(270+ 45))
                                .splineToLinearHeading(new Pose2d(-24 - 6, 6, toRAD(270 + 45)), toRAD(270+ 45))

                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.25)/**/
                                /*.back(3)
                                .splineToLinearHeading(new Pose2d(-36, 12, toRAD(180)), toRAD(180))
                                .lineToSplineHeading(new Pose2d(-64, 12, toRAD(180)))
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {})
                                .waitSeconds(0.5)
                                .lineToLinearHeading(new Pose2d(-48, 12, toRAD(90)))
                                .forward(5)
                                .waitSeconds(1)
                                .back(5)*/

                                //park17
                                .lineToLinearHeading(new Pose2d(-36, 12, toRAD(270 + 45)))
                                .turn(toRAD(-45))
                                .lineToLinearHeading(new Pose2d(-12,12, toRAD(270)))
                                .build()
                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    private static double toRAD(double deg) {
        return Math.toRadians(deg);
    }
}