package util;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by patry on 30/04/17.
 */
public class DatasetUtil {

    private static DatasetUtil instance = new DatasetUtil();

    public static DatasetUtil getInstance() {
        return instance;
    }

    private DatasetUtil() {}

    public Matrix readTestData(String filePath) {
        int counter = 0;
        try(FileReader fr = new FileReader(filePath);
            FileReader fr2 = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            BufferedReader br2 = new BufferedReader(fr2)) {
            int N = (int) br2.lines().count();
            double[][] dataset = new double[N][];
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(",");
                dataset[counter] = new double[numbers.length];
                for (int i = 0; i < numbers.length; ++i) {
                    dataset[counter][i] = Double.parseDouble(numbers[i]);
                }
                counter++;
            }
            return new Basic2DMatrix(dataset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
