package ro.usv.rf;

/**
 * DistanceObj
 */
public class DistanceObj implements Comparable<DistanceObj> {
    double distance;
    String ofClass;

    public DistanceObj(double _distance, String _ofClass) {
        this.distance = _distance;
        this.ofClass = _ofClass;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the ofClass
     */
    public String getOfClass() {
        return ofClass;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @param ofClass the ofClass to set
     */
    public void setOfClass(String ofClass) {
        this.ofClass = ofClass;
    }

    @Override
    public int compareTo(DistanceObj dist) {
        if (this.getDistance() > dist.getDistance())
            return 1;
        else if (this.getDistance() < dist.getDistance())
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return distance + " " + ofClass;
    }

}