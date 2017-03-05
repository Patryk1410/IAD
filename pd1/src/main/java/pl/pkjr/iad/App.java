package pl.pkjr.iad;

import org.la4j.Matrix;
import org.la4j.Vector;
import pl.pkjr.iad.data.DatasetsManager;
import pl.pkjr.iad.Interface.ConsoleController;
import pl.pkjr.iad.Utility.ChartsUtility;
import pl.pkjr.iad.Utility.MatrixUtility;
import pl.pkjr.iad.machineLearning.LinearNeuron;

/**
 * Hello world!
 *
 */
public class App 
{
    private final String kSelectDataset = "Select dataset: \n";
    private final String kSelectAlpha = "Select alpha: ";
    private final String kSelectNumberOfEpochs = "Select number of epochs: ";

    private int selectedDataset = 1;
    private int inputNumber;

    public static void main( String[] args )
    {
        App app = new App();
        app.run();
    }

    public void run() {
        ConsoleController.print(kSelectDataset);
        ConsoleController.print(DatasetsManager.getInstance().getDatasetsNames());
        inputNumber = ConsoleController.getInt();
        selectedDataset = inputNumber >= 1 && inputNumber <= 3 ? inputNumber : selectedDataset;
        Matrix Dataset = DatasetsManager.getInstance().getDataset(selectedDataset);
        Matrix X = MatrixUtility.getX(Dataset);
        Vector y = MatrixUtility.getY(Dataset);
        ChartsUtility.plotScatter(X, y);
        LinearNeuron neuron = new LinearNeuron(y, X, 0.01, 100);
        neuron.fit();
    }
}
