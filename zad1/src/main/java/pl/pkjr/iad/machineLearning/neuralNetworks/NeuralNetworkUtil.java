package pl.pkjr.iad.machineLearning.neuralNetworks;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.ArrayList;

/**
 * Created by patry on 24/03/2017.
 */
public class NeuralNetworkUtil {

    private final static int NUMBER_OF_INPUT_LAYERS = 1;
    private final static int NUMBER_OF_OUTPUT_LAYERS = 1;

    private NeuralNetwork network;
    private int bias;

    public NeuralNetworkUtil(NeuralNetwork network, int bias) {
        this.network = network;
        this.bias = bias;
    }

    public void initParameters() {
        network.n = network.X.columns();
        network.m = network.X.rows();
        network.accuracyHistory = new ArrayList<>();
        network.testAccuracyHistory = new ArrayList<>();
        network.errorHistory = new ArrayList<>();
        network.testErrorHistory = new ArrayList<>();
        randomlyInitTheta();
        initPreviousTheta();
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
            int rows = network.numbersOfNeuronsInEachLayer[i] + bias;
            int columns = network.numbersOfNeuronsInEachLayer[i + 1];
            network.Theta[i] = new Basic2DMatrix(rows, columns);
        }
    }

    private void initPreviousTheta() {
        network.Momentum = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
        for (int i = 0; i < network.Momentum.length; ++i) {
            network.Momentum[i] = Matrix.zero(network.Theta[i].rows(), network.Theta[i].columns());
        }
    }

    private void initZ() {
        network.Z = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_INPUT_LAYERS + NUMBER_OF_OUTPUT_LAYERS];
        network.Z[0] = network.X.copy();
    }

    private void initA() {
        network.A = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
    }

    private void initDelta() {
        network.Sigma = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
    }

    private void initGradients() {
        network.Gradients = new Matrix[network.numberOfHiddenLayers + NUMBER_OF_OUTPUT_LAYERS];
        for (int i = 0; i < network.Gradients.length; ++i) {
            network.Gradients[i] = Matrix.zero(network.Theta[i].rows(), network.Theta[i].columns());
        }
    }

}
