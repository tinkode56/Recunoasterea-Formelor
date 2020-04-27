package ro.usv.rf;

/**
 * DistanceUtils
 */
public class DistanceUtils {

    protected static double[] euclidianDistance(double[] feature1, double[] feature2) {
        double[] distances = new double[feature1.length];
        for (int i = 0; i < feature1.length - 1; i++) {
            distances[i] = Math
                    .sqrt(Math.pow(feature1[i] - feature1[i + 1], 2) + Math.pow(feature2[i] - feature2[i + 1], 2));
        }
        return distances;
    }

    protected static double mahalanobisDistance(double[] form1, double[] form2, double nOfForms) {
        double distance = 0.0;
        for (int i = 0; i < form1.length; i++) {
            distance += Math.pow(form1[i] - form2[i], nOfForms);
        }
        distance = Math.pow(distance, 1 / nOfForms);
        return distance;
    }

    protected static double cebisevDistance(double[] form1, double[] form2) {
        double distance = 0.0;
        for (int i = 0; i < form1.length; i++) {
            double d = Math.abs(form1[i] - form2[i]);
            distance = Math.max(distance, d);
        }
        return distance;
    }

    protected static double cityBlockDistance(double[] pattern1, double[] pattern2) {
        double distance = 0.0;
        for (int i = 0; i < pattern1.length; i++)
            distance += Math.abs(pattern1[i] - pattern2[i]);
        return distance;
    }

    protected static double generalizedEuclidianDistance(double[] pattern1, double[] pattern2) {
        double distance = 0.0;
        double diffSums = 0.0;
        for (int i = 0; i < pattern1.length; i++) {
            diffSums += Math.pow((pattern1[i] - pattern2[i]), 2);
        }
        distance = Math.sqrt(diffSums);
        return distance;
    }
}