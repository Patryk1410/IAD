package pl.pkjr.iad;

import org.junit.BeforeClass;
import org.junit.Test;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.machineLearning.NeuralNetwork;

/**
 * Created by patry on 24/03/2017.
 */
public class NeuralNetworkTest {

    private static NeuralNetwork network;

    @BeforeClass
    public static void init() {
        double[][] dataX = {{1, 2, 3}, {3, 4, 5}};
        double[][] dataY = {{1, 2}};
        Matrix X = new Basic2DMatrix(dataX);
        Matrix Y = new Basic2DMatrix(dataY);
        int[] neurons = {3, 2, 1};
        network = new NeuralNetwork(X, Y, 1, neurons, 0.1, 0.1, 1, 100);
    }

    @Test
    public void testPredict() {
        network.predict();
    }
}
