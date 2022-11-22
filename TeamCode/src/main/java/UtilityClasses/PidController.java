package UtilityClasses;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PidController {
    private double kp, ki, kd;
    private double lastError = 0, integralSum = 0;
    private ElapsedTime timer;

    public PidController(double p, double i, double d){
        kp = p;
        ki = i;
        kd = d;

        timer = new ElapsedTime();
    }

    public double calculateResponse(double error){
        double d = (error - lastError) / timer.nanoseconds();
        integralSum += (error * timer.nanoseconds());

        double output = (kp * error) + (ki * integralSum) + (kd * d);

        lastError = error;
        return output;
    }

    public void reset(){
        lastError = 0;
        integralSum = 0;
        timer.reset();
    }
}
