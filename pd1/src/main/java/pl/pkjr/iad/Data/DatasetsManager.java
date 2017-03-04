package pl.pkjr.iad.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

    public void getDataset() {
        
    }
}
