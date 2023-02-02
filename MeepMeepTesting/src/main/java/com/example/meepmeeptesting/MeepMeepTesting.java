package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 30, 10, Math.toRadians(180), 10.5553)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-40.5,64.5,270))
                                //preload
                                .lineToLinearHeading(new Pose2d(-38, 64.5, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270)))
                                .turn(Math.toRadians(45))
                                .lineToLinearHeading(new Pose2d(-24 - 5, 0 + 5, Math.toRadians(315)))
                                .waitSeconds(1.5)

                                //getconeandscore
                                .lineToLinearHeading(new Pose2d(-36,12, Math.toRadians(315)))
                                .turn(Math.toRadians(-135))
                                .lineToLinearHeading(new Pose2d(-64, 12, Math.toRadians(180)))
                                .waitSeconds(0.5)
                                .waitSeconds(1)
                                .waitSeconds(0.5)
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(180)))
                                .turn(Math.toRadians(135))
                                .lineToLinearHeading(new Pose2d(-24 - 6, 0 + 6, Math.toRadians(315)))
                                .waitSeconds(0.75)
                                .waitSeconds(0.5)

                                //getconeandscore
                                .lineToLinearHeading(new Pose2d(-36,12, Math.toRadians(315)))
                                .turn(Math.toRadians(-135))
                                .lineToLinearHeading(new Pose2d(-64, 12, Math.toRadians(180)))
                                .waitSeconds(0.5)
                                .waitSeconds(1)
                                .waitSeconds(0.5)
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(180)))
                                .turn(Math.toRadians(135))
                                .lineToLinearHeading(new Pose2d(-24 - 6, 0 + 6, Math.toRadians(315)))
                                .waitSeconds(0.75)
                                .waitSeconds(0.5)

                                //park at 19
                                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270)))
                                .lineToLinearHeading(new Pose2d(-60, 12, Math.toRadians(270)))
                                .build()
                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}