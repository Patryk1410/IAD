package pl.pkjr.iad;

import pl.pkjr.iad.Data.DatasetsManager;
import pl.pkjr.iad.Interface.ConsoleController;

/**
 * Hello world!
 *
 */
public class App 
{
    private final String kSelectDataset = "Select dataset: \n";

    private boolean shouldContinue = false;
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

    }
}
