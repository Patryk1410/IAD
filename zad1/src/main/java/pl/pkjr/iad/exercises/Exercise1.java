package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;
import pl.pkjr.iad.utility.DoubleUtil;
import pl.pkjr.iad.utility.LearningParams;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.pkjr.iad.console.ConsoleController.*;
import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.quadratic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.linear;
import static pl.pkjr.iad.utility.ChartsUtil.plotError;
import static pl.pkjr.iad.utility.ChartsUtil.plotFunction;
import static pl.pkjr.iad.utility.ChartsUtil.plotFunctionAndApproximation;
import static pl.pkjr.iad.utility.DoubleUtil.round;
import static pl.pkjr.iad.utility.MatrixUtil.readTestData;

/**
 * Created by patry on 06/03/2017.
 */
@SuppressWarnings("Duplicates")
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
    private static final double kLambda = 0.0;
    private static final double kEpsilon = 0.5;
    private static final double kMu = 0.5;
    private static final int kXColumnNumber = 0;
    private static final int kYColumnNumber = 1;
    private static final int kMinDatasetNumber = 1;
    private static final int kMaxDatasetNumber = 2;

    private static int iteration = 1;

    private Matrix X;
    private Matrix Y;
    private Matrix X_test;
    private Matrix Y_test;

    private int numberOfNeuronsInHiddenLayer;
    private double alpha;
    private double mu;
    private int maxEpochs = 25000;
    private int chosenDataset = 1;


    private Map<Integer, String> datasetPaths;

    private void prepareDatasetPaths() {
        datasetPaths = new HashMap<>();
        datasetPaths.put(1, kDataset1Path);
        datasetPaths.put(2, kDataset2Path);
    }

    private void prepareDataset() {
        Matrix Dataset = readTestData(datasetPaths.get(chosenDataset));
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
        chosenDataset = getInt(kMinDatasetNumber, kMaxDatasetNumber);
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
                kEpsilon, maxEpochs, mu, quadratic, linear, X_test, Y_test);
        network.fit();
        String errorFile = "./plots/ex1/err1_" + Integer.toString(iteration) + ".jpg";
        plotError(network.getErrorHistory(), network.getTestErrorHistory(), errorFile);
//        plotFunction(X, Y, "function");
//        plotFunctionAndApproximation(X, Y, network, "approximation");
//        plotFunction(X_test, Y_test, "testFunction");
        String apprFile = "./plots/ex1/appr1_" + Integer.toString(iteration) + ".jpg";
        plotFunctionAndApproximation(X_test, Y_test, network, apprFile);
        saveToFile(network);
    }

    private void finish() {
        print(kContinue);
        if (getChar() == 'n') {
            App.setShouldContinue(false);
        }
    }

    private void saveToFile(NeuralNetwork neuralNetwork) {
        try(FileWriter fw = new FileWriter("./res/ex1.txt", true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            List<Double> errors = neuralNetwork.getErrorHistory();
            List<Double> testErrors = neuralNetwork.getTestErrorHistory();
            String line = Integer.toString(numberOfNeuronsInHiddenLayer) + "&" + Double.toString(alpha) + "&"
                    + Double.toString(mu) + "&" + Integer.toString(errors.size())
                    + "&" + (errors.size() > 100 ? round(errors.get(100)) : "+")
                    + "&" + (errors.size() > 1000 ? round(errors.get(1000)) : "+")
                    + "&" + (errors.size() > 10000 ? round(errors.get(10000)) : "+")
                    + "&" + (errors.size() > 24999 ? round(errors.get(24999)) : round(errors.get(errors.size() - 1)))
                    + "&" + (errors.size() > 100 ? round(testErrors.get(100)) : "+")
                    + "&" + (errors.size() > 1000 ? round(testErrors.get(1000)) : "+")
                    + "&" + (errors.size() > 10000 ? round(testErrors.get(10000)) : "+")
                    + "&" + (errors.size() > 24999 ? round(testErrors.get(24999)) :
                     round(testErrors.get(testErrors.size() - 1)))
                    + "\\\\" + "\n";
            bw.write(line);
            bw.write("\\hline\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        prepareDatasetPaths();
        prepareDataset();
        prepareTestSet();
        for (int i = 0; i < LearningParams.h.length; ++i) {
            numberOfNeuronsInHiddenLayer = LearningParams.h[i];
            for (int j = 0; j < LearningParams.alpha.length; ++j) {
                alpha = LearningParams.alpha[j];
                for (int k = 0; k < LearningParams.mu.length; ++k) {
                    mu = LearningParams.mu[k];
                    ConsoleController.print("hidden: " + Integer.toString(numberOfNeuronsInHiddenLayer));
                    ConsoleController.print("alpha: " + Double.toString(alpha));
                    ConsoleController.print("mu: " + Double.toString(mu));
                    ConsoleController.print("");
                    trainNetwork();
                    ++iteration;
                }
            }
        }
        finish();
    }
}
