import machineLearning.KohonenNeuralNetwork;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import util.ChartsUtil;
import util.DatasetUtil;

import static machineLearning.neighborhoodFunction.NeighborhoodFunctionType.GAUSSIAN;
import static machineLearning.neighborhoodFunction.NeighborhoodFunctionType.RECTANGULAR;

/**
 * Created by patry on 27/04/17.
 */
public class App {

    public static void main(String[] args) {
        String plotPath = "./plots/dataset.jpg",
            datasetPath = "./datasets/dataset.txt";
        Matrix dataset = DatasetUtil.getInstance().readTestData(datasetPath);
        ChartsUtil.getInstance().plotDataset(plotPath, dataset);
        KohonenNeuralNetwork network = new KohonenNeuralNetwork(5, dataset, 250, GAUSSIAN,
                0.1, 0.5);
        ChartsUtil.getInstance().plotNetworkState("./plots/solution.jpg", dataset, network.getTheta());
        network.fit();
        ChartsUtil.getInstance().plotNetworkState("./plots/fitted.jpg", dataset, network.getTheta());
    }
}
