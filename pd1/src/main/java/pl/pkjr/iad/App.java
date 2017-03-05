package pl.pkjr.iad;

import org.la4j.Matrix;
import org.la4j.Vector;
import pl.pkjr.iad.data.DatasetsManager;
import pl.pkjr.iad.Interface.ConsoleController;
import pl.pkjr.iad.Utility.ChartsUtility;
import pl.pkjr.iad.Utility.MatrixUtility;
import pl.pkjr.iad.machineLearning.LinearNeuron;

public class App 
{
    private final String kSelectDataset = "Select dataset: \n";
    private final String kSelectAlpha = "Select alpha: ";
    private final String kSelectNumberOfEpochs = "Select number of epochs: ";

    private int selectedDataset = 1;
    private int numberOfEpochs;
    private double alpha;

    private Matrix X;
    private Vector y;

    public static void main( String[] args )
    {
        App app = new App();
        app.run();
    }

    private void run() {
        pickDataset();
        prepareMatrices();
        plotScatter();
        prepareLearningParameters();
        trainNeuron();
    }

    private void pickDataset() {
        ConsoleController.print(kSelectDataset);
        ConsoleController.print(DatasetsManager.getInstance().getDatasetsNames());
        selectedDataset = ConsoleController.getInt(1, 3);
    }

    private void prepareMatrices() {
        Matrix Dataset = DatasetsManager.getInstance().getDataset(selectedDataset);
        X = MatrixUtility.getX(Dataset);
        y = MatrixUtility.getY(Dataset);
    }

    private void plotScatter() {
        ChartsUtility.plotScatter(X, y);
    }

    private void prepareLearningParameters() {
        ConsoleController.print(kSelectAlpha);
        alpha = ConsoleController.getDouble(0, 1);
        ConsoleController.print(kSelectNumberOfEpochs);
        numberOfEpochs = ConsoleController.getInt(1, 1000);
    }

    private void trainNeuron() {
        LinearNeuron neuron = new LinearNeuron(y, X, alpha, numberOfEpochs);
        neuron.fit();
    }
}
