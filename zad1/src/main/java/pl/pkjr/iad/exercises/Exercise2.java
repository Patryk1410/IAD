package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;
import pl.pkjr.iad.utility.DoubleUtil;
import pl.pkjr.iad.utility.LearningParams;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.List;

import static pl.pkjr.iad.console.ConsoleController.*;
import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.quadratic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;
import static pl.pkjr.iad.utility.ChartsUtil.*;
import static pl.pkjr.iad.utility.DoubleUtil.round;
import static pl.pkjr.iad.utility.MatrixUtil.prepareY;
import static pl.pkjr.iad.utility.MatrixUtil.readTestData;
import static pl.pkjr.iad.utility.VectorUtil.getIndexOfMaxElement;

/**
 * Created by patry on 06/03/2017.
 */
@SuppressWarnings("Duplicates")
public class Exercise2 implements Exercise {

    private static final String kDatasetPath = "./datasets/ex2/dataset1.txt";
    private static final String kTestsetPath = "./datasets/ex2/testset1.txt";
    private static final String kChooseNumberOfNeuronsInInputLayer = "Choose number of neurons in input layer";
    private static final String kChooseNumberOfNeuronsInHiddenLayer = "Choose number of neurons in hidden layer";
    private static final String kChooseAlpha = "Choose alpha";
    private static final String kChooseMaxEpochs = "Choose max number of epochs";
    private static final String kContinue = "Continue?(y/n)";

    private static final int kMinNeuronsInInputLayer = 1;
    private static final int kMaxNeuronsInInputLayer = 4;
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
    private static final int kYColumnNumber = 4;

    private static int iteration = 109;

    private Matrix X;
    private Matrix Y;
    private Matrix X_test;
    private Matrix Y_test;

    private int numberOfNeuronsInInputLayer;
    private int numberOfNeuronsInHiddenLayer;
    private double alpha;
    private double mu;
    private int maxEpochs = 25000;

    private int[][] table = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    private void resetTable() {
        table = new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    }

    private void fillTable(Matrix A, Matrix Y) {
        for (int i = 0; i < A.rows(); ++i) {
            int expected = getIndexOfMaxElement(Y.getRow(i));
            int actual = getIndexOfMaxElement(A.getRow(i));
            table[expected][actual]++;
        }
    }

    private void saveTableToFile(String fileName) {
        try(FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("\t\t\\begin{table}[H]\n");
            bw.write("\t\t\t\\setlength\\extrarowheight{15pt}\n");
            bw.write("\t\t\t\\begin{tabular}{|c|c|c|c|c|}\n");
            bw.write("\t\t\t\t\\cline{3-5}\n");
            bw.write("\t\t\t\t\\multicolumn{1}{c}{ }&\\multicolumn{1}{c|}{ }& \\multicolumn{3}{c|}{Wartość przewidziana}  \\\\\n");
            bw.write("\t\t\t\t\\cline{3-5}\n");
            bw.write("\t\t\t\t\\multicolumn{1}{c}{ }&\\multicolumn{1}{c|}{ }&K1&K2&K3\\\\\n");
            bw.write("\t\t\t\t\\hline\n");
            bw.write("\t\t\t\t\\parbox[t]{2mm}{\\multirow{3}{*}{\\rotatebox[origin=c]{90}{Wartość oczekiwana}}}\n");
            for (int i = 0; i < 3; ++i) {
                String endl = (i == 2 ? "\\\\\\hline" : "\\\\\\cline{2-5}") + "\n";
                bw.write("\t\t\t\t& K" + Integer.toString(i + 1)
                        + "&" + Integer.toString(table[i][0])
                        + "&" + Integer.toString(table[i][1])
                        + "&" + Integer.toString(table[i][2]) + endl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void otherParams(String fileName) {
        double[] precision = new double[3];
        for (int i = 0; i < 3; ++i) {
            precision[i] = (double)table[i][i] / (table[0][i] + table[1][i] + table[2][i]);
        }
        double[] recall = new double[3];
        for (int i = 0; i < 3; ++i) {
            recall[i] = (double)table[i][i] / (table[i][0] + table[i][1] + table[i][2]);
        }
        double f1[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            f1[i] = 2 * precision[i] * recall[i] / (precision[i] + recall[i]);
        }
        try(FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw)) {
            for (int i = 0; i < 3; ++i) {
                bw.write("K" + Integer.toString(i + 1) + ":\n"
                        + "precision: " + round(precision[i]) + "\n"
                        + "recall: " + round(recall[i]) + "\n"
                        + "f1: " + round(f1[i]) + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(NeuralNetwork neuralNetwork) {
        try(FileWriter fw = new FileWriter("./res/ex2/res.txt", true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            List<Double> errors = neuralNetwork.getErrorHistory();
            List<Double> testErrors = neuralNetwork.getTestErrorHistory();
            List<Double> accuracy = neuralNetwork.getAccuracyHistory();
            List<Double> testAccuracy = neuralNetwork.getTestAccuracyHistory();
            String line = Integer.toString(numberOfNeuronsInInputLayer)
                    + "&" + Integer.toString(numberOfNeuronsInHiddenLayer) + "&" + Double.toString(alpha) + "&"
                    + Double.toString(mu) + "&" + Integer.toString(errors.size())
                    + "&" + round(errors.get(errors.size() - 1))
                    + "&" + round(testErrors.get(testErrors.size() - 1))
                    + "&" + round(accuracy.get(accuracy.size() - 1))
                    + "&" + round(testAccuracy.get(testAccuracy.size() - 1))
                    + "\\\\" + "\n";
            bw.write(line);
            bw.write("\\hline\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareDataset() {
        Matrix Dataset = readTestData(kDatasetPath);
        X = Dataset.slice(0,0, Dataset.rows(), 4);
        Y = Dataset.getColumn(kYColumnNumber).toColumnMatrix();
        Y = prepareY(Y, 3);
    }

    private void prepareTestSet() {
        Matrix Testset = readTestData(kTestsetPath);
        X_test = Testset.slice(0, 0, Testset.rows(), 4);
        Y_test = Testset.getColumn(kYColumnNumber).toColumnMatrix();
        Y_test = prepareY(Y_test, 3);
    }

    private void prepareParameters() {
        print(kChooseNumberOfNeuronsInInputLayer);
        numberOfNeuronsInInputLayer = getInt(kMinNeuronsInInputLayer, kMaxNeuronsInInputLayer);
        print(kChooseNumberOfNeuronsInHiddenLayer);
        numberOfNeuronsInHiddenLayer = getInt(kMinNeuronsInHiddenLayer, kMaxNeuronsInHiddenLayer);
        print(kChooseAlpha);
        alpha = getDouble(kMinAlpha, kMaxAlpha);
        print(kChooseMaxEpochs);
        maxEpochs = getInt(kMinMaxEpochs, kMaxMaxEpochs);
    }

    private void trainNetwork() {
        int neurons[] = {numberOfNeuronsInInputLayer, numberOfNeuronsInHiddenLayer, 3};
        NeuralNetwork network = new NeuralNetworkWithBias(
                X.slice(0,0, X.rows(), numberOfNeuronsInInputLayer), Y, kNumberOfHiddenLayers,
                neurons, alpha, kLambda, kEpsilon, maxEpochs, kMu, quadratic, sigmoid,
                X_test.slice(0, 0, X_test.rows(), numberOfNeuronsInInputLayer), Y_test);
        network.fit();
        String errorFile = "./plots/ex2/err_" + Integer.toString(iteration) + ".jpg";
        String accFile = "./plots/ex2/acc_" + Integer.toString(iteration) + ".jpg";
        plotError(network.getErrorHistory(), network.getTestErrorHistory(), errorFile);
        plotAccuracy(network.getAccuracyHistory(), network.getTestAccuracyHistory(), accFile);
        saveToFile(network);
        fillTable(network.getA()[network.getA().length - 1], network.getY());
        String fileName = "./res/ex2/table" + Integer.toString(iteration) + ".txt";
        saveTableToFile(fileName);
        fileName = "./res/ex2/params" + Integer.toString(iteration) + ".txt";
        otherParams(fileName);
        resetTable();
        fillTable(network.getA()[network.getA().length - 1], network.getY_t());
        fileName = "./res/ex2/testTable" + Integer.toString(iteration) + ".txt";
        saveTableToFile(fileName);
        fileName = "./res/ex2/testParams" + Integer.toString(iteration) + ".txt";
        otherParams(fileName);
        resetTable();
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
        prepareTestSet();
        for (int h = 0; h < 4; ++h) {
            numberOfNeuronsInInputLayer = h + 1;
            for (int i = LearningParams.startPos[h]; i < LearningParams.length[h]; ++i) {
                numberOfNeuronsInHiddenLayer = LearningParams.h2[i];
                for (int j = 0; j < LearningParams.alpha.length; ++j) {
                    alpha = LearningParams.alpha[j];
                    for (int k = 0; k < LearningParams.mu.length; ++k) {
                        mu = LearningParams.mu[k];
                        ConsoleController.print("input: " + Integer.toString(numberOfNeuronsInInputLayer));
                        ConsoleController.print("hidden: " + Integer.toString(numberOfNeuronsInHiddenLayer));
                        ConsoleController.print("alpha: " + Double.toString(alpha));
                        ConsoleController.print("mu: " + Double.toString(mu));
                        ConsoleController.print("");
                        trainNetwork();
                        ++iteration;
                    }
                }
            }
        }
        finish();
    }
}
