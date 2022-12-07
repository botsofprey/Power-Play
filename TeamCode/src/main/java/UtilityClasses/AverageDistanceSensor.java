package UtilityClasses;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class AverageDistanceSensor {
    private DistanceSensor distanceSensor;
    private DistanceUnit unit;

    private double[] distances;
    private int curIndex = 0;

    public AverageDistanceSensor(DistanceSensor sensor, DistanceUnit u, int savedDistances){
        distanceSensor = sensor;
        unit = u;
        distances = new double[savedDistances];
    }

    public double getDistance(){
        double distance = 0;
        int saved = 0;
        for(double d : distances){
            distance += d;
            if(d != 0)
                saved++;
        }
        distance /= saved;

        return distance;
    }

    public void update(){
        distances[curIndex] = distanceSensor.getDistance(unit);
        curIndex++;
        if(curIndex == distances.length)
            curIndex = 0;
    }
}
