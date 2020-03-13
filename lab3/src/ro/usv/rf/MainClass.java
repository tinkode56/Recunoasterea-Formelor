package ro.usv.rf;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet;
		double[] col1;
		double[] col2;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			col1 = new double[numberOfPatterns];
			col2 = new double[numberOfPatterns];
			
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

			System.out.println("-- Euclidian distances: ");
			for (int i = 0; i < numberOfPatterns; i++) {
				col1[i] = learningSet[i][0];
				col2[i] = learningSet[i][1];
			}
			double[] eucDists = DistanceUtils.euclidianDistance(col1, col2);
			for (int i = 0; i < eucDists.length - 1; i++)
				System.out.println(eucDists[i]);

			System.out.println("-- Mahalanobis distances: ");
			for (int i = 1; i < numberOfPatterns - 1; i++) {
				double mahDistances = DistanceUtils.mahalanobisDistance(learningSet[0], learningSet[i + 1],
						numberOfPatterns);
				System.out.println(mahDistances);
			}

			System.out.println("-- Cebisev distance: ");
			for (int i = 1; i < numberOfPatterns; i++) {
				double cebDistances = DistanceUtils.cebisevDistance(learningSet[0], learningSet[i]);
				System.out.println(cebDistances);
			}

			System.out.println("-- City Block distance: ");
			for (int i = 1; i < numberOfPatterns; i++) {
				double cbDistances = DistanceUtils.cityBlockDistance(learningSet[0], learningSet[i]);
				System.out.println(cbDistances);
			}

		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
