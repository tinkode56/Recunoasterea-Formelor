package ro.usv.rf;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet;
		double[][] distanceMatrix;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));
			distanceMatrix = new double[numberOfPatterns][numberOfPatterns];

			// System.out.println(DistanceUtils.generalizedEuclidianDistance(learningSet[0],
			// learningSet[1]));

			for (int i = 0; i < numberOfPatterns; i++)
				for (int j = i + 1; j < numberOfPatterns; j++) {
					distanceMatrix[i][j] = distanceMatrix[j][i] = DistanceUtils
							.generalizedEuclidianDistance(learningSet[i], learningSet[j], 1);
				}
			FileUtils.writeLearningSetToFile("out.csv", distanceMatrix);

			// get class for the latest pattern
			int closestPattern = 0;
			int lastPattern = numberOfPatterns - 1;
			double minDist = distanceMatrix[lastPattern][0];

			for (int i = 0; i < numberOfPatterns; i++) {
				if (distanceMatrix[lastPattern][i] < minDist && distanceMatrix[lastPattern][i] != 0) {
					minDist = distanceMatrix[4][i];
					closestPattern = i;
				}
			}
			System.out.println("Nearest neighbour distance: " + minDist);
			System.out.println("4th pattern is in class: " + learningSet[closestPattern][numberOfFeatures - 1]);

		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
