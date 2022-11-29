package org.firstinspires.ftc.teamcode.misc;

import java.io.*;

public class logger {
    //name of file to be logged
    String fileName;

    //state of the logger
    public static int loggerState;

    //determine whether logError is called recursively
    public static int logErrorLoop = 0;

    //start the logger
    public static void startLogger() {
        if (loggerState == 1)
            logError("startLogger");
        loggerState = 1;
    }

    //end the logger
    public static void endLogger() {
        if (loggerState == 0)
            logError("endLogger");


        loggerState = 0;

    }

    //log errors to the selected logfile
    public static void logError(String errorName) {
        if (loggerState == 0 && logErrorLoop == 0)
            logErrorLoop = 1;
            logError("logError");

        switch(errorName) {
            //driveBaseMovementMethods errors
            case "moveRobot1":
            case "moveRobot2":
            //driveBaseClaw errors
            case "openClaw":
            case "closeClaw":
            //logger errors
            case "startLogger":
            case "endLogger":
            case "logError":
            //cameraControl errors
            //opencvPipeline errors
        }
    }
}
