package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13, 16.5)
                .setConstraints(60, 40, 10, Math.toRadians(180), 10.5553)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(40.5, 63.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(40.5, 62.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(12, 62.5, Math.toRadians(270)))
                                .addTemporalMarker(() -> {

                                })
                                .lineToLinearHeading(new Pose2d(12, 11.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(21.5, 11.5, Math.toRadians(270)))
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.25)
                                .lineToLinearHeading(new Pose2d(21.5, 8.5, Math.toRadians(270)))
                                .waitSeconds(0.1)
                                .addTemporalMarker(() -> {

                                })

                .setReversed(true)
                .lineToLinearHeading(new Pose2d(21.5, 11.5, Math.toRadians(270)))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)
                .lineToLinearHeading(new Pose2d(58.5, 10.5, Math.toRadians(180)))
                .addTemporalMarker(() -> {

                })
                .waitSeconds(1)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {

                })
                .lineToLinearHeading(new Pose2d(21.5, 11.5, Math.toRadians(270)))
                .addTemporalMarker(() -> {
                })
                .lineToLinearHeading(new Pose2d(21.5, 7, Math.toRadians(270)))
                .waitSeconds(0.3)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)


                .addTemporalMarker(() -> {

                })
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(21.5, 11.5, Math.toRadians(270)))
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.35)
                .lineToLinearHeading(new Pose2d(58.5, 10.5, Math.toRadians(180)))
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.25)
                .lineToLinearHeading(new Pose2d(21.5, 11, Math.toRadians(270)))
                .addTemporalMarker(() -> {

                })
                .lineToLinearHeading(new Pose2d(21.5, 7, Math.toRadians(270)))
                .waitSeconds(0.25)
                .addTemporalMarker(() -> {

                })
                .waitSeconds(0.3)
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