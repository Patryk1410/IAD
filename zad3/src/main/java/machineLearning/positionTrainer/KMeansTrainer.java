package machineLearning.positionTrainer;

import machineLearning.KMeans;
import org.la4j.Matrix;

/**
 * Created by patry on 21/05/17.
 */
public class KMeansTrainer implements PositionTrainer {

    @Override
    public Matrix train(Matrix x, int k) {
        KMeans kMeans = new KMeans(30, x, k);
        kMeans.fit();
        return kMeans.getCentroidPositions();
    }
}
