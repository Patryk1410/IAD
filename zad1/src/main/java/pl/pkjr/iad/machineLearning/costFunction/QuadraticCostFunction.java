package pl.pkjr.iad.machineLearning.costFunction;

import org.la4j.Matrix;
import pl.pkjr.iad.utility.MatrixUtil;

import static pl.pkjr.iad.utility.MatrixUtil.square;

/**
 * Created by patry on 24/03/2017.
 */
public class QuadraticCostFunction implements CostFunction {
    @Override
    public double calculateCost(Matrix[] Theta, double m, Matrix Y, Matrix H, double lambda) {
        return square(Y.subtract(H)).sum() / m;
    }
}
