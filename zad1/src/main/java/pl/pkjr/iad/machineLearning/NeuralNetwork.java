package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.machineLearning.costFunction.CostFunction;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionSelector;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;
import pl.pkjr.iad.utility.MatrixUtil;

import java.util.List;

/**
 * Created by patry on 08/03/2017.
 */
public class NeuralNetwork {

    private static final int kMaxAcuracy = 1;

    Matrix X; //training samples
    Matrix Y; //Expected values
    Matrix[] Theta; //weights of connection between each pair of connected neurons
    Matrix[] Z; //input of j-th neuron in i-th training example in k-th layer
    Matrix[] A; //output of j-th neuron in i-th training example in k-th layer
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
    List<Double> accuracyHistory;

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
            accuracyHistory.add(computeAccuracy());
            if (computeAccuracy() == kMaxAcuracy) {
                break;
            }
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
        computeErrors();
        computeGradients();
        regularizeGradients();
        MachineLearningAlgorithm.gradientDescent(Theta, Gradients, alpha);
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

    private void computeErrorsForLastHiddenLayer() {
        int indexOfOutputLayer = numberOfHiddenLayers;
        int indexOfLastHiddenLayer = numberOfHiddenLayers - 1;
        //TODO: split into separate lines so it's easier to debug
        Delta[indexOfLastHiddenLayer] = MatrixUtil.elementwiseMultiply(
                Delta[indexOfOutputLayer].multiply(Theta[indexOfOutputLayer].transpose()),
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[indexOfOutputLayer])));
    }

    private void computeErrorsForHiddenLayer(int index) {
        //TODO: split into separate lines so it's easier to debug
        Delta[index] = MatrixUtil.elementwiseMultiply(
                Delta[index + 1].removeFirstColumn().multiply(Theta[index + 1].transpose()),
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[index + 1])));
    }

    private void computeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            if (j == Gradients.length - 1) {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(A[j - 1]).transpose().multiply(Delta[j]));
            } else if (j == 0) {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(X).transpose().multiply(
                        Delta[j].removeFirstColumn()));
            } else {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(A[j - 1].transpose().multiply(
                        Delta[j].removeFirstColumn())));
            }
        }
    }

    private void regularizeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            Matrix reg = Theta[j].multiply(lambda / m);
            //putting column of zeros at the beginning, because we don't want to regularize biases
            reg.getColumn(0).each((int i, double value) -> reg.set(i, 0, 0));
            Gradients[j] = Gradients[j].multiply(1.0 / m).add(reg);
        }
    }

    private double computeAccuracy() {
        //TODO: generalize
        int wrongPredictions = 0;
        int lastAIndex = 1;
        for (int i = 0; i < m; ++i) {
           if ((A[lastAIndex].get(i, 0) >= 0.5 && Y.get(i,0) == 0) ||
                   (A[lastAIndex].get(i,0) < 0.5 && Y.get(i,0) == 1)) {
               wrongPredictions++;
           }
        }
        return 1 - (double)wrongPredictions / m;
    }

    private void printPrediction() {
        int lastAIndex = 1;
        for (int i  = 0; i < m; ++i) {
            ConsoleController.print("Expected:");
            ConsoleController.print((Y.getRow(i).toString()));
            ConsoleController.print("Actual:");
            ConsoleController.print(A[lastAIndex].getRow(i).toString());
//            ConsoleController.print("\n");
        }
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
}
