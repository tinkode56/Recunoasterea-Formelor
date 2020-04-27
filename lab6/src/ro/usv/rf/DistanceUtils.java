package ro.usv.rf;

public class DistanceUtils {
    protected static double generalizedEuclidianDistance(double pattern1, double pattern2) {
        double diffSums = Math.pow((pattern1 - pattern2), 2);
        return Math.sqrt(diffSums);
    }
}