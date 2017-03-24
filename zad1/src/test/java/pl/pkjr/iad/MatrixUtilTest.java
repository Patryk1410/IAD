package pl.pkjr.iad;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.machineLearning.MachineLearningAlgorithm;
import pl.pkjr.iad.utility.MatrixUtil;

public class MatrixUtilTest {

    private static Matrix M;

    @BeforeClass
    public static void init() {
        double[][] numbers = {{1, 2}, {2, 3}};
        M = new Basic2DMatrix(numbers);
    }

    @Test
    public void testLambda() {
        MatrixUtil.sigmoid(M);
    }

    @Test
    public void testSigmoid() {
        double[][] numbersIn = {{11, 0}, {0, -11}};
        double[][] numbersOut = {{1, 0.5}, {0.5, 0}};
        Matrix M = new Basic2DMatrix(numbersIn);
        M = MatrixUtil.sigmoid(M);
        M.each((int i, int j, double value) -> Assert.assertEquals(numbersOut[i][j], value, 0.1));
    }

    @Test
    public void testAddColumnsOfOnesToMatrix() {
        Matrix newM = MatrixUtil.addColumnOfOnesToMatrix(M);
        Assert.assertEquals(3, newM.columns());
        newM.getColumn(0).each((int i, double value) -> Assert.assertEquals(1, value, 0.01));
    }

    @Test
    public void testLog() {
        double[][] numbers = {{1, 2}, {2, 3}};
        for (double[] numberArr : numbers) {
            for (int i = 0; i < numberArr.length; ++i) {
                numberArr[i] = Math.log(numberArr[i]);
            }
        }
        Matrix newM = MatrixUtil.log(M);
        newM.each((int i, int j, double value) -> Assert.assertEquals(numbers[i][j], value, 0.01));
    }
}
