package org.firstinspires.ftc.teamcode.driveBase;

/*
* This is the driveBaseMovement class, which is responsible for auto and teleop
* robot movement calculations, and calculations only. This class is restricted to
* only calculations so that all active control flow is handled by the opmodes,
* so as to avoid issues with control flow and memory allocation (This class's
* functions must be initialized in an external object with its own address space,
* because we want to avoid side effects of previous operations being applied to other
* opmodes or any other instances of usage).
*
* **THIS CLASS HANDLES:**
* Odometry functions
* all auto movement functions excluding claw movement
* all teleop movement functions excluding claw movement
*
* **ANOTHER THING TO NOTE**
* The constants (or final variables, because Java is stupid) are specifically set
* satisfy a VERY SPECIFIC robot design. It is highly recommended to measure your own robot
* to determine these variables. The BLOCK_LENGTH and TICKS_PER_REV should be specifically
* noted to only work for the GoBilda Yellow Jacket 5202/3/4 series motors, however, the
* BLOCK_LENGTH is based on a conversion from (inches > centimeters > ticks) based on the
* notion that an FTC foam tile is 24 x 24 inches, or 60.69 x 60.69 centimeters. Simply
* convert centimeters to ticks and you are good to go!
*
* **FURTHER READING**
* All robot movements that are used in this class are based on GM0's mecanum drive
* documentation at
* https://gm0.org/en/latest/docs/software/tutorials/mecanum-drive.html
*/
public class driveBaseMovement {
    //the main variable that drive speed is stored in
    public float driveMode;
    //speed variables that are set to driveMode
    public int normalMode = 1;
    public int fastMode;
    public float slowMode;

    //amount of ticks in a single full revolution of the wheel
    public final double TICKS_PER_REV = 537.7;
    //the length of the 24x24 inch foam tiles on the field in ticks
    public final double BLOCK_LENGTH = 1082.029;
    //the amount of wheel turns required to fully turn the robot in ticks
    //(136.973 cm, the circumference a single wheel travels during a full
    // wheel turn, divided by the wheel circumference 30.159 cm, the robot
    //takes about 4.54 wheel rotations for a full 360 degree turn.)
    public final double COMPLETE_TURN_ROTS = 4.54;
    //
    public final double TICKS_PER_REV_ODOM = 0;
    //
    public final double COMPLETE_TURN_ROTS_ODOM = 0;

    public void setSlowMode() {
        slowMode = normalMode / 2;
    }

    public void setFastMode() {
        fastMode = normalMode * 2;
    }

    //set drive speed mode
    public void setDriveMode(String mode) {
        if (mode == "normal")
            driveMode = normalMode;
        else if (mode == "fast")
            driveMode = fastMode;
        else if (mode == "slow")
            driveMode = slowMode;
    }

    //move robot completely in teleop
    public double[] moveRobot(double turn, double forback, double strafe) {
        double frontLeftMotor = 0.0;
        double frontRightMotor = 0.0;
        double backLeftMotor = 0.0;
        double backRightMotor = 0.0;

        //turn robot
            frontLeftMotor -= (turn * driveMode);
            frontRightMotor += (turn * driveMode);
            backLeftMotor -= (turn * driveMode);
            backRightMotor += (turn * driveMode);

        //Move forward or backward
            frontLeftMotor += (forback * driveMode);
            frontRightMotor += (forback * driveMode);
            backLeftMotor += (forback * driveMode);
            backRightMotor += (forback * driveMode);

        //Strafe
            frontLeftMotor -= (strafe * driveMode);
            frontRightMotor += (strafe * driveMode);
            backLeftMotor += (strafe * driveMode);
            backRightMotor -= (strafe * driveMode);

        //return calculation results from pipeline for use in code
        return new double[]{frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};
    }

    //like moveRobot but for auto only
    public double[] moveRobotAUTO(double turnDEG, double forbackBLOCK, double strafeBLOCK) {
        double frontLeftMotorTICKS = 0.0;
        double frontRightMotorTICKS = 0.0;
        double backLeftMotorTICKS = 0.0;
        double backRightMotorTICKS = 0.0;

        //convert degrees into parts of a whole turn
        turnDEG = (turnDEG / 360) * (TICKS_PER_REV * COMPLETE_TURN_ROTS);

        //turn robot a certain amount of degrees
            frontLeftMotorTICKS -= (turnDEG * driveMode);
            frontRightMotorTICKS += (turnDEG * driveMode);
            backLeftMotorTICKS -= (turnDEG * driveMode);
            backRightMotorTICKS += (turnDEG * driveMode);

        //Move forward or backward a certain amount of blocks
            frontLeftMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH);
            frontRightMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH);
            backLeftMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH);
            backRightMotorTICKS += (forbackBLOCK * driveMode * BLOCK_LENGTH);

        //Strafe a certain amount of blocks
            frontLeftMotorTICKS -= (strafeBLOCK * driveMode * BLOCK_LENGTH);
            frontRightMotorTICKS += (strafeBLOCK * driveMode * BLOCK_LENGTH);
            backLeftMotorTICKS += (strafeBLOCK * driveMode * BLOCK_LENGTH);
            backRightMotorTICKS -= (strafeBLOCK * driveMode * BLOCK_LENGTH);

        //return calculation results from pipeline for use in code
        return new double[]{frontLeftMotorTICKS, frontRightMotorTICKS, backLeftMotorTICKS, backRightMotorTICKS};
    }

    //odometry position checking and setting
    public double[] odomToMotorConvert(double[] motorTICKS, ) {
        motorTICKS = new double[]{};
        double frontLeftMotorTICKS = motorTICKS[0];
        double frontRightMotorTICKS = motorTICKS[1];
        double backLeftMotorTICKS = motorTICKS[2];
        double backRightMotorTICKS = motorTICKS[3];
    }
}