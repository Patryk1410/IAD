package cli;

import machineLearning.RadialNeuralNetwork;
import machineLearning.outputFunction.OutputFunctionType;
import machineLearning.positionTrainer.PositionTrainerType;
import org.la4j.Matrix;
import util.ChartsUtil;
import util.MatrixUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 20/05/17.
 */
public class ApproximationExercise implements InterfaceModule {

    private static final String DATASET_1_PATH = "./datasets/ex1/dataset2_1.txt";
    private static final String DATASET_2_PATH = "./datasets/ex1/dataset2_2.txt";
    private static final String TESTSET_PATH = "./datasets/ex1/testset2.txt";
    private static final String CHOOSE_NUMBER_OF_NEURONS_IN_HIDDEN_LAYER = "Choose number of neurons in hidden layer";
    private static final String CHOOSE_ALPHA = "Choose alpha";
    private static final String CHOOSE_MAX_EPOCHS = "Choose max number of epochs";
    private static final String CHOOSE_DATASET = "Choose Dataset";

    private static final int MIN_NEURONS_IN_HIDDEN_LAYER = 1;
    private static final int MAX_NEURONS_IN_HIDDEN_LAYER = 60;
    private static final double MIN_ALPHA = 0.0;
    private static final double MAX_ALPHA = 1.0;
    private static final int MIN_MAX_EPOCHS = 1;
    private static final int MAX_MAX_EPOCHS = 100000;

    private Matrix X;
    private Matrix Y;
    private Matrix XTest;
    private Matrix YTest;

    private int numberOfNeuronsInHiddenLayer;
    private double alpha;
    private int maxEpochs;
    private int chosenDataset;

    private Map<Integer, String> datasetPaths;

    private void prepareDatasetPaths() {
        datasetPaths = new HashMap<>();
        datasetPaths.put(1, DATASET_1_PATH);
        datasetPaths.put(2, DATASET_2_PATH);
    }

    private void prepareDataset() {
        Matrix Dataset = MatrixUtil.getInstance().readTestData(datasetPaths.get(chosenDataset));
        X = Dataset.getColumn(0).toColumnMatrix();
        Y = Dataset.getColumn(1).toColumnMatrix();
    }

    private void prepareTestSet() {
        Matrix Testset = MatrixUtil.getInstance().readTestData(TESTSET_PATH);
        XTest = Testset.getColumn(0).toColumnMatrix();
        YTest = Testset.getColumn(1).toColumnMatrix();
    }

    private void prepareParameters() {
        commandLineUtil.print(CHOOSE_DATASET);
        chosenDataset = commandLineUtil.getInt(1, 2);
        commandLineUtil.print(CHOOSE_NUMBER_OF_NEURONS_IN_HIDDEN_LAYER);
        numberOfNeuronsInHiddenLayer = commandLineUtil.getInt(MIN_NEURONS_IN_HIDDEN_LAYER, MAX_NEURONS_IN_HIDDEN_LAYER);
        commandLineUtil.print(CHOOSE_ALPHA);
        alpha = commandLineUtil.getDouble(MIN_ALPHA, MAX_ALPHA);
        commandLineUtil.print(CHOOSE_MAX_EPOCHS);
        maxEpochs = commandLineUtil.getInt(MIN_MAX_EPOCHS, MAX_MAX_EPOCHS);
    }

    private void trainNetwork() {
        RadialNeuralNetwork network = new RadialNeuralNetwork(X, Y, alpha, PositionTrainerType.random, numberOfNeuronsInHiddenLayer,
                1, maxEpochs, OutputFunctionType.linear);
        network.fit();
        ChartsUtil.getInstance().plotFunctionAndApproximation(X, Y, network, "./charts/approx.jpg");
        ChartsUtil.getInstance().plotErrorHistory(network.getErrorHistory(), "./charts/errors.jpg");
    }

    @Override
    public void run() {
        prepareParameters();
        prepareDatasetPaths();
        prepareDataset();
        prepareTestSet();
        trainNetwork();
    }
}
