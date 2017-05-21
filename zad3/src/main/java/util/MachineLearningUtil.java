package util;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.Random;

/**
 * Created by patry on 21/05/17.
 */
public class MachineLearningUtil {

    Random random = new Random();

    private static MachineLearningUtil instance;

    public static MachineLearningUtil getInstance() {
        return instance;
    }

    private MachineLearningUtil(){}

    public Matrix setRandomPositions(Matrix x, int k) {
        Matrix res = new Basic2DMatrix(k, x.columns());
        for (int i = 0; i < res.rows(); ++i) {
            int randomPointIndex = random.nextInt(x.rows());
            for (int j = 0; j < res.columns(); ++j) {
                double value = x.get(randomPointIndex, j);
                res.set(i, j, value);
            }
        }
        return res;
    }
}
