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
    private static final double stopCondition = 0.00001;

    private int stopCounter = 0;

    Matrix X; //training samples
    Matrix Y; //Expected values
    Matrix X_t; //test samples
    Matrix Y_t; //expected values for test samples
    Matrix[] Momentum; //used to compute momentum
    Matrix[] Theta; //weights of connection between each pair of connected neurons
    Matrix[] Z; //input of j-th neuron in i-th training example in k-th layer
    Matrix[] A; //output of j-th neuron in i-th training example in k-th layer
    Matrix[] Sigma; //error of j-th neuron in i-th training example in k-th layer
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
    double mu; //momentum rate
    int maxEpochs;
    NeuralNetworkUtil util;
    List<Double> accuracyHistory;
    List<Double> testAccuracyHistory;
    List<Double> errorHistory;
    List<Double> testErrorHistory;

    public NeuralNetwork(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInEachLayer,
                         double alpha, double lambda, double epsilon, int maxEpochs, double mu,
                         CostFunctionType costFunction, OutputFunctionType outputFunction, Matrix X_t, Matrix Y_t) {
        X = x;
        Y = y;
        this.X_t = X_t;
        this.Y_t = Y_t;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInEachLayer = numbersOfNeuronsInEachLayer;
        this.alpha = alpha;
        this.lambda = lambda;
        this.epsilon = epsilon;
        this.maxEpochs = maxEpochs;
        this.mu = mu;
        this.costFunction = CostFunctionSelector.getCostFunction(costFunction);
        this.outputFunction = OutputFunctionSelector.getOutputFunction(outputFunction);
    }

    public void fit() {
        int i = 0;
        for (; i < maxEpochs; ++i) {
            forwardPropagate();
            backpropagate();
//            if (i % 50 == 0) {
                accuracyHistory.add(computeAccuracy(false));
//                testAccuracyHistory.add(computeAccuracy(true));
                errorHistory.add(J());
//                testErrorHistory.add(J_t());
//                ConsoleController.print("Epoch: " + Integer.toString(i));
                if (accuracyHistory.get(i) == kMaxAccuracy) {
                    break;
                }
//            }

//            if (i > 0 && Math.abs(errorHistory.get(i) - errorHistory.get(i - 1)) < stopCondition) {
//                ++stopCounter;
//                if (stopCounter > 10) {
//                    break;
//                }
//            } else {
//                stopCounter = 0;
//            }
        }
    }

    protected abstract void forwardPropagate();

    public abstract Matrix predict(Matrix input);

    private double J() {
        return costFunction.calculateCost(Theta, m, Y, A[A.length - 1], lambda);
    }

    private double J_t() { return costFunction.calculateCost(Theta, m, Y_t, predict(X_t), lambda); }

    private void backpropagate() {
        computeErrors();
        computeGradients();
        regularizeGradients();
        MachineLearningAlgorithm.gradientDescent(Theta, Momentum, Gradients, alpha, mu);
    }

    private void computeErrors() {
        for (int j = Sigma.length - 1; j >= 0; --j) {
            if (j == Sigma.length - 1) {
                computeErrorsForOutputLayer();
            } else if (j == Sigma.length - 2) {
                computeErrorsForLastHiddenLayer();
            } else {
                computeErrorsForHiddenLayer(j);
            }
        }
    }

    private void computeErrorsForOutputLayer() {
        int indexOfOutputLayer = numberOfHiddenLayers;
        Sigma[indexOfOutputLayer] = A[indexOfOutputLayer].subtract(Y);
    }

    protected abstract void computeErrorsForLastHiddenLayer();

    protected abstract void computeErrorsForHiddenLayer(int index);

    protected abstract void computeGradients();

    protected abstract void regularizeGradients();

    private double computeAccuracy(boolean isTest) {
        if (hasOneOutputNeuron()) {
            return computeAccuracyForOneOutputNeuron();
        } else {
            return isTest ? computeTestAccuracyForMultipleOutputNeurons() : computeAccuracyForMultipleOutputNeurons();
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

    private double computeTestAccuracyForMultipleOutputNeurons() {
        int correctPredictions = 0;
        for (int i = 0; i < m; ++i) {
            int expected = getIndexOfMaxElement(Y_t.getRow(i));
            int actual = getIndexOfMaxElement(predict(X_t).getRow(i));
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

    public List<Double> getTestAccuracyHistory() {
        return testAccuracyHistory;
    }

    public List<Double> getErrorHistory() {
        return errorHistory;
    }

    public List<Double> getTestErrorHistory() {
        return testErrorHistory;
    }

    public Matrix getY() {
        return Y;
    }

    public Matrix getY_t() {
        return Y_t;
    }

    public Matrix[] getA() {
        return A;
    }
}
