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
    private Matrix[] Z;
    private int n;
    private int m;
    private int numberOfHiddenLayers;
    private int[] numbersOfNeuronsInHiddenLayer;
    private int numberOfNeuronsInOutputLayer;
    private double alpha;
    private double lambda;
    private int maxEpochs;

    public NeuralNetwork(Matrix x, Matrix y, int numberOfHiddenLayers, int[] numbersOfNeuronsInHiddenLayer,
                         int numberOfNeuronsInOutputLayer, double alpha, double lambda, int maxEpochs) {
        X = x;
        Y = y;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numbersOfNeuronsInHiddenLayer = numbersOfNeuronsInHiddenLayer;
        this.numberOfNeuronsInOutputLayer = numberOfNeuronsInOutputLayer;
        this.alpha = alpha;
        this.lambda = lambda;
        this.maxEpochs = maxEpochs;
        initZ();
        initParameters();
    }

    private void initParameters() {
        n = X.columns();
        m = X.rows();
        randomlyInitTheta();
    }

    private void randomlyInitTheta() {
        Theta = new Matrix[numberOfHiddenLayers + 1];
        for (int i = 0; i < Theta.length; ++i) {
            if (i == 0) {
                initFirstTheta();
            } else if (i != numberOfHiddenLayers + numberOfNeuronsInOutputLayer - 1) {
                initHiddenTheta(i);
            } else {
                initOutputTheta();
            }
        }
    }

    private void initZ() {
        Z = new Matrix[numberOfHiddenLayers + 1];
        for (int i = 0; i < Z.length; ++i) {
            Z[i] = new Basic2DMatrix()
        }
    }

    //TODO if there is no hidden layer change this method
    private void initFirstTheta() {
        int rows = n;
        int columns = numbersOfNeuronsInHiddenLayer[0] - 1;
        Theta[0] = new Basic2DMatrix(rows, columns);
    }

    //TODO if there is no hidden layer change this method
    private void initHiddenTheta(int i) {
        int rows = numbersOfNeuronsInHiddenLayer[i - 1];
        int columns = numbersOfNeuronsInHiddenLayer[i] - 1;
        Theta[i] = new Basic2DMatrix(rows, columns);
    }

    //TODO if there is no hidden layer change this method
    private void initOutputTheta() {
        int lastIndex = numberOfHiddenLayers - 1;
        int rows = numbersOfNeuronsInHiddenLayer[lastIndex];
        int columns = numberOfNeuronsInOutputLayer;
        Theta[lastIndex] = new Basic2DMatrix(rows, columns);
    }

    public void fit() {
        for (int i = 0; i < numberOfHiddenLayers; ++i) {

        }
    }

    public void predict() {

    }

    private void learn() {

    }
}
