package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            
            // Guess the new set`s classes
            System.out.println("\n-- 9-NN -- ");
            System.out.println(" /> " + guessClass(dist1, 9));
            System.out.println(" /> " + guessClass(dist2, 9));
            System.out.println(" /> " + guessClass(dist3, 9));
            System.out.println("\n-- 11-NN -- ");
            System.out.println(" /> " + guessClass(dist1, 11));
            System.out.println(" /> " + guessClass(dist2, 11));
            System.out.println(" /> " + guessClass(dist3, 11));
            System.out.println("\n-- 17-NN -- ");
            System.out.println(" /> " + guessClass(dist1, 17));
            System.out.println(" /> " + guessClass(dist2, 17));
            System.out.println(" /> " + guessClass(dist3, 17));
            System.out.println("\n-- 31-NN -- ");
            System.out.println(" /> " + guessClass(dist1, 31));
            System.out.println(" /> " + guessClass(dist2, 31));
            System.out.println(" /> " + guessClass(dist3, 31) + "\n");
        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }
    }

    /**
     * @param distances the distance data structure
     * @param k         the value for k-nn
     */
    public static String guessClass(PriorityQueue<DistanceObj> distances, int k) {
        Map<String, Integer> bestGuess = new HashMap<String, Integer>();
        for (int i = 0; i < k; i++) {
            DistanceObj dObj = distances.remove();
            // If no value for this key, put 1, else add 1 to the old value
            bestGuess.merge(dObj.getOfClass(), 1, Integer::sum);
        }
        // System.out.println(bestGuess);
        // Get the key associated to the max value
        String res = bestGuess.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        System.out.println("Probability: " + (bestGuess.get(res)/(double)k)*Double.valueOf(100)+ "%");
        return res;
    }

}