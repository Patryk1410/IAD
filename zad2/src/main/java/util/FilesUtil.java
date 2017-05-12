package util;

import org.apache.commons.io.FileUtils;
import org.la4j.Matrix;
import org.la4j.Vector;

import java.io.*;
import java.util.List;

/**
 * Created by patry on 10/05/17.
 */
public class FilesUtil {

    private static FilesUtil instance = new FilesUtil();

    private FilesUtil(){}

    public static FilesUtil getInstance() {
        return instance;
    }

    public void clearDirectory(String directoryPath) {
        try {
            FileUtils.cleanDirectory(new File(directoryPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveErrorHistory(List<Double> errorHistory, String path) {
        try (FileWriter fileWriter = new FileWriter(path); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            for (Double error : errorHistory) {
                String line = Double.toString(error) + "\n\r";
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMeans(Matrix means, String path) {
        try (FileWriter fileWriter = new FileWriter(path); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            for (int i = 0; i < means.rows(); ++i) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < means.columns(); ++j) {
                    line.append(means.get(i, j)).append(" ");
                }
                line.append('\n');
                bufferedWriter.write(line.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
