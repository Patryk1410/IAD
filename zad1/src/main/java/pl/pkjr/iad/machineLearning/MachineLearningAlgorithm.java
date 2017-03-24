package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * Created by patry on 24/03/2017.
 */
public class MachineLearningAlgorithm {

    public static Matrix sigmoid(Matrix M) {
        Matrix newMatrix = M.copy();
        M.each((int i, int j, double value) -> {
            if (value > 10) {
                newMatrix.set(i, j, 1);
            } else if (value < -10) {
                newMatrix.set(i, j, 0);
            } else {
                value = 1 / (1 + Math.exp(-value));
                newMatrix.set(i, j, value);
            }
        });
        return newMatrix;
    }

    public static Matrix sigmoidDerivative(Matrix M) {
        //TODO: implement
        return null;
    }

    public static double logarithmicCost(Matrix[] Theta) {
        //TODO: implement
        return 0;
    }

    public static double quadraticCost(Matrix[] Theta) {
        //TODO: implement
        return 0;
    }

    public static void gradientDescent(Matrix[] Theta, Matrix[] Gradients, double alpha, double lambda) {
        //TODO: implement
    }

    public static Matrix addColumOfOnesToMatrix(Matrix M) {
        Matrix newMatrix = M.copy();
        Vector columnOfOnes = Vector.constant(M.rows(), 1);
        newMatrix = newMatrix.insertColumn(0, columnOfOnes);
        return newMatrix;
    }
}
