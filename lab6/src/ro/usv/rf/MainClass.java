package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

public class MainClass {

    public static void main(String[] args) {
        List<ArrayList<String>> learningSet;
        double[] grades = { 3.80, 5.75, 6.25, 7.25, 8.5 };
        int[] kVal = { 1, 3, 5, 7, 9, 13, 17 };
        try {
            learningSet = FileUtils.readLearningSetFromFile("gradesClasses.txt");
            int numberOfPatterns = learningSet.size();
            int numberOfFeatures = learningSet.get(0).size() - 1;
            System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
                    numberOfFeatures));

            for (int i = 0; i < grades.length; i++) {
                PriorityQueue<DistanceObj> dist = new PriorityQueue<>();

                for (int j = 0; j < numberOfPatterns; j++) {
                    double d = DistanceUtils.generalizedEuclidianDistance(grades[i],
                            Double.valueOf(learningSet.get(j).get(0)));
                    dist.add(new DistanceObj(d, learningSet.get(j).get(1)));
                }

                for (int k = 0; k < kVal.length; k++) {
                    PriorityQueue<DistanceObj> distNew = new PriorityQueue<>(dist);
                    System.out.println(kVal[k] + "-NN for " + grades[i]);
                    System.out.println(guessClass(distNew, kVal[k]));
                }
            }

            // identify max k value for accurate results
            int testIndex = new Random().nextInt(numberOfPatterns);
            PriorityQueue<DistanceObj> dist = new PriorityQueue<>();
            for (int i = 0; i < numberOfPatterns; i++) {
                if (i == testIndex)
                    continue;
                else {
                    double d = DistanceUtils.generalizedEuclidianDistance(
                            Double.valueOf(learningSet.get(testIndex).get(0)),
                            Double.valueOf(learningSet.get(i).get(0)));
                    dist.add(new DistanceObj(d, learningSet.get(i).get(1)));
                }
            }
            for (int k = 0; k < kVal.length; k++) {
                PriorityQueue<DistanceObj> distNew = new PriorityQueue<>(dist);
                System.out.println(kVal[k] + "-NN for " + learningSet.get(testIndex).get(0));
                String guess = guessClass(distNew, kVal[k]);
                if (!(learningSet.get(testIndex).get(1).equals(guess))) {
                    System.out.println("Latest accurat k value is " + kVal[k - 1]);
                    break;
                }
                System.out.println("Latest accurat k value is " + kVal[k]);
            }

        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }

    }

    /**
     * @param distances the distance data structure
     * @param k         the value for k-nn
     * @return the class name
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
        String res = bestGuess.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        return res;
    }
}
