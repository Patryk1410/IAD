import cli.ExerciseSelectionMenu;
import imageCompression.ImageCompressor;
import machineLearning.KMeans;
import machineLearning.KohonenNeuralNetwork;
import machineLearning.NeuralGas;
import machineLearning.SelfOrganizingNeuralNetwork;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import report.KMeansDataGenerator;
import report.NeuralNetworkDataGenerator;
import util.ChartsUtil;
import util.DatasetUtil;

/**
 * Created by patry on 27/04/17.
 */
public class App {

    public static void main(String[] args) {
        ExerciseSelectionMenu menu = new ExerciseSelectionMenu();
        menu.run();
    }

    private static void runNeuralNetwork() {
        String plotPath = "./plots/dataset.jpg",
                datasetPath = "./datasets/dataset.txt";
        Matrix dataset = DatasetUtil.getInstance().readTestData(datasetPath);
//        NeuralNetworkDataGenerator.getInstance().generate(dataset);
//        ChartsUtil.getInstance().plotDataset(plotPath, dataset);
//        SelfOrganizingNeuralNetwork network = new KohonenNeuralNetwork(5, dataset, 250, NeighborhoodFunctionType.GAUSSIAN,
//                0.1, 0.25,0.99);
        SelfOrganizingNeuralNetwork network = new NeuralGas(5, dataset, 250,
                0.4, 0.25,0.8);
        network.fit();
    }

    private static void runKMeans() {
        String datasetPath = "./datasets/dataset.txt";
        Matrix dataset = DatasetUtil.getInstance().readTestData(datasetPath);
//        KMeansDataGenerator.getInstance().generate(dataset);
        KMeans kMeans = new KMeans(10, dataset, 250);
        kMeans.fit();
    }

    private static void runCompression() {
        String image = "bird";
        ImageCompressor imageCompressor = new ImageCompressor(image, 256);
        imageCompressor.compress();
    }
}
