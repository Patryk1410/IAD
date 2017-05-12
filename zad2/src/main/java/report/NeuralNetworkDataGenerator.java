package report;

import machineLearning.KohonenNeuralNetwork;
import machineLearning.NeuralGas;
import machineLearning.SelfOrganizingNeuralNetwork;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import util.ReportUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by patry on 03/05/17.
 */
public class NeuralNetworkDataGenerator {

    private static NeuralNetworkDataGenerator instance = new NeuralNetworkDataGenerator();

    public static NeuralNetworkDataGenerator getInstance() {
        return instance;
    }

    private NeuralNetworkDataGenerator(){}

    public void generate(Matrix dataset) {
        int numberOfIterations = ReportUtil.NUMBER_OF_EPOCHS;
        for (int i = 0; i < ReportUtil.K.length; ++i) {
            for (int j = 0; j < ReportUtil.ALPHA.length; ++j) {
                for (int l = 0; l < ReportUtil.P.length; ++l) {
                    for (int m = 0; m < ReportUtil.LAMBDA.length; ++m) {
                        System.out.println("Number of neurons: " + i);
                        System.out.println("alpha: " + j);
                        System.out.println("potential: " + l);
                        System.out.println("lambda: " + m);
                        SelfOrganizingNeuralNetwork network = new NeuralGas(numberOfIterations, dataset,
                                ReportUtil.K[i], ReportUtil.LAMBDA[m], ReportUtil.ALPHA[j], ReportUtil.P[l]);
                        network.fit();
                        saveToFile(network);
                    }
                }
            }
        }
    }

    private void saveToFile(SelfOrganizingNeuralNetwork neuralNetwork) {
        try(FileWriter fw = new FileWriter("./res/ex1_ng.txt", true);
                BufferedWriter bw = new BufferedWriter(fw)) {
            List<Double> errors = neuralNetwork.getErrorHistory();
            String line = Integer.toString(neuralNetwork.getNumberOfNeurons()) + "&"
                    + Double.toString(neuralNetwork.getAlpha()) + "&"
                    + Double.toString(neuralNetwork.getAdaptationPotential()) + "&"
                    + Double.toString(neuralNetwork.getLambda()) + "&" + Double.toString(neuralNetwork.getLastError())
                    + "\\\\" + "\n";
            bw.write(line);
            bw.write("\\hline\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
