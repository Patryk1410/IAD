package pl.pkjr.iad.machineLearning.neuralNetworks;

import org.la4j.Matrix;
import pl.pkjr.iad.machineLearning.costFunction.CostFunctionType;
import pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType;
import pl.pkjr.iad.utility.MatrixUtil;

import static pl.pkjr.iad.utility.MatrixUtil.addColumnOfOnesToMatrix;
import static pl.pkjr.iad.utility.MatrixUtil.sigmoid;

/**
 * Created by patry on 01/04/2017.
 */
public class NeuralNetworkWithBias extends NeuralNetwork {

    public NeuralNetworkWithBias(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInEachLayer,
                                 double alpha, double lambda, double epsilon, int maxEpochs, double mu,
                                 CostFunctionType costFunction, OutputFunctionType outputFunction, Matrix X_t,
                                 Matrix Y_t) {
        super(x, y, numberOfHiddenLayers, numbersOfNeuronsInEachLayer, alpha, lambda, epsilon, maxEpochs, mu,
                costFunction, outputFunction, X_t, Y_t);
        util = new NeuralNetworkUtil(this, 1);
        util.initParameters();
    }

    @Override
    protected void forwardPropagate() {
        //Starting from i=1, because we don't need to forwardPropagate values from input layer
        for (int i = 1; i < Z.length; ++i) {
            Matrix PreviousMatrix =
                    i == 1 ?
                            MatrixUtil.addColumnOfOnesToMatrix(X) :
                            MatrixUtil.addColumnOfOnesToMatrix(A[i - 2]);
            Matrix CurrentTheta = Theta[i - 1]; //i - 1, because we need first theta matrix to forwardPropagate values for
            //second layer
            Z[i] = PreviousMatrix.multiply(CurrentTheta);
            A[i - 1] = (i == (Z.length - 1)) ? outputFunction.activate(Z[i]) : sigmoid(Z[i]);
        }
    }

//    @Override
//    protected void forwardPropagate_t() {
//
//    }

    @SuppressWarnings("Duplicates")
    @Override
    public Matrix predict(Matrix input) {
        Matrix res = MatrixUtil.addColumnOfOnesToMatrix(input);
        for (int i = 1; i < Z.length; ++i) {
            Matrix CurrentTheta = Theta[i - 1];
            res = res.multiply(CurrentTheta);
            if (i == Z.length - 1) {
                res = outputFunction.activate(res);
            } else {
                res = sigmoid(res);
                res = addColumnOfOnesToMatrix(res);
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
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[indexOfOutputLayer])));
    }

    @Override
    protected void computeErrorsForHiddenLayer(int index) {
        Sigma[index] = MatrixUtil.elementwiseMultiply(
                Sigma[index + 1].removeFirstColumn().multiply(Theta[index + 1].transpose()),
                MatrixUtil.sigmoidDerivative(MatrixUtil.addColumnOfOnesToMatrix(Z[index + 1])));
    }

    @Override
    protected void computeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            if (j == Gradients.length - 1) {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(A[j - 1]).transpose().multiply(Sigma[j]));
            } else if (j == 0) {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(X).transpose().multiply(
                        Sigma[j].removeFirstColumn()));
            } else {
                Gradients[j] = Gradients[j].add(MatrixUtil.addColumnOfOnesToMatrix(A[j - 1]).transpose().multiply(
                        Sigma[j].removeFirstColumn()));
            }
        }
    }

    @Override
    protected void regularizeGradients() {
        for (int j = Gradients.length - 1; j >= 0; --j) {
            Matrix reg = Theta[j].multiply(lambda / m);
            //putting column of zeros at the beginning, because we don't want to regularize biases
            reg.getColumn(0).each((int i, double value) -> reg.set(i, 0, 0));
            Gradients[j] = Gradients[j].multiply(1.0 / m).add(reg);
        }
    }

}
