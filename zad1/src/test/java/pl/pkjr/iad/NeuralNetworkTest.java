package pl.pkjr.iad;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.machineLearning.NeuralNetwork;
import pl.pkjr.iad.machineLearning.costFunction.CostFunction;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionSelector;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;

import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;

/**
 * Created by patry on 24/03/2017.
 */
public class NeuralNetworkTest {

    private static NeuralNetwork network;

    @BeforeClass
    public static void init() {
        double[][] dataX = {{1, 2, 3}, {3, 4, 5}};
        double[][] dataY = {{0}, {1}};
        Matrix X = new Basic2DMatrix(dataX);
        Matrix Y = new Basic2DMatrix(dataY);
        int[] neurons = {3, 2, 1};
        network = new NeuralNetwork(X, Y, 1, neurons, 0.1, 1, 1, 100,
                CostFunctionType.logarithmic, sigmoid);
    }

    @Test
    public void testLogarithmicCost() {
        CostFunction costFunction = CostFunctionSelector.getCostFunction(CostFunctionType.logarithmic);
        Matrix[] Theta = {new Basic2DMatrix()};
        double[][] data = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix Y = new Basic2DMatrix(data);
        Matrix H = Y.copy();
        double cost = costFunction.calculateCost(Theta, 10, Y, H, 0);
        Assert.assertEquals(0, cost, 0.01);
    }

    @Test
    public void testRunNetwork() {
        network.fit();
    }
}
