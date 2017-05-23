package machineLearning.outputFunction;

import org.la4j.Matrix;

/**
 * Created by patry on 29/03/2017.
 */
public interface OutputFunction {
    Matrix activate(Matrix input);
}
