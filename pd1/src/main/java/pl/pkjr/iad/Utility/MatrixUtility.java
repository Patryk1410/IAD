package pl.pkjr.iad.Utility;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by patry on 04/03/2017.
 */
public class MatrixUtility {

    private static final int N = 200;

    public static Matrix readFromFile(String filePath) {
        double[][] dataset = new double[N][];
        int counter = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
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

    public static Matrix getX(Matrix Dataset) {
        return Dataset.removeLastColumn();
    }

    public static Vector getY(Matrix Dataset) {
        return Dataset.getColumn(2);
    }
}
