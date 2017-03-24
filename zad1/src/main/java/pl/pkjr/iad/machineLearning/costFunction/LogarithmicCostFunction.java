package pl.pkjr.iad.machineLearning.costFunction;

import org.la4j.Matrix;
import pl.pkjr.iad.utility.MatrixUtil;

/**
 * Created by patry on 24/03/2017.
 */
public class LogarithmicCostFunction implements CostFunction {

    @Override
    public double calculateCost(Matrix[] Theta, double m, Matrix Y, Matrix H, double lambda) {
        Matrix logH = MatrixUtil.log(H);
        Matrix yLogH = MatrixUtil.elementwiseMultiply(Y, logH);
        Matrix y_1 = MatrixUtil.subtractFromNumber(1, Y);
        Matrix H_1 = MatrixUtil.subtractFromNumber(1, H);
        Matrix logH_1 = MatrixUtil.log(H_1);
        Matrix y_1logH_1 = MatrixUtil.elementwiseMultiply(y_1, logH_1);
        Matrix res = yLogH.add(y_1logH_1);
        double regSum = calculateRegSum(Theta);
        return ((-1.0 / m) * (res.sum())) + ((lambda / (2 * m)) * regSum);
    }

    private double calculateRegSum(Matrix[] Theta) {
        double res = 0;
        for (Matrix matrix : Theta) {
            res += MatrixUtil.square(matrix).sum();
        }
        return res;
    }
}
