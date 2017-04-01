package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;

import java.util.Arrays;

/**
 * Created by patry on 24/03/2017.
 */
public class MachineLearningAlgorithm {

    public static void gradientDescent(Matrix[] Theta, Matrix[] PreviousTheta, Matrix[] Gradients, double alpha,
                                       double mu) {
        for (int i = 0; i < Theta.length; ++i) {
            PreviousTheta[i] = PreviousTheta[i].multiply(mu).subtract(Gradients[i].multiply(alpha));
            Theta[i] = Theta[i].add(PreviousTheta[i]);
        }
    }
}
