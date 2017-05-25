package util;

import machineLearning.RadialNeuralNetwork;
import org.apache.commons.io.FileUtils;
import org.la4j.Matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by patry on 25/05/17.
 */
public class FilesUtil {

    private static FilesUtil instance = new FilesUtil();

    public static FilesUtil getInstance() {
        return instance;
    }

    private FilesUtil() {}

    public void clearDirectory(String directoryPath) {
        try {
            FileUtils.cleanDirectory(new File(directoryPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRadialNeuronsPositions(RadialNeuralNetwork network, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            Matrix positions = network.getC(), radiuses = network.getR();
            for (int i = 0; i < positions.rows(); i++) {
                String line = i + ": position: " + positions.getRow(i).toString() + " radius: "
                        + radiuses.getRow(i).toString() + '\n';
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
