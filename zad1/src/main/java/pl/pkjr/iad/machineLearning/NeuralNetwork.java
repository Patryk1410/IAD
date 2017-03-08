package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

/**
 * Created by patry on 08/03/2017.
 */
public class NeuralNetwork {

    private Matrix X;
    private Matrix Y;
    private Matrix[] Theta;
    private int n;
    private int m;
    private int numberOfHiddenLayers;
    private int[] numbersOfNeuronsInHiddenLayer;
    private int numberOfNeuronsInOutputLayer;
    private double alpha;
    private double lambda;
    private int maxEpochs;

    public NeuralNetwork(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInHiddenLayer,
                         int numberOfOutputLayers, double alpha, double lambda, int maxEpochs) {
        X = x;
        Y = y;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInHiddenLayer = numbersOfNeuronsInHiddenLayer;
        this.numberOfNeuronsInOutputLayer = numberOfOutputLayers;
        this.alpha = alpha;
        this.lambda = lambda;
        this.maxEpochs = maxEpochs;
        initParameters();
    }

    private void initParameters() {
        n = X.columns();
        m = X.rows();
        randomlyInitTheta();
    }

    private void randomlyInitTheta() {
        Theta = new Matrix[numberOfHiddenLayers + numberOfNeuronsInOutputLayer];
        for (int i = 0; i < Theta.length; ++i) {
            if (i != numberOfHiddenLayers + numberOfNeuronsInOutputLayer - 1) {
                initHiddenTheta(i);
            } else {
                initOutputTheta();
            }
        }
    }

    private void initHiddenTheta(int i) {
        int rows = numbersOfNeuronsInHiddenLayer[i];
        int columns = numbersOfNeuronsInHiddenLayer[i + 1] - 1;
        Theta[i] = new Basic2DMatrix(rows, columns);
    }

    private void initOutputTheta() {
        int lastIndex = numberOfHiddenLayers - 1;
        int rows = numbersOfNeuronsInHiddenLayer[lastIndex];
        int columns = numberOfNeuronsInOutputLayer;
        Theta[lastIndex] = new Basic2DMatrix(rows, columns);
    }
}
