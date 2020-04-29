package ro.usv.rf;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainClass {
    public static void main(String[] args) {
        double[][] learningSet;
        try {
            learningSet = FileUtils.readLearningSetFromFile("in.txt");
            int numberOfPatterns = learningSet.length;
            int numberOfFeatures = learningSet[0].length;

            System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
                    numberOfFeatures));
            int[] classes = kMeans(learningSet, 2, 100, 0.0001);
            for (int i = 0; i < classes.length; i++) {
                System.out.println(classes[i]);
            }
        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }
    }

    public static int[] kMeans(double[][] learningSet, int k, int max_iterations, double precision) {
        List<Point2D> dataSet = new ArrayList<Point2D>();
        ArrayList<Point2D> centroids = new ArrayList<Point2D>();
        ArrayList<Point2D> oldCentroids = null;
        List<ArrayList<Double>> distances = new ArrayList<ArrayList<Double>>();
        int[] clusterIndex = new int[learningSet.length];
        int iterations = 0;
        Random r = new Random();
        // Create a list of points
        for (int i = 0; i < learningSet.length; i++) {
            dataSet.add(new Point2D.Double(learningSet[i][0], learningSet[i][1]));
        }

        // Pick k random starting centroids
        for (int i = 0; i < k; i++) {
            int cIndex = r.nextInt(learningSet.length);
            // Assign cluster id to this point
            clusterIndex[cIndex] = i;
            System.out.println(dataSet.get(cIndex));
            centroids.add(dataSet.get(cIndex));
        }
        oldCentroids = new ArrayList<Point2D>(centroids);

        // Start loop
        do {

            oldCentroids = new ArrayList<Point2D>(centroids);
            iterations++;

            // Calculate distances from each point to the centroids
            for (int i = 0; i < k; i++) {
                ArrayList<Double> dist = new ArrayList<Double>();
                for (int j = 0; j < dataSet.size(); j++) {
                    // System.out.println(DistanceUtils.generalizedEuclidianDistance(centroids.get(i),
                    // dataSet.get(j)));
                    dist.add(DistanceUtils.generalizedEuclidianDistance(centroids.get(i), dataSet.get(j)));
                }
                distances.add(dist);
            }

            // Assign each point to a cluster
            for (int point = 0; point < dataSet.size(); point++) {
                double minDist = Double.POSITIVE_INFINITY;
                int cIndex = -1;
                for (int cl = 0; cl < k; cl++) {
                    if (distances.get(cl).get(point) < minDist) {
                        minDist = distances.get(cl).get(point);
                        cIndex = cl;
                    }
                }
                clusterIndex[point] = cIndex;
            }

            // Calculate new centroid values
            for (int cl = 0; cl < k; cl++) {
                int nrOfPoints = 0;
                double[] coordSums = { 0, 0 };
                for (int point = 0; point < dataSet.size(); point++) {
                    if (clusterIndex[point] == cl) {
                        nrOfPoints++;
                        coordSums[0] += dataSet.get(point).getX();
                        coordSums[1] += dataSet.get(point).getY();
                    }
                }

                centroids.set(cl,
                        new Point2D.Double((double) (coordSums[0] / nrOfPoints), (double) (coordSums[1] / nrOfPoints)));
            }
        } while (!shouldStop(oldCentroids, centroids, iterations, max_iterations, precision));
        return clusterIndex;
    }

    private static boolean shouldStop(ArrayList<Point2D> oldCentroids, ArrayList<Point2D> centroids, int iterations,
            int max_iterations, double precision) {
        if (iterations > max_iterations)
            return true;
        for (int i = 0; i < centroids.size(); i++) {
            if (DistanceUtils.generalizedEuclidianDistance(oldCentroids.get(i), centroids.get(i)) < precision)
                return true;
        }
        return false;
    }

}