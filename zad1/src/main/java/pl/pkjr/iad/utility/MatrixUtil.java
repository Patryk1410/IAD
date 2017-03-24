package pl.pkjr.iad.utility;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;

/**
 * Created by patry on 24/03/2017.
 */
public class MatrixUtil {

    private static final int SIGMOID_LARGE_VALUE = 1;
    private static final int SIGMOID_SMALL_VALUE = 0;

    public static Matrix sigmoid(Matrix M) {
        Matrix newMatrix = M.copy();
        M.each((int i, int j, double value) -> {
            if (value > 10) {
                newMatrix.set(i, j, SIGMOID_LARGE_VALUE);
            } else if (value < -10) {
                newMatrix.set(i, j, SIGMOID_SMALL_VALUE);
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

    public static Matrix addColumnOfOnesToMatrix(Matrix M) {
        Matrix newMatrix = M.copy();
        Vector columnOfOnes = Vector.constant(M.rows(), 1);
        newMatrix = newMatrix.insertColumn(0, columnOfOnes);
        return newMatrix;
    }

    public static Matrix log(Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j,
                Math.log(value) == Double.NEGATIVE_INFINITY ? Double.MAX_VALUE * (-1) : Math.log(value)));
        return newMatrix;
    }

    public static Matrix elementwiseMultiply(Matrix M1, Matrix M2) {
        if (M1.rows() != M2.rows() || M1.columns() != M2.columns()) {
            throw new IllegalArgumentException("Matrices must be of the same size");
        }
        Matrix newMatrix = new Basic2DMatrix(M1.rows(), M1.columns());
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j, M1.get(i, j) * M2.get(i, j)));
        return newMatrix;
    }

    public static Matrix subtractFromNumber(double number, Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j, number - value));
        return newMatrix;
    }

    public static Matrix square(Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j, value*value));
        return newMatrix;
    }
}
