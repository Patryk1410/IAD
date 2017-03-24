package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import pl.pkjr.iad.machineLearning.costFunction.CostFunction;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionSelector;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;
import pl.pkjr.iad.utility.MatrixUtil;

/**
 * Created by patry on 08/03/2017.
 */
public class NeuralNetwork {

    Matrix X; //training samples
    Matrix Y; //Expected values
    Matrix[] Theta; //weights of connection between each pair of connected neurons
    Matrix[] Z; //output of j-th neuron in i-th training example in k-th layer
    CostFunction costFunction;
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
                         double alpha, double lambda, double epsilon, int maxEpochs,
                         CostFunctionType costFunction) {
        X = x;
        Y = y;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInEachLayer = numbersOfNeuronsInEachLayer;
        this.alpha = alpha;
        this.lambda = lambda;
        this.epsilon = epsilon;
        this.maxEpochs = maxEpochs;
        this.costFunction = CostFunctionSelector.getCostFunction(costFunction);
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
            Matrix PreviousMatrix = MatrixUtil.addColumnOfOnesToMatrix(Z[i - 1]);
            Matrix CurrentTheta = Theta[i - 1]; //i - 1, because we need first theta matrix to predict values for
                                                //second layer
            Z[i] = MatrixUtil.sigmoid(PreviousMatrix.multiply(CurrentTheta));
        }
    }

    public double J() {
        return costFunction.calculateCost(Theta, m, Y, Z[Z.length - 1], lambda);
    }

    public Matrix[] computeGradients() {
        //TODO: implement
        return null;
    }

    //////////////Getters and setters//////////////
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
