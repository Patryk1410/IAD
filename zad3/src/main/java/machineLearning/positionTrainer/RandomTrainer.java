package machineLearning.positionTrainer;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.MachineLearningUtil;

import java.util.Random;

/**
 * Created by patry on 21/05/17.
 */
public class RandomTrainer implements PositionTrainer {

    Random random = new Random();

    @Override
    public Matrix train(Matrix x, int k) {
        return MachineLearningUtil.getInstance().setRandomPositions(x, k);
    }
}
