package cli;

import machineLearning.KMeans;
import org.la4j.Matrix;
import util.DatasetUtil;

/**
 * Created by patry on 10/05/17.
 */
public class KMeansExercise implements InterfaceModule {

    private static final String SELECT_NUMBER_OF_ITERATIONS = "Select number of iterations:";
    private static final String SELECT_NUMBER_OF_CENTROIDS = "Select number of centroids:";

    private int numberOfIterations;
    private Matrix x;
    private int k;

    @Override
    public void run() {
        initParams();
        runKMeans();
    }

    private void runKMeans() {
        KMeans kMeans = new KMeans(numberOfIterations, x, k);
        kMeans.fit();
    }

    private void initParams() {
        commandLineUtil.print(SELECT_NUMBER_OF_ITERATIONS);
        numberOfIterations = commandLineUtil.getInt(1, 1000);
        commandLineUtil.print(SELECT_NUMBER_OF_CENTROIDS);
        k = commandLineUtil.getInt(1, 10000);
        String datasetPath = "./datasets/dataset.txt";
        x = DatasetUtil.getInstance().readTestData(datasetPath);
    }
}
