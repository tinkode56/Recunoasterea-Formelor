package ro.usv.rf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MainClass {

    public static void main(String[] args) {
        String[][] learningSet;
        int[] kVal = { 1, 3, 5, 7, 9, 11, 13, 15 };
        Map<String, Integer> classes;
        double[][] wMatrix;
        int numberOfClasses;
        List<String> classNames;
        double[][] dValues;

        try {
            learningSet = FileUtils.readLearningSetFromFile("iris.csv");
            int numberOfPatterns = learningSet.length;
            int numberOfFeatures = learningSet[0].length - 1;

            System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
                    numberOfFeatures));

            Map<String, Integer> classesMap = new HashMap<String, Integer>();

            // create map with distinct classes and number of occurence for each class
            for (int i = 0; i < numberOfPatterns; i++) {
                String cls = learningSet[i][learningSet[i].length - 1];
                if (classesMap.containsKey(cls)) {
                    Integer nrOfClassPatterns = classesMap.get(cls);
                    classesMap.put(cls, nrOfClassPatterns + 1);
                } else {
                    classesMap.put(cls, 1);
                }
            }
            Random random = new Random();
            // map that keeps for each class the random patterns selected for evaluation set
            Map<String, List<Integer>> classesEvaluationPatterns = new HashMap<String, List<Integer>>();
            Integer evaluationSetSize = 0;
            for (Map.Entry<String, Integer> entry : classesMap.entrySet()) {
                String className = entry.getKey();
                Integer classMembers = entry.getValue();
                Integer evaluationPatternsNr = Math.round(classMembers * 15 / 100);
                evaluationSetSize += evaluationPatternsNr;
                List<Integer> selectedPatternsForEvaluation = new ArrayList<Integer>();
                for (int i = 0; i < evaluationPatternsNr; i++) {
                    Integer patternNr = random.nextInt(classMembers) + 1;
                    while (selectedPatternsForEvaluation.contains(patternNr)) {
                        patternNr = random.nextInt(classMembers) + 1;
                    }
                    selectedPatternsForEvaluation.add(patternNr);
                }
                classesEvaluationPatterns.put(className, selectedPatternsForEvaluation);
            }

            // split into training set and evaluation set
            String[][] evaluationSet = new String[evaluationSetSize][numberOfPatterns];
            String[][] trainingSet = new String[numberOfPatterns - evaluationSetSize][numberOfPatterns];
            int evaluationSetIndex = 0;
            int trainingSetIndex = 0;
            Map<String, Integer> classCurrentIndex = new HashMap<String, Integer>();
            for (int i = 0; i < numberOfPatterns; i++) {
                String className = learningSet[i][numberOfFeatures];
                if (classCurrentIndex.containsKey(className)) {
                    int currentIndex = classCurrentIndex.get(className);
                    classCurrentIndex.put(className, currentIndex + 1);
                } else {
                    classCurrentIndex.put(className, 1);
                }
                if (classesEvaluationPatterns.get(className).contains(classCurrentIndex.get(className))) {
                    evaluationSet[evaluationSetIndex] = learningSet[i];
                    evaluationSetIndex++;
                } else {
                    trainingSet[trainingSetIndex] = learningSet[i];
                    trainingSetIndex++;
                }
            }

            FileUtils.writeLearningSetToFile("eval.txt", evaluationSet);
            FileUtils.writeLearningSetToFile("train.txt", trainingSet);

            // KNN algorithm
            for (int i = 0; i < evaluationSet.length; i++) {
                System.out.println("-------------------");
                PriorityQueue<DistanceObj> dist = new PriorityQueue<>();
                double[] pattern1 = Arrays.stream(Arrays.copyOf(evaluationSet[i], numberOfFeatures))
                        .mapToDouble(Double::parseDouble).toArray();

                for (int j = 0; j < trainingSet.length; j++) {
                    double[] pattern2 = Arrays.stream(Arrays.copyOf(trainingSet[j], numberOfFeatures))
                            .mapToDouble(Double::parseDouble).toArray();
                    double d = DistanceUtils.generalizedEuclidianDistance(pattern1, pattern2);
                    dist.add(new DistanceObj(d, trainingSet[j][numberOfFeatures]));
                }

                for (int k = 0; k < kVal.length; k++) {
                    PriorityQueue<DistanceObj> distNew = new PriorityQueue<>(dist);
                    System.out.println(kVal[k] + "-NN: guessed class: " + guessClass(distNew, kVal[k])
                            + ", expected class: " + evaluationSet[i][numberOfFeatures]);
                }
            }

            // type 3 classifier
            classes = new HashMap<String, Integer>();

            // count the classes
            for (int i = 0; i < numberOfPatterns; i++) {
                classes.merge(learningSet[i][numberOfFeatures], 1, Integer::sum);
            }
            numberOfClasses = classes.size();
            wMatrix = new double[numberOfClasses][numberOfFeatures + 1];
            classNames = classes.keySet().stream().collect(Collectors.toList());

            // calculate the W matrix
            for (int i = 0; i < numberOfClasses; i++) {
                double[] avg = calculateClassFeatureAverage(learningSet, classNames.get(i));
                for (int j = 0; j < numberOfFeatures; j++)
                    wMatrix[i][j] = avg[j];
                double squareSums = Arrays.stream(avg).map(x -> Math.pow(x, 2)).sum();
                wMatrix[i][numberOfFeatures] = (-0.5) * squareSums;
            }

            // calculate the value of discriminant functions
            dValues = new double[evaluationSet.length][numberOfClasses];
            for (int i = 0; i < evaluationSet.length; i++) {
                for (int j = 0; j < numberOfClasses; j++) {
                    double[] pattern = Arrays.stream(Arrays.copyOf(evaluationSet[i], numberOfFeatures))
                            .mapToDouble(Double::parseDouble).toArray();
                    double[] finalPattern = Arrays.copyOf(pattern, pattern.length + 1);
                    finalPattern[finalPattern.length - 1] = 1;
                    dValues[i][j] = calculateDiscriminantValue(wMatrix[j], finalPattern);
                }
            }
            // show classes
            for (int i = 0; i < evaluationSet.length; i++) {
                System.out.println("Determined class: " + classNames.get(getClassName(dValues[i])) + " Actual class: "
                        + evaluationSet[i][numberOfFeatures]);
            }

        } catch (USVInputFileCustomException e) {
            e.printStackTrace();
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

    public static double calculateDiscriminantValue(double[] array1, double[] array2) {
        double result = 0;
        for (int i = 0; i < array1.length; i++) {
            result += (array1[i] * array2[i]);
        }
        return result;
    }

    public static double[] calculateClassFeatureAverage(String[][] learningSet, String className) {
        double[] averages = new double[learningSet[0].length - 1];
        int[] nOldValues = new int[learningSet[0].length - 1];
        double[] sumOldValues = new double[learningSet[0].length - 1];
        Arrays.fill(averages, 0);
        Arrays.fill(nOldValues, 0);
        Arrays.fill(sumOldValues, 0);

        for (int i = 0; i < learningSet.length; i++) {
            for (int j = 0; j < learningSet[0].length - 1; j++) {
                if (learningSet[i][learningSet[i].length - 1].compareTo(className) == 0) {
                    averages[j] = (sumOldValues[j] + Double.valueOf(learningSet[i][j])) / (nOldValues[j] + 1);
                    sumOldValues[j] += Double.valueOf(learningSet[i][j]);
                    nOldValues[j]++;
                }
            }
        }
        return averages;
    }

    public static int getClassName(double[] discriminantValues) {
        int index = 0;
        double maxVal = discriminantValues[0];
        for (int i = 1; i < discriminantValues.length; i++) {
            if (maxVal < discriminantValues[i]) {
                maxVal = discriminantValues[i];
                index = i;
            }
        }
        return index;
    }

}
