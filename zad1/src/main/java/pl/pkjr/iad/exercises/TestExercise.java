package pl.pkjr.iad.exercises;

import org.la4j.Matrix;
import pl.pkjr.iad.App;
import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.data.DatasetsManager;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetwork;
import pl.pkjr.iad.machineLearning.neuralNetworks.NeuralNetworkWithBias;
import pl.pkjr.iad.utility.ChartsUtil;
import pl.pkjr.iad.utility.MatrixUtil;

import static pl.pkjr.iad.machineLearning.costFunction.CostFunctionType.logarithmic;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;

/**
 * Created by patry on 28/03/2017.
 */
public class TestExercise implements Exercise {

    private final String kSelectDataset = "Select dataset: \n";
    private final String kSelectAlpha = "Select alpha: ";
    private final String kSelectNumberOfEpochs = "Select number of epochs: ";

    private int selectedDataset = 1;
    private int numberOfEpochs;
    private double alpha;

    private Matrix X;
    private Matrix Y;

    private void pickDataset() {
        ConsoleController.print(kSelectDataset);
        ConsoleController.print(DatasetsManager.getInstance().getTestDatasetsNames());
        selectedDataset = ConsoleController.getInt(1, 3);
    }

    private void prepareMatrices() {
        Matrix Dataset = DatasetsManager.getInstance().getDataset(selectedDataset);
        X = MatrixUtil.getX(Dataset);
        Y = MatrixUtil.getY(Dataset);
    }

    private void plotScatter() {
        ChartsUtil.plotScatter(X, Y.getColumn(0));
    }

    private void prepareLearningParameters() {
        ConsoleController.print(kSelectAlpha);
        alpha = ConsoleController.getDouble(0, 1);
        ConsoleController.print(kSelectNumberOfEpochs);
        numberOfEpochs = ConsoleController.getInt(1, 1000000);
    }

    private void trainNetwork() {
        int[] neurons = {2, 2, 2, 1};
        NeuralNetwork network = new NeuralNetworkWithBias(X, Y, 2, neurons, alpha, 0, 0.5,
                numberOfEpochs, 0.5, logarithmic, sigmoid, null, null);
        network.fit();
//        ChartsUtil.plotAccuracy(network.getAccuracyHistory());
    }

    @Override
    public void run() {
        pickDataset();
        prepareMatrices();
        plotScatter();
        prepareLearningParameters();
        trainNetwork();
        App.setShouldContinue(false);
    }
}
