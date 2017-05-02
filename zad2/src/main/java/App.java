import imageCompression.ImageCompressor;
import machineLearning.KMeans;
import machineLearning.NeuralGas;
import machineLearning.SelfOrganizingNeuralNetwork;
import org.la4j.Matrix;
import util.ChartsUtil;
import util.DatasetUtil;

/**
 * Created by patry on 27/04/17.
 */
public class App {

    public static void main(String[] args) {
        runCompression();
    }

    private static void runNeuralNetwork() {
        String plotPath = "./plots/dataset.jpg",
                datasetPath = "./datasets/dataset.txt";
        Matrix dataset = DatasetUtil.getInstance().readTestData(datasetPath);
        ChartsUtil.getInstance().plotDataset(plotPath, dataset);
        SelfOrganizingNeuralNetwork network = new NeuralGas(5, dataset, 250,
                0.1, 0.25);
        network.fit();
    }

    private static void runKMeans() {
        String datasetPath = "./datasets/dataset.txt";
        Matrix dataset = DatasetUtil.getInstance().readTestData(datasetPath);
        KMeans kMeans = new KMeans(10, dataset, 250);
        kMeans.fit();
    }

    private static void runCompression() {
        String image = "test";
        ImageCompressor imageCompressor = new ImageCompressor(image, 1024);
        imageCompressor.compress();
    }
}
