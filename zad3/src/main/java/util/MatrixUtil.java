package util;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by patry on 23/05/17.
 */
public class MatrixUtil {

    private static final int SIGMOID_LARGE_VALUE = 1;
    private static final int SIGMOID_SMALL_VALUE = 0;
    private static final int SIGMOID_GRADIENT_LARGE_VALUE = 0;
    private static final int SIGMOID_GRADIENT_SMALL_VALUE = 0;

    private static MatrixUtil instance = new MatrixUtil();

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

    public Matrix readTestData(String filePath) {
        int counter = 0;
        try(FileReader fr = new FileReader(filePath);
            FileReader fr2 = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            BufferedReader br2 = new BufferedReader(fr2)) {
            int N = (int) br2.lines().count();
            double[][] dataset = new double[N][];
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
                dataset[counter] = new double[numbers.length];
                for (int i = 0; i < numbers.length; ++i) {
                    dataset[counter][i] = Double.parseDouble(numbers[i]);
                }
                counter++;
            }
            return new Basic2DMatrix(dataset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Matrix square(Matrix M) {
        Matrix newMatrix = M.copy();
        newMatrix.each((int i, int j, double value) -> newMatrix.set(i, j, value*value));
        return newMatrix;
    }
}
