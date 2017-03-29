package pl.pkjr.iad.machineLearning.outputFunction;

import org.la4j.Matrix;
import pl.pkjr.iad.utility.MatrixUtil;

import static pl.pkjr.iad.utility.MatrixUtil.sigmoid;

/**
 * Created by patry on 29/03/2017.
 */
public class SigmoidOutputFunction implements OutputFunction {
    @Override
    public Matrix activate(Matrix input) {
        return sigmoid(input);
    }
}
