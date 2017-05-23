package util;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * Created by patry on 23/05/17.
 */
public class MatrixUtil {

    private static final int SIGMOID_LARGE_VALUE = 1;
    private static final int SIGMOID_SMALL_VALUE = 0;
    private static final int SIGMOID_GRADIENT_LARGE_VALUE = 0;
    private static final int SIGMOID_GRADIENT_SMALL_VALUE = 0;

    private static MatrixUtil instance;

    public static MatrixUtil getInstance() {
        return instance;
    }

    private MatrixUtil() {}

    public Matrix addColumnOfOnesToMatrix(Matrix M) {
        Matrix newMatrix = M.copy();
        Vector columnOfOnes = Vector.constant(M.rows(), 1);
        newMatrix = newMatrix.insertColumn(0, columnOfOnes);
        return newMatrix;
    }

    public Matrix sigmoid(Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> {
            if (value > 10) {
                newMatrix.set(i, j, SIGMOID_LARGE_VALUE);
            } else if (value < -10) {
                newMatrix.set(i, j, SIGMOID_SMALL_VALUE);
            } else {
                value = sigmoid(value);
                newMatrix.set(i, j, value);
            }
        });
        return newMatrix;
    }

    public Matrix sigmoidDerivative(Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> {
            if (value > 8) {
                newMatrix.set(i, j, SIGMOID_GRADIENT_LARGE_VALUE);
            } else if (value < -8) {
                newMatrix.set(i, j, SIGMOID_GRADIENT_SMALL_VALUE);
            } else {
                double valueSigmoid = sigmoid(value);
                value = valueSigmoid * (1 - valueSigmoid);
                newMatrix.set(i, j, value);
            }
        });
        return newMatrix;
    }

    private double sigmoid(double value) {
        return 1 / (1 + Math.exp(-value));
    }
}