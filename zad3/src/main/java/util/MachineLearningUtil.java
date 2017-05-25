package util;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.Random;

/**
 * Created by patry on 21/05/17.
 */
public class MachineLearningUtil {

    Random random = new Random();

    private static MachineLearningUtil instance = new MachineLearningUtil();

    public static MachineLearningUtil getInstance() {
        return instance;
    }

    private MachineLearningUtil(){}

    public Matrix setRandomPositions(Matrix x, int k) {
        //TODO: sometimes it chooses the same position multiple times
        Matrix res = new Basic2DMatrix(k, x.columns());
        Matrix copy = x.copy();
        for (int i = 0; i < res.rows(); ++i) {
            int randomPointIndex = random.nextInt(copy.rows());
            for (int j = 0; j < res.columns(); ++j) {
                double value = copy.get(randomPointIndex, j);
                res.set(i, j, value);
            }
            copy = copy.removeRow(randomPointIndex);
        }
        return res;
    }
}
