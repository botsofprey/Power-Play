package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13, 18)
                .setConstraints(60, 40, 10, Math.toRadians(180), 10.5553)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(40.5, 63.5, Math.toRadians(270)))
                                //first cone
                                .splineToLinearHeading(new Pose2d(12, 58.5, toRAD(270)), toRAD(270))
                                .addTemporalMarker(() -> {
                                })
                                .splineToSplineHeading(new Pose2d(12, 11.5, toRAD(270)), toRAD(90))
                                .splineToLinearHeading(new Pose2d(24.5, 11.5, toRAD(270)), toRAD(0))
                                .addTemporalMarker(() -> {
                                })
                                .waitSeconds(0.25)
                                .splineToLinearHeading(new Pose2d(26.5, 7.5, toRAD(270)), toRAD(90))
                                .waitSeconds(0.1)
                                .addTemporalMarker(() -> {
                                })
                                //second cone
                                .setReversed(true)
                                .waitSeconds(0.1)
                                .addTemporalMarker(() -> {
                                })
                                .waitSeconds(0.25)
                                .lineToLinearHeading(new Pose2d(28, 10, toRAD(270)))
                                .splineToLinearHeading(new Pose2d(35, 1, toRAD(270)), toRAD(180))
                                .splineToLinearHeading(new Pose2d(63.5, 12, Math.toRadians(0)), toRAD(0))
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.25)
                                .addTemporalMarker(() -> {

                                })
                                .splineToSplineHeading(new Pose2d(22.5, 12.5, toRAD(270)), toRAD(0))
                                .addTemporalMarker(() -> {
                                })
                                .splineToLinearHeading(new Pose2d(26.5, 7.25, toRAD(270)), toRAD(0))
                                .waitSeconds(0.3)
                                .addTemporalMarker(() -> {

                                })
                                .waitSeconds(0.25)
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