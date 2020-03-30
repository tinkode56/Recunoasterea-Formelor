package ro.usv.rf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * MainClass
 */
public class MainClass {

    public static void main(String[] args) {
        double[][] learningSet;
        int numberOfForms;
        int numberOfFeatures;
        int numberOfClasses;
        double wMatrix[][];
        Map<Integer, Integer> classes;
        double[][] unknownPatterns = { { 1, 3, 1 }, { 4, 5, 1 }, { 0, 0, 1 } };

        try {
            learningSet = FileUtils.readLearningSetFromFile("in.txt");
            numberOfForms = learningSet.length;
            numberOfFeatures = learningSet[0].length - 1; // do not count class as feature
            classes = new HashMap<>();

            // count the classes
            for (int i = 0; i < numberOfForms; i++) {
                if (!classes.containsKey((int) learningSet[i][numberOfFeatures]))
                    classes.put((int) learningSet[i][numberOfFeatures], 1);
            }
            numberOfClasses = classes.size();
            wMatrix = new double[numberOfClasses][numberOfFeatures + 1];
            // System.out.println(numberOfClasses);

            // calculate the W matrix
            for (int i = 0; i < numberOfClasses; i++) {
                double[] avg = calculateClassFeatureAverages(learningSet, i + 1);
                for (int j = 0; j < numberOfFeatures; j++)
                    wMatrix[i][j] = avg[j];
                double squareSums = Arrays.stream(avg).map(x -> Math.pow(x, 2)).sum();
                wMatrix[i][numberOfFeatures] = (-0.5) * squareSums;
            }
            double[] d1 = new double[numberOfClasses];
            double[] d2 = new double[numberOfClasses];
            double[] d3 = new double[numberOfClasses];
            // calculate the value of discriminant functions
            for (int i = 0; i < numberOfClasses; i++) {
                d1[i] = calculateDiscriminantValue(wMatrix[i], unknownPatterns[0]);
                d2[i] = calculateDiscriminantValue(wMatrix[i], unknownPatterns[1]);
                d3[i] = calculateDiscriminantValue(wMatrix[i], unknownPatterns[2]);
            }
            // determine class as the biggest value in discriminant values
            System.out.println("Clasa forma 1: " + getClass(d1));
            System.out.println("Clasa forma 2: " + getClass(d2));
            System.out.println("Clasa forma 3: " + getClass(d3));

        } catch (

        USVInputFileCustomException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Finished learning set operations");
        }

    }

    public static double[] calculateClassFeatureAverages(double[][] learningSet, int classNumber) {
        double[] averages = new double[learningSet[0].length - 1];
        int[] nOldValues = new int[learningSet[0].length - 1];
        double[] sumOldValues = new double[learningSet[0].length - 1];
        Arrays.fill(averages, 0);
        Arrays.fill(nOldValues, 0);
        Arrays.fill(sumOldValues, 0);

        for (int i = 0; i < learningSet.length; i++)
            for (int j = 0; j < learningSet[0].length - 1; j++) {
                if (learningSet[i][learningSet[i].length - 1] == classNumber) {
                    averages[j] = (sumOldValues[j] + learningSet[i][j]) / (nOldValues[j] + 1);
                    sumOldValues[j] += learningSet[i][j];
                    nOldValues[j]++;
                }
            }
        return averages;
    }

    public static double calculateDiscriminantValue(double[] array1, double[] array2) {
        double result = 0;
        for (int i = 0; i < array1.length; i++) {
            result += (array1[i] * array2[i]);
        }
        return result;
    }

    public static int getClass(double[] discriminantValues) {
        int index = 0;
        double maxVal = discriminantValues[0];
        for (int i = 1; i < discriminantValues.length; i++) {
            if (maxVal < discriminantValues[i]) {
                maxVal = discriminantValues[i];
                index = i;
            }
        }
        return index + 1;
    }
}