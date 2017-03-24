package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * Created by patry on 24/03/2017.
 */
public class MachineLearningAlgorithm {

    public static void gradientDescent(Matrix[] Theta, Matrix[] Gradients, double alpha) {
        for (int i = 0; i < Theta.length; ++i) {
            Theta[i] = Theta[i].subtract(Gradients[i].multiply(alpha));
        }
    }


}
