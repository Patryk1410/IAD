package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
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
    Matrix[] A; //input of j-th neuron in i-th training example in k-th layer
    Matrix[] Delta; //error of j-th neuron in i-th training example in k-th layer
    Matrix[] Gradients; //gradients used in Gradient Descent algorithm
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
    }

    public void fit() {
        for (int i = 0; i < maxEpochs; ++i) {
            predict();
            backpropagate();
        }
    }

    public void predict() {
        //Starting from i=1, because we don't need to predict values from input layer
        for (int i = 1; i < Z.length; ++i) {
            Matrix PreviousMatrix =
                    i == 1 ?
                    MatrixUtil.addColumnOfOnesToMatrix(X) :
                    MatrixUtil.addColumnOfOnesToMatrix(A[i - 2]);
            Matrix CurrentTheta = Theta[i - 1]; //i - 1, because we need first theta matrix to predict values for
                                                //second layer
            Z[i] = PreviousMatrix.multiply(CurrentTheta);
            A[i - 1] = MatrixUtil.sigmoid(Z[i]);
        }
    }

    public double J() {
        return costFunction.calculateCost(Theta, m, Y, A[A.length - 1], lambda);
    }

    private void backpropagate() {

        for (int j = Delta.length - 1; j >= 0; --j) {
            if (j == Delta.length - 1) {
                computeErrorsForOutputLayer();
            } else if (j == Delta.length - 2){
                computeErrorsForLastHiddenLayer();
            } else {
                computeErrorsForHiddenLayer(j);
            }
        }
    }

    private void computeErrorsForOutputLayer() {
        int indexOfOutputLayer = numberOfHiddenLayers;
        Delta[indexOfOutputLayer] = A[indexOfOutputLayer].subtract(Y);
    }

    private void computeErrorsForLastHiddenLayer() {
        int indexOfOutputLayer = numberOfHiddenLayers;
        int indexOfLastHiddenLayer = numberOfHiddenLayers - 1;
        Delta[indexOfLastHiddenLayer] = MatrixUtil.elementwiseMultiply(
                Delta[indexOfOutputLayer].multiply(Theta[indexOfOutputLayer].transpose()),
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[indexOfOutputLayer])));
    }

    private void computeErrorsForHiddenLayer(int index) {
        Delta[index] = MatrixUtil.elementwiseMultiply(
                Delta[index + 1].removeFirstColumn().multiply(Theta[index + 1].transpose()),
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[index + 1])));
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
