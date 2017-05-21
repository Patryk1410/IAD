package machineLearning.positionTrainer;

import org.la4j.Matrix;

/**
 * Created by patry on 21/05/17.
 */
public interface PositionTrainer {

    Matrix train(Matrix x, int k);
}
