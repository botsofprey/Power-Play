package org.firstinspires.ftc.teamcode.autoAndTeleOpDriveClasses;

public class driveMode {
    public int driveMode;
    private int normalMode;
    private int fastMode = normalMode * 2;
    private int slowMode = driveMode / 2;

    public driveMode(int normal ,String mode) {
        this.normalMode = normal;
        switchDriveMode(mode);
    }

    public void changeDriveMode(String mode) {
        switchDriveMode(mode);
    }

    private void switchDriveMode(String mode) {
        switch (mode) {
            case "normal":
                driveMode = normalMode;
                break;
            case "fast":
                driveMode = fastMode;
                break;
            case "slow":
                driveMode = slowMode;
                break;
        }
    }
}
