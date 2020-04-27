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
        int[] kVal = { 9, 11, 17, 31 };
        double[][] evalSet = { { 25.89, 47.56 }, { 24, 45.15 }, { 25.33, 45.44 } };

        try {
            // Read data sets from file and remove header
            learningSet = FileUtils.readLearningSetFromFile("data.csv");
            learningSet.remove(0);
            int numberOfPatterns = learningSet.size();

            // Calculate distances
            for (int i = 0; i < evalSet.length; i++) {
                PriorityQueue<DistanceObj> dist = new PriorityQueue<>();

                for (int j = 0; j < learningSet.size(); j++) {
                    double[] pattern2 = { Double.valueOf(learningSet.get(j).get(0)),
                            Double.valueOf(learningSet.get(j).get(1)) };
                    double d = DistanceUtils.generalizedEuclidianDistance(evalSet[i], pattern2);
                    dist.add(new DistanceObj(d, learningSet.get(j).get(3)));
                }
                for (int k = 0; k < kVal.length; k++) {
                    PriorityQueue<DistanceObj> distNew = new PriorityQueue<>(dist);
                    System.out.println(kVal[k] + "-NN for evalSet " + i);
                    System.out.println(guessClass(distNew, kVal[k]));
                }
            }

            // Guess the new set`s classes

        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }
    }

    /**
     * @param distances the distance data structure
     * @param k         the value for k-nn
     * @return a list containing the class and the probability
     */
    public static List<String> guessClass(PriorityQueue<DistanceObj> distances, int k) {
        Map<String, Integer> bestGuess = new HashMap<String, Integer>();
        for (int i = 0; i < k; i++) {
            DistanceObj dObj = distances.remove();
            // If no value for this key, put 1, else add 1 to the old value
            bestGuess.merge(dObj.getOfClass(), 1, Integer::sum);
        }
        // System.out.println(bestGuess);
        // Get the key associated to the max value
        String res = bestGuess.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        double probability = (bestGuess.get(res) / (double) k) * Double.valueOf(100);
        List<String> l = new ArrayList<>();
        l.add(res);
        l.add(Double.toString(probability));

        return l;
    }

}