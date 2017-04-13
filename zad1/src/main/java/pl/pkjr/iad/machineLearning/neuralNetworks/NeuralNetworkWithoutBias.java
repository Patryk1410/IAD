package pl.pkjr.iad.machineLearning.neuralNetworks;

import org.la4j.Matrix;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;
import pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType;
import pl.pkjr.iad.utility.MatrixUtil;

import static pl.pkjr.iad.utility.MatrixUtil.sigmoid;

/**
 * Created by patry on 01/04/2017.
 */
public class NeuralNetworkWithoutBias extends NeuralNetwork {
    public NeuralNetworkWithoutBias(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInEachLayer,
                                    double alpha, double lambda, double epsilon, int maxEpochs, double mu,
                                    CostFunctionType costFunction, OutputFunctionType outputFunction, Matrix X_t,
                                    Matrix Y_t) {
        super(x, y, numberOfHiddenLayers, numbersOfNeuronsInEachLayer, alpha, lambda, epsilon, maxEpochs, mu,
                costFunction, outputFunction, X_t, Y_t);
        util = new NeuralNetworkUtil(this, 0);
        util.initParameters();
    }

    @Override
    protected void forwardPropagate() {
        //Starting from i=1, because we don't need to forwardPropagate values from input layer
        for (int i = 1; i < Z.length; ++i) {
            Matrix PreviousMatrix = (i == 1) ? X.copy() : A[i - 2].copy();
            Matrix CurrentTheta = Theta[i - 1]; //i - 1, because we need first theta matrix to forwardPropagate values for
            //second layer
            Z[i] = PreviousMatrix.multiply(CurrentTheta);
            A[i - 1] = (i == (Z.length - 1)) ? outputFunction.activate(Z[i]) : sigmoid(Z[i]);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Matrix predict(Matrix input) {
        Matrix res = input.copy();
        for (int i = 1; i < Z.length; ++i) {
            Matrix CurrentTheta = Theta[i - 1];
            res = res.multiply(CurrentTheta);
            if (i == Z.length - 1) {
                res = outputFunction.activate(res);
            } else {
                res = sigmoid(res);
            }
        }
        return res;
    }

    @Override
    protected void computeErrorsForLastHiddenLayer() {
        int indexOfOutputLayer = numberOfHiddenLayers;
        int indexOfLastHiddenLayer = numberOfHiddenLayers - 1;
        Sigma[indexOfLastHiddenLayer] = MatrixUtil.elementwiseMultiply(
                Sigma[indexOfOutputLayer].multiply(Theta[indexOfOutputLayer].transpose()),
                MatrixUtil.sigmoidDerivative(Z[indexOfOutputLayer]));
    }

    @Override
    protected void computeErrorsForHiddenLayer(int index) {
        Sigma[index] = MatrixUtil.elementwiseMultiply(
                Sigma[index + 1].removeFirstColumn().multiply(Theta[index + 1].transpose()),
                MatrixUtil.sigmoidDerivative(Z[index + 1]));
    }

    @Override
    protected void computeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            if (j == 0) {
                Gradients[j] = Gradients[j].add(X.transpose().multiply(Sigma[j]));
            } else {
                Gradients[j] = Gradients[j].add(A[j - 1].transpose().multiply(Sigma[j]));
            }
        }
    }

    @Override
    protected void regularizeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            Matrix reg = Theta[j].multiply(lambda / m);
            Gradients[j] = Gradients[j].multiply(1.0 / m).add(reg);
        }
    }
}
