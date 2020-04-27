package ro.usv.rf;

import java.util.Arrays;
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
            dynamicKernels(learningSet, 2);
        } catch (USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }
    }

    public static int[] dynamicKernels(double[][] learningSet, int k) {
        Random rng = new Random();
        int[] iClass = new int[learningSet.length];
        int[] centroids = new int[k];
        boolean done;
        Arrays.fill(iClass, 0);

        for (int i = 0; i < k; i++) {
            centroids[i] = rng.nextInt(k);
            iClass[centroids[i]] = i + 1;
            // System.out.println(centroids[i] + " " + iClass[centroids[i]]);
        }

        do {
            done = true;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < learningSet.length; j++) {

                }
            }
        } while (done == false);
        return iClass;
    }
}