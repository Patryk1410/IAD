package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * Created by patry on 08/03/2017.
 */
public class NeuralNetwork {

    Matrix X; //training samples
    Matrix Y; //Expected values
    Matrix[] Theta; //weights of connection between each pair of connected neurons
    Matrix[] Z; //output of j-th neuron in i-th training example in k-th layer
    int n; //number of features
    int m; //number of training examples
    int numberOfHiddenLayers;
    int[] numbersOfNeuronsInEachLayer; //without bias
    double alpha; //learning rate
    double lambda; //regularization rate
    double epsilon; //range of theta initial values
    int maxEpochs;
    NeuralNetworkUtil util;

    public NeuralNetwork(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInEachLayer,
                         double alpha, double lambda, double epsilon,
                         int maxEpochs) {
        X = x;
        Y = y;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInEachLayer = numbersOfNeuronsInEachLayer;
        this.alpha = alpha;
        this.lambda = lambda;
        this.epsilon = epsilon;
        this.maxEpochs = maxEpochs;
        util = new NeuralNetworkUtil(this);
        util.initParameters();
        util.randomlyInitTheta();
        util.initZ();
    }

    public void fit() {
        for (int i = 0; i < Z.length; ++i) {

        }
    }

    public void predict() {
        //Starting from i=1, because we don't need to predict values from input layer
        for (int i = 1; i < Z.length; ++i) {
            Matrix PreviousMatrix = MachineLearningAlgorithm.addColumOfOnesToMatrix(Z[i - 1]);
            Matrix CurrentTheta = Theta[i - 1]; //i - 1, because we need first theta matrix to predict values for
                                                //second layer
            Z[i] = MachineLearningAlgorithm.sigmoid(PreviousMatrix.multiply(CurrentTheta));
        }
    }

    public double J() {
        //TODO: implement
        return 0;
    }

    public Matrix[] gradients() {
        //TODO: implement
        return null;
    }
}
