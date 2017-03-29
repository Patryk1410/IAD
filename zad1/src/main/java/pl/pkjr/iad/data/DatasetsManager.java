package pl.pkjr.iad.data;

import org.la4j.Matrix;
import pl.pkjr.iad.utility.MatrixUtil;

import java.io.File;

/**
 * Created by patry on 28/03/2017.
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

    public String getTestDatasetsNames() {
        StringBuilder datasetsNames = new StringBuilder();
        File[] datasetsDirectory = new File("./datasets/test").listFiles();
        for (File file : datasetsDirectory != null ? datasetsDirectory : new File[0]) {
            datasetsNames.append(file.getName()).append("\n");
        }
        return datasetsNames.toString();
    }

    public Matrix getDataset(int datasetNumber) {
        String fileName = "./datasets/test/testData" + Integer.toString(datasetNumber) + ".txt";
        return MatrixUtil.readTestData(fileName);
    }
}
