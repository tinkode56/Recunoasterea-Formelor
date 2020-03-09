package ro.usv.rf;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			double[] col1 = new double[numberOfPatterns];
			double[] col2 = new double[numberOfPatterns];
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

			System.out.println("Euclidian distances: ");
			for (int i = 0; i < numberOfPatterns; i++) {
				col1[i] = learningSet[i][0];
				col2[i] = learningSet[i][1];
			}
			double[] eucDists = DistanceUtils.euclidianDistance(col1, col2);
			for (int i = 0; i < eucDists.length; i++)
				System.out.println(eucDists[i]);
			System.out.println("Mahalanobis distance: ");
			double mahDistances = DistanceUtils.mahalanobisDistance(learningSet[0], learningSet[1], numberOfPatterns);
			System.out.println(mahDistances);
			System.out.println("Cebisev distance: ");
			double cebDistances = DistanceUtils.cebisevDistance(learningSet[0], learningSet[1]);
			System.out.println(cebDistances);
			System.out.println("City Block distance: ");
			double cbDistances = DistanceUtils.cityBlockDistance(learningSet[0], learningSet[1]);
			System.out.println(cbDistances);
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
