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

    public Location(int i) {

    }

    public double getRadians(){
        return Math.toRadians(angle);
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
        changePoint(this.x + x, this.y + y, this.angle + angle);
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
        return Math.abs(location.angle - this.angle) < angleOffset
                || Math.abs(location.angle - this.angle) > (360 - angleOffset);
    }

    public double[] toArray(){
        double[] locArray = {
                x,
                y,
                angle
        };

        return locArray;
    }

    public double distanceBetween(Location other){
        return Math.sqrt(Math.pow(xDifference(other.x), 2) + Math.pow(yDifference(other.y), 2));
    }

    public boolean equals(Location other, double angleOffset){
        return distanceBetween(other) < 1 && compareHeading(other, angleOffset);
    }

    public String toString(){
        return Math.round(x) + ", " +
        Math.round(y) + ", " +
        Math.round(angle) + ", ";
    }

    public void add(Location positionLocation) {
        x += positionLocation.x;
        y += positionLocation.y;
        angle += positionLocation.angle;
    }
}
