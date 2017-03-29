package pl.pkjr.iad.machineLearning.outputFunction;

import org.la4j.Matrix;

/**
 * Created by patry on 29/03/2017.
 */
public class LinearOutputFunction implements OutputFunction {
    @Override
    public Matrix activate(Matrix input) {
        return input;
    }
}
