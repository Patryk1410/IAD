package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithoutBias;

import static pl.pkjr.iad.console.ConsoleController.*;
import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.quadratic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;
import static pl.pkjr.iad.utility.ChartsUtil.plotAccuracy;
import static pl.pkjr.iad.utility.ChartsUtil.plotError;
import static pl.pkjr.iad.utility.MatrixUtil.readTestData;

/**
 * Created by patry on 06/03/2017.
 */
public class Exercise3 implements Exercise {

    private static final String kDatasetPath = "./datasets/ex3/dataset3_1.txt";
    private static final String kChooseNumberOfNeuronsInHiddenLayer = "Choose number of neurons in hidden layer";
    private static final String kChooseAlpha = "Choose alpha";
    private static final String kChooseMaxEpochs = "Choose max number of epochs";
    private static final String kContinue = "Continue?(y/n)";

    private static final int kMinNeuronsInHiddenLayer = 1;
    private static final int kMaxNeuronsInHiddenLayer = 4;
    private static final double kMinAlpha = 0.0;
    private static final double kMaxAlpha = 1.0;
    private static final int kMinMaxEpochs = 1;
    private static final int kMaxMaxEpochs = 100000;
    private static final int kNumberOfHiddenLayers = 1;
    private static final double kLambda = 0.0;
    private static final double kEpsilon = 0.5;

    private Matrix X;
    private Matrix Y;

    private int numberOfNeuronsInHiddenLayer;
    private double alpha;
    private int maxEpochs;

    private void prepareDataset() {
        X = readTestData(kDatasetPath);
        Y = X.copy();
    }

    private void prepareParameters() {
        print(kChooseNumberOfNeuronsInHiddenLayer);
        numberOfNeuronsInHiddenLayer = getInt(kMinNeuronsInHiddenLayer, kMaxNeuronsInHiddenLayer);
        print(kChooseAlpha);
        alpha = getDouble(kMinAlpha, kMaxAlpha);
        print(kChooseMaxEpochs);
        maxEpochs = getInt(kMinMaxEpochs, kMaxMaxEpochs);
    }

    private void trainNetwork() {
        int neurons[] = {4, numberOfNeuronsInHiddenLayer, 4};
        NeuralNetwork network = new NeuralNetworkWithoutBias(X, Y, kNumberOfHiddenLayers, neurons, alpha, kLambda,
                kEpsilon, maxEpochs, quadratic, sigmoid);
        network.fit();
        plotAccuracy(network.getAccuracyHistory());
        plotError(network.getErrorHistory());
    }

    private void finish() {
        print(kContinue);
        if (getChar() == 'n') {
            App.setShouldContinue(false);
        }
    }

    @Override
    public void run() {
        prepareDataset();
        prepareParameters();
        trainNetwork();
        finish();
    }
}
