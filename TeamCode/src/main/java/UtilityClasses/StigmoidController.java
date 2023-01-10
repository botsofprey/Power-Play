package UtilityClasses;

import com.qualcomm.robotcore.util.ElapsedTime;

public class StigmoidController {
    private double verticalStretch = 1, horizontalStretch = 1;
    private double yIntercept;

    private ElapsedTime timer;
    private double addPower;
    private double lastError;

    public StigmoidController(double v, double h, double y){
        verticalStretch = v;
        horizontalStretch = h;
        yIntercept = y;

        timer = new ElapsedTime();
    }

    public void reset(){
        addPower = 0;
        lastError = 0;
        timer.reset();
    }

    public double calculateResponse(double error){
        error *= horizontalStretch;

        double response = error/Math.sqrt(1 + Math.pow(error, 2));
        response *= verticalStretch;

        if(error < 0){
            response -= yIntercept;
        }else if(error > 0){
            response += yIntercept;
        }

        return response;
    }
}
