package ro.usv.rf;

/**
 * MainClass
 */
public class MainClass {

    public static void main(String[] args) {
        double[][] learningSet = FileUtils.readLearningSetFromFile("in.txt");
        FileUtils.writeLearningSetToFile("out.csv", normalizeLearningSet(learningSet));
    }
    
    private static double[][] normalizeLearningSet(double[][] learningSet) {
        double[][] normalizedLearningSet = new double[learningSet.length][learningSet[0].length];
        int lines = learningSet.length;
        int cols = learningSet[0].length;

        double[] max = new double[learningSet.length];
        double[] min = new double[learningSet.length];

        for(int i=0; i<cols; i++){
            max[i] = learningSet[0][i];
            min[i] = learningSet[0][i];
        }

        for(int i=0; i<lines; i++)
            for(int j=0; j<cols; j++){
                if(max[j] < learningSet[i][j])
                max[j] = learningSet[i][j];
                if(min[j] > learningSet[i][j])
                min[j] = learningSet[i][j];
            }
            
        for(int i=0; i<lines; i++)
            for(int j=0; j<cols; j++)
                normalizedLearningSet[i][j] = (learningSet[i][j] - min[j]) / (max[j] - min[j]);

        return normalizedLearningSet;
    }
}