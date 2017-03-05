package pl.pkjr.iad.data;

import org.la4j.Matrix;
import pl.pkjr.iad.Utility.MatrixUtility;

import java.io.File;

/**
 * Created by patry on 04/03/2017.
 */
public class DatasetsManager {
    private static DatasetsManager instance = null;

    private DatasetsManager() {}

    public static DatasetsManager getInstance() {
        if (instance == null) {
            instance = new DatasetsManager();
        }
        return instance;
    }

    public String getDatasetsNames() {
        String datasetsNames = "";
        File[] datasetsDirectory = new File("./datasets").listFiles();
        for (File file : datasetsDirectory) {
            datasetsNames += file.getName() + "\n";
        }
        return datasetsNames;
    }

    public Matrix getDataset(int datasetNumber) {
        String fileName = "./datasets/testData" + Integer.toString(datasetNumber) + ".txt";
        return MatrixUtility.readFromFile(fileName);
    }
}
