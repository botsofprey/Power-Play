package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(14, 17)
                .setConstraints(60, 30, 10, Math.toRadians(180), 10.5553)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-31.5, 64.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-38, 64.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270)))
                                .waitSeconds(0.75)
                                .turn(Math.toRadians(55))
                                .lineToLinearHeading(new Pose2d(-24 - 4.5, 3.5, Math.toRadians(330)))
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(330)))
                                .addTemporalMarker(() -> {

                                })
                                .turn(Math.toRadians(-150))
                                .lineToLinearHeading(new Pose2d(-64, 12, Math.toRadians(180)))
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {

                                })
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(180)))
                                .turn(Math.toRadians(150))
                                .lineToLinearHeading(new Pose2d(-24 - 5, 3, Math.toRadians(330)))
                                .waitSeconds(0.75)
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.5)
                                .build()
                        );
        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}