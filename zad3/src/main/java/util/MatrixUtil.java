package util;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;

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

    public Matrix elementwiseMultiply(Matrix M1, Matrix M2) {
        if (M1.rows() != M2.rows() || M1.columns() != M2.columns()) {
            throw new IllegalArgumentException("Matrices must be of the same size");
        }
        Matrix newMatrix = new Basic2DMatrix(M1.rows(), M1.columns());
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j, M1.get(i, j) * M2.get(i, j)));
        return newMatrix;
    }
}
