package machineLearning.positionTrainer;

import machineLearning.KMeans;
import org.la4j.Matrix;

/**
 * Created by patry on 21/05/17.
 */
public class KMeansTrainer implements PositionTrainer {

    private static KMeansTrainer instance = new KMeansTrainer();

    public static KMeansTrainer getInstance() {
        return instance;
    }

    private KMeansTrainer() {}

    @Override
    public Matrix train(Matrix x, int k) {
        KMeans kMeans = new KMeans(30, x, k);
        kMeans.fit();
        return kMeans.getCentroidPositions();
    }
}
