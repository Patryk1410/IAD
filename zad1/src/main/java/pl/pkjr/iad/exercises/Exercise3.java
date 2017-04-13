package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithoutBias;
import pl.pkjr.iad.utility.LearningParams;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static pl.pkjr.iad.console.ConsoleController.*;
import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.quadratic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;
import static pl.pkjr.iad.utility.ChartsUtil.plotAccuracy;
import static pl.pkjr.iad.utility.ChartsUtil.plotError;
import static pl.pkjr.iad.utility.DoubleUtil.round;
import static pl.pkjr.iad.utility.MatrixUtil.readTestData;

/**
 * Created by patry on 06/03/2017.
 */
@SuppressWarnings("Duplicates")
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
    private double mu;
    private int maxEpochs = 25000;

    private static int iteration = 1;

    private void saveToFile(NeuralNetwork neuralNetwork) {
        try(FileWriter fw = new FileWriter("./res/ex3/resnb.txt", true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            List<Double> errors = neuralNetwork.getErrorHistory();
            List<Double> accuracy = neuralNetwork.getAccuracyHistory();
            String line = Integer.toString(numberOfNeuronsInHiddenLayer) + "&" + Double.toString(alpha) + "&"
                    + Double.toString(mu) + "&" + Integer.toString(errors.size())
                    + "&" + round(errors.get(errors.size() - 1))
                    + "&" + round(accuracy.get(accuracy.size() - 1))
                    + "\\\\" + "\n";
            bw.write(line);
            bw.write("\\hline\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                kEpsilon, maxEpochs, mu, quadratic, sigmoid, null, null);
        network.fit();
        String errorFile = "./plots/ex3/err3nb_" + Integer.toString(iteration) + ".jpg";
        String accFile = "./plots/ex3/acc3nb_" + Integer.toString(iteration) + ".jpg";
        plotError(network.getErrorHistory(), network.getTestErrorHistory(), errorFile);
        plotAccuracy(network.getAccuracyHistory(), network.getTestAccuracyHistory(), accFile);
        saveToFile(network);
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
        for (int i = 0; i < 3; ++i) {
            numberOfNeuronsInHiddenLayer = i + 1;
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
        trainNetwork();
        finish();
    }
}
