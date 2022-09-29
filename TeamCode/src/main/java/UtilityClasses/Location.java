package UtilityClasses;

public class Location {
    public double x, y, angle;

    public Location(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    public Location(double x, double y){
        this.x = x;
        this.y = y;
        this.angle = 0;
    }

    public Location difference(Location otherLo){
        return new Location(otherLo.x - x, otherLo.y - y, otherLo.angle - angle);
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

    public boolean compareAll(Location location, double cmOffset, double angleOffset){
        return
                comparePosition(location, cmOffset) &&
                compareHeading(location, angleOffset);
    }

    public boolean comparePosition(Location location, double cmOffset){
        return
                Math.abs(location.x - this.x) < cmOffset &&
                Math.abs(location.y - this.y) < cmOffset;
    }

    public boolean compareHeading(Location location, double angleOffset){
        return Math.abs(location.angle - this.angle) < angleOffset;
    }

}
