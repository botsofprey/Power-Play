package UtilityClasses;

public class Point {
    public double x, y, angle;

    public Point(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double xDifference(double otherX){
        return otherX - x;
    }

    public double yDifference(double otherY){
        return otherY - y;
    }

    public double angleDifference(double otherAngle){
        return otherAngle - angle;
    }

    public void changePoint(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void add(double x, double y, double angle){
        changePoint(this.x + x, this.y = y, this.angle + angle);
    }

    public boolean compareAll(Point point, double cmOffset, double angleOffset){
        return
                comparePosition(point, cmOffset) &&
                compareHeading(point, angleOffset);
    }

    public boolean comparePosition(Point point, double cmOffset){
        return
                Math.abs(point.x - this.x) < cmOffset &&
                Math.abs(point.y - this.y) < cmOffset;
    }

    public boolean compareHeading(Point point, double angleOffset){
        return Math.abs(point.angle - this.angle) < angleOffset;
    }

}
