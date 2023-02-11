package UtilityClasses;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class PositionDistanceSensor {
	private final DistanceSensor distanceSensor;
	private final Location offsetFromCenter;

	public PositionDistanceSensor(DistanceSensor dis, Location offset){
		distanceSensor = dis;
		offsetFromCenter = offset;
	}

	public Location calculatePosition(double currentRadians){
		double distanceMeasured = distanceSensor.getDistance(DistanceUnit.CM);

		Location sensorPos = new Location(distanceMeasured * Math.sin(currentRadians),
				distanceMeasured * Math.cos(distanceMeasured));

		sensorPos.subtract(offsetFromCenter);

		return sensorPos;
	}
}
