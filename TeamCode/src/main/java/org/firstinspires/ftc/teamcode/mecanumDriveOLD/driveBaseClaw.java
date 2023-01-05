package org.firstinspires.ftc.teamcode.mecanumDriveOLD;

public class driveBaseClaw {
    public static int clawPercent;

    //open claw
    public int openClaw(int percentOpened) {
        clawPercent += percentOpened;
        if ((percentOpened < 0 || percentOpened > 100) || (clawPercent == 0 || clawPercent == 100)) {
            clawPercent -= percentOpened;
            return 1;
        }
        return clawPercent;
    }
    //close claw
    public int closeClaw(int percentClosed) {
        clawPercent -= percentClosed;
        if ((percentClosed < 0 || percentClosed > 100) && (clawPercent == 0 || clawPercent == 100)) {
            clawPercent += percentClosed;
            return 1;
        }
        return clawPercent;
    }
}
