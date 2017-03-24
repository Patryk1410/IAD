package pl.pkjr.iad;

import org.junit.Test;
import org.junit.Assert;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.machineLearning.MachineLearningAlgorithm;

public class MachineLearningAlgorithmTest {

    @Test
    public void testLambda() {
        double[][] numbers = {{1, 2}, {2, 3}};
        Matrix M = new Basic2DMatrix(numbers);
        MachineLearningAlgorithm.sigmoid(M);
    }

    @Test
    public void testSigmoid() {
        double[][] numbersIn = {{11, 0}, {0, -11}};
        double[][] numbersOut = {{1, 0.5}, {0.5, 0}};
        Matrix M = new Basic2DMatrix(numbersIn);
        M = MachineLearningAlgorithm.sigmoid(M);
        M.each((int i, int j, double value) -> {
            Assert.assertEquals(numbersOut[i][j], value, 0.1);
        });
    }

    @Test
    public void testAddColumnsOfOnesToMatrix() {
        double[][] numbers = {{1, 2}, {2, 3}};
        Matrix matrix = new Basic2DMatrix(numbers);
        matrix = MachineLearningAlgorithm.addColumOfOnesToMatrix(matrix);
        Assert.assertEquals(3, matrix.columns());
        matrix.getColumn(0).each((int i, double value) -> {
            Assert.assertEquals(1, value, 0.01);
        });
    }
}
