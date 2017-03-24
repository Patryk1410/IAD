package pl.pkjr.iad.machineLearning.costFunction;

import org.la4j.Matrix;

/**
 * Created by patry on 24/03/2017.
 */
public interface CostFunction {
    double calculateCost(Matrix[] Theta, double m, Matrix Y, Matrix H, double lambda);
}
