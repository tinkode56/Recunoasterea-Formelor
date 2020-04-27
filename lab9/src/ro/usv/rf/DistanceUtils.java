package ro.usv.rf;

/**
 * DistanceUtils
 */
public class DistanceUtils {

    protected static double generalizedEuclidianDistance(double[] pattern1, double[] pattern2, int classOffset) {
        double distance = 0.0;
        double diffSums = 0.0;
        for (int i = 0; i < pattern1.length - classOffset; i++) {
            diffSums += Math.pow((pattern1[i] - pattern2[i]), 2);
        }
        distance = Math.sqrt(diffSums);
        return distance;
    }
}