package report;

import machineLearning.KMeans;
import machineLearning.NeuralGas;
import machineLearning.SelfOrganizingNeuralNetwork;
import org.la4j.Matrix;
import util.ReportUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by patry on 03/05/17.
 */
public class KMeansDataGenerator {
    private static KMeansDataGenerator instance = new KMeansDataGenerator();

    public static KMeansDataGenerator getInstance() {
        return instance;
    }

    private KMeansDataGenerator(){}

    public void generate(Matrix dataset) {
        int numberOfIterations = ReportUtil.K_MEANS_NUMBER_OF_EPOCHS;
        for (int i = 0; i < ReportUtil.K_MEANS_K.length; ++i) {
            System.out.println("Number of neurons: " + i);
            KMeans network = new KMeans(numberOfIterations, dataset, ReportUtil.K_MEANS_K[i]);
            network.fit();
            saveToFile(network);
        }
    }

    private void saveToFile(KMeans kMeans) {
        try(FileWriter fw = new FileWriter("./res/ex1_kmeans.txt", true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            List<Double> errors = kMeans.getErrorHistory();
            String line = Integer.toString(kMeans.getK()) + "&" + Double.toString(kMeans.getLastError())
                    + "\\\\" + "\n";
            bw.write(line);
            bw.write("\\hline\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
