package UtilityClasses;

import com.qualcomm.robotcore.util.ElapsedTime;

public class StigmoidController {
    private double verticalStretch = 1, horizontalStretch = 1;
    private double yIntercept = 0;

    private double lastError;


    public StigmoidController(double v, double h, double y){
        verticalStretch = v;
        horizontalStretch = h;
        yIntercept = y;
    }

    public double calculateResponse(double error){
        error *= horizontalStretch;

        double response = ((1.0 / (1. + Math.exp(-error)))-.5) * 2.0;
        response *= verticalStretch;

        if(error < 0){
            response -= yIntercept;
        }else if(error > 0){
            response += yIntercept;
        }

        return response;
    }
}
