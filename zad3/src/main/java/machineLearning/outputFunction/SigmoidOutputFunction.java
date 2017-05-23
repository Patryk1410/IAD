package machineLearning.outputFunction;

import org.la4j.Matrix;
import util.MatrixUtil;


/**
 * Created by patry on 29/03/2017.
 */
public class SigmoidOutputFunction implements OutputFunction {
    @Override
    public Matrix activate(Matrix input) {
        return MatrixUtil.getInstance().sigmoid(input);
    }
}
