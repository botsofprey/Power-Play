package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
                        drive.trajectorySequenceBuilder(new Pose2d(-15, 63, Math.toRadians(90)))
                                .addTemporalMarker(()->{})
                                .waitSeconds(0.5)
                                .lineToConstantHeading(new Vector2d(-12, 12))
                                .turn(Math.toRadians(-45))
                                .lineToConstantHeading(new Vector2d(-5, 18))
                                .addTemporalMarker(()->{})
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