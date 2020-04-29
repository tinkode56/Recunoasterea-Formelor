package ro.usv.rf;

import java.awt.geom.Point2D;

/**
 * DistanceUtils
 */
public class DistanceUtils {

    protected static double generalizedEuclidianDistance(double[] pattern1, double[] pattern2) {
        double distance = 0.0;
        double diffSums = 0.0;
        for (int i = 0; i < pattern1.length; i++) {
            diffSums += Math.pow((pattern1[i] - pattern2[i]), 2);
        }
        distance = Math.sqrt(diffSums);
        return distance;
    }

    protected static double generalizedEuclidianDistance(Point2D p1, Point2D p2) {
        return Math.sqrt((Math.pow(p1.getX() - p2.getX(), 2)) + (Math.pow(p1.getY() - p2.getY(), 2)));
    }
}