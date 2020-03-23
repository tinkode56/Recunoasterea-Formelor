package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * MainClass
 */
public class MainClass {

    public static void main(String[] args) {
        List<ArrayList<String>> learningSet;
        PriorityQueue<DistanceObj> dist1 = new PriorityQueue<>();
        PriorityQueue<DistanceObj> dist2 = new PriorityQueue<>();
        PriorityQueue<DistanceObj> dist3 = new PriorityQueue<>();

        double[] unknownPattern1 = { 25.89, 47.56 };
        double[] unknownPattern2 = { 24, 45.15 };
        double[] unknownPattern3 = { 25.33, 45.44 };

        try {
            // Read data sets from file and remove header
            learningSet = FileUtils.readLearningSetFromFile("data.csv");
            learningSet.remove(0);
            int numberOfPatterns = learningSet.size();

            // Calculate distances
            for (int i = 0; i < numberOfPatterns; i++) {

                double[] pattern2 = { Double.valueOf(learningSet.get(i).get(0)),
                        Double.valueOf(learningSet.get(i).get(1)) };
                double d1 = DistanceUtils.generalizedEuclidianDistance(unknownPattern1, pattern2, 0);
                double d2 = DistanceUtils.generalizedEuclidianDistance(unknownPattern2, pattern2, 0);
                double d3 = DistanceUtils.generalizedEuclidianDistance(unknownPattern3, pattern2, 0);
                dist1.add(new DistanceObj(d1, learningSet.get(i).get(3)));
                dist2.add(new DistanceObj(d2, learningSet.get(i).get(3)));
                dist3.add(new DistanceObj(d3, learningSet.get(i).get(3)));
            }
            for (int i = 0; i < 31; i++){
                System.out.println(dist3.remove());

            }
        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }
    }

}