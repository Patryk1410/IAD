package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.util.ArrayList;

/**
 * Created by patry on 24/03/2017.
 */
public class NeuralNetworkUtil {

    private final static int NUMBER_OF_INPUT_LAYERS = 1;
    private final static int NUMBER_OF_OUTPUT_LAYERS = 1;
    private final static int BIAS = 1;
    private final static int NO_BIAS = 0;

    private NeuralNetwork network;

    public NeuralNetworkUtil(NeuralNetwork network) {
        this.network = network;
    }

    public void initParameters() {
        network.n = network.X.columns();
        network.m = network.X.rows();
        network.accuracyHistory = new ArrayList<>();
        randomlyInitTheta();
        initZ();
        initA();
        initDelta();
        initGradients();
    }

    private void randomlyInitTheta() {
        initTheta();
        for (Matrix Theta : network.Theta) {
            for (int i = 0; i < Theta.rows(); ++i) {
                for (int j = 0; j < Theta.columns(); ++j) {
                    double randomNumber = (Math.random() * 2 * network.epsilon) - network.epsilon;
                    Theta.set(i, j, randomNumber);
                }
            }
        }
    }

    private void initTheta() {
        network.Theta = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
        for (int i = 0; i < network.Theta.length; ++i) {
            int rows = network.numbersOfNeuronsInEachLayer[i] + BIAS;
            int columns = network.numbersOfNeuronsInEachLayer[i + 1];
            network.Theta[i] = new Basic2DMatrix(rows, columns);
        }
    }

    private void initZ() {
        network.Z = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_INPUT_LAYERS + NUMBER_OF_OUTPUT_LAYERS];
//        int rows = network.m;
//        for (int i = 0; i < network.Z.length; ++i) {
//            int columns = network.numbersOfNeuronsInEachLayer[i];
//            network.Z[i] = new Basic2DMatrix(rows, columns);
//        }
        network.Z[0] = network.X.copy();
    }

    private void initA() {
        network.A = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
//        int rows = network.m;
//        for (int i = 0; i < network.A.length; ++i) {
//            int columns = network.numbersOfNeuronsInEachLayer[i + 1];//i + 1 because we don't use activation function
//                                                                     //on input layer to get output
//            network.A[i] = new Basic2DMatrix(rows, columns);
//        }
    }

    private void initDelta() {
        network.Delta = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
//        int rows = network.m;
//        for (int i = 0; i < network.Delta.length; ++i) {
//            int columns = network.numbersOfNeuronsInEachLayer[i + 1]; //i + 1 because we don't calculate error for
//                                                                      //input layer
//            network.Delta[i] = new Basic2DMatrix(rows, columns);
//        }
    }

    private void initGradients() {
        network.Gradients = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
        for (int i = 0; i < network.Gradients.length; ++i) {
            network.Gradients[i] = Matrix.zero(network.Theta[i].rows(), network.Theta[i].columns());
        }
    }

}
