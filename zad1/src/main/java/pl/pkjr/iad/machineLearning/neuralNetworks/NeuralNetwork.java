package pl.pkjr.iad.machineLearning.neuralNetworks;

import org.la4j.Matrix;
import pl.pkjr.iad.machineLearning.MachineLearningAlgorithm;
import pl.pkjr.iad.machineLearning.costFunction.CostFunction;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionSelector;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;
import pl.pkjr.iad.machineLearning.outputFunction.OutputFunction;
import pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionSelector;
import pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType;

import java.util.List;

import static pl.pkjr.iad.utility.VectorUtil.getIndexOfMaxElement;

/**
 * Created by patry on 08/03/2017.
 */
public abstract class NeuralNetwork {

    private static final int kMaxAccuracy = 1;

    Matrix X; //training samples
    Matrix Y; //Expected values
    Matrix[] PreviousTheta; //used to compute momentum
    Matrix[] Theta; //weights of connection between each pair of connected neurons
    Matrix[] Z; //input of j-th neuron in i-th training example in k-th layer
    Matrix[] A; //output of j-th neuron in i-th training example in k-th layer
    Matrix[] Delta; //error of j-th neuron in i-th training example in k-th layer
    Matrix[] Gradients; //gradients used in Gradient Descent algorithm
    CostFunction costFunction;
    OutputFunction outputFunction;
    int n; //number of features
    int m; //number of training examples
    int numberOfHiddenLayers;
    int[] numbersOfNeuronsInEachLayer; //without bias
    double alpha; //learning rate
    double lambda; //regularization rate
    double epsilon; //range of theta initial values
    int maxEpochs;
    NeuralNetworkUtil util;
    List<Double> accuracyHistory;
    List<Double> errorHistory;

    public NeuralNetwork(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInEachLayer,
                         double alpha, double lambda, double epsilon, int maxEpochs,
                         CostFunctionType costFunction, OutputFunctionType outputFunction) {
        X = x;
        Y = y;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInEachLayer = numbersOfNeuronsInEachLayer;
        this.alpha = alpha;
        this.lambda = lambda;
        this.epsilon = epsilon;
        this.maxEpochs = maxEpochs;
        this.costFunction = CostFunctionSelector.getCostFunction(costFunction);
        this.outputFunction = OutputFunctionSelector.getOutputFunction(outputFunction);
    }

    public void fit() {
        for (int i = 0; i < maxEpochs; ++i) {
            forwardPropagate();
            backpropagate();
            accuracyHistory.add(computeAccuracy());
            errorHistory.add(J());
        }
    }

    protected abstract void forwardPropagate();

    public abstract Matrix predict(Matrix input);

    private double J() {
        return costFunction.calculateCost(Theta, m, Y, A[A.length - 1], lambda);
    }

    private void backpropagate() {
        computeErrors();
        computeGradients();
        regularizeGradients();
        MachineLearningAlgorithm.gradientDescent(Theta, PreviousTheta, Gradients, alpha);
    }

    private void computeErrors() {
        for (int j = Delta.length - 1; j >= 0; --j) {
            if (j == Delta.length - 1) {
                computeErrorsForOutputLayer();
            } else if (j == Delta.length - 2) {
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

    protected abstract void computeErrorsForLastHiddenLayer();

    protected abstract void computeErrorsForHiddenLayer(int index);

    protected abstract void computeGradients();

    protected abstract void regularizeGradients();

    private double computeAccuracy() {
        if (hasOneOutputNeuron()) {
            return computeAccuracyForOneOutputNeuron();
        } else {
            return computeAccuracyForMultipleOutputNeurons();
        }
    }

    private double computeAccuracyForOneOutputNeuron() {
        int correctPredictions = 0;
        int lastAIndex = numberOfHiddenLayers;
        for (int i = 0; i < m; ++i) {
            if (Math.round(A[lastAIndex].get(i, 0)) ==  Y.get(i,0)) {
                correctPredictions++;
            }
        }
        return (double)correctPredictions / m;
    }

    private double computeAccuracyForMultipleOutputNeurons() {
        int correctPredictions = 0;
        int lastAIndex = numberOfHiddenLayers;
        for (int i = 0; i < m; ++i) {
            int expected = getIndexOfMaxElement(Y.getRow(i));
            int actual = getIndexOfMaxElement(A[lastAIndex].getRow(i));
            if (expected == actual) {
                correctPredictions++;
            }
        }
        return (double)correctPredictions / m;
    }

    private boolean hasOneOutputNeuron() {
        int outputLayerIndex = numberOfHiddenLayers + 1;
        return numbersOfNeuronsInEachLayer[outputLayerIndex] == 1;
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

    public List<Double> getAccuracyHistory() {
        return accuracyHistory;
    }

    public List<Double> getErrorHistory() {
        return errorHistory;
    }
}
