package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;

import java.util.HashMap;
import java.util.Map;

import static pl.pkjr.iad.console.ConsoleController.*;
import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.quadratic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.linear;
import static pl.pkjr.iad.utility.ChartsUtil.plotError;
import static pl.pkjr.iad.utility.ChartsUtil.plotFunction;
import static pl.pkjr.iad.utility.ChartsUtil.plotFunctionAndApproximation;
import static pl.pkjr.iad.utility.MatrixUtil.readTestData;

/**
 * Created by patry on 06/03/2017.
 */
public class Exercise1 implements Exercise {

    private static final String kDataset1Path = "./datasets/ex1/dataset2_1.txt";
    private static final String kDataset2Path = "./datasets/ex1/dataset2_2.txt";
    private static final String kTestsetPath = "./datasets/ex1/testset2.txt";
    private static final String kChooseNumberOfNeuronsInHiddenLayer = "Choose number of neurons in hidden layer";
    private static final String kChooseAlpha = "Choose alpha";
    private static final String kChooseMaxEpochs = "Choose max number of epochs";
    private static final String kContinue = "Continue?(y/n)";
    private static final String kChooseDataset = "Choose Dataset";

    private static final int kMinNeuronsInHiddenLayer = 1;
    private static final int kMaxNeuronsInHiddenLayer = 20;
    private static final double kMinAlpha = 0.0;
    private static final double kMaxAlpha = 1.0;
    private static final int kMinMaxEpochs = 1;
    private static final int kMaxMaxEpochs = 1000000;
    private static final int kNumberOfHiddenLayers = 1;
    private static final double kLambda = 0.25;
    private static final double kEpsilon = 0.5;
    private static final int kXColumnNumber = 0;
    private static final int kYColumnNumber = 1;
    private static final int kMinDatasetNumber = 1;
    private static final int kMaxDatasetNumber = 2;

    private Matrix X;
    private Matrix Y;
    private Matrix X_test;
    private Matrix Y_test;

    private int numberOfNeuronsInHiddenLayer;
    private double alpha;
    private int maxEpochs;
    private int choosenDataset;

    private Map<Integer, String> datasetPaths;

    private void prepareDatasetPaths() {
        datasetPaths = new HashMap<>();
        datasetPaths.put(1, kDataset1Path);
        datasetPaths.put(2, kDataset2Path);
    }

    private void prepareDataset() {
        Matrix Dataset = readTestData(datasetPaths.get(choosenDataset));
        X = Dataset.getColumn(kXColumnNumber).toColumnMatrix();
        Y = Dataset.getColumn(kYColumnNumber).toColumnMatrix();
    }

    private void prepareTestSet() {
        Matrix Testset = readTestData(kTestsetPath);
        X_test = Testset.getColumn(kXColumnNumber).toColumnMatrix();
        Y_test = Testset.getColumn(kYColumnNumber).toColumnMatrix();
    }

    @SuppressWarnings("Duplicates")
    private void prepareParameters() {
        print(kChooseDataset);
        choosenDataset = getInt(kMinDatasetNumber, kMaxDatasetNumber);
        print(kChooseNumberOfNeuronsInHiddenLayer);
        numberOfNeuronsInHiddenLayer = getInt(kMinNeuronsInHiddenLayer, kMaxNeuronsInHiddenLayer);
        print(kChooseAlpha);
        alpha = getDouble(kMinAlpha, kMaxAlpha);
        print(kChooseMaxEpochs);
        maxEpochs = getInt(kMinMaxEpochs, kMaxMaxEpochs);
    }

    private void trainNetwork() {
        int neurons[] = {1, numberOfNeuronsInHiddenLayer, 1};
        NeuralNetwork network = new NeuralNetworkWithBias(X, Y, kNumberOfHiddenLayers, neurons, alpha, kLambda,
                kEpsilon, maxEpochs, quadratic, linear);
        network.fit();
        plotError(network.getErrorHistory());
        plotFunction(X, Y, "function");
        plotFunctionAndApproximation(X, Y, network, "approximation");
        plotFunction(X_test, Y_test, "testFunction");
        plotFunctionAndApproximation(X_test, Y_test, network, "testApproximation");
    }

    private void finish() {
        print(kContinue);
        if (getChar() == 'n') {
            App.setShouldContinue(false);
        }
    }

    @Override
    public void run() {
        prepareDatasetPaths();
        prepareParameters();
        prepareDataset();
        prepareTestSet();
        trainNetwork();
        finish();
    }
}
