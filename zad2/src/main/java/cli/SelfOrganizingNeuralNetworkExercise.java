package cli;

import machineLearning.KohonenNeuralNetwork;
import machineLearning.NeuralGas;
import machineLearning.SelfOrganizingNeuralNetwork;
import machineLearning.neighborhoodFunction.NeighborhoodFunction;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionSelector;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import util.CommandLineUtil;
import util.DatasetUtil;

/**
 * Created by patry on 10/05/17.
 */
public class SelfOrganizingNeuralNetworkExercise implements InterfaceModule {

    private static final String SELECT_NUMBER_OF_ITERATIONS = "Select number of iterations:";
    private static final String SELECT_NUMBER_OF_NEURONS = "Select number of neurons";
    private static final String SELECT_ALPHA = "Select learning rate";
    private static final String SELECT_LAMBDA = "Select neighborhood radius";
    private static final String SELECT_ADAPTATION_POTENTIAL = "Select adaptation potential";
    private static final String SELECT_NETWORK_TYPE = "Select network type\n";
    private static final String KOHONEN_NETWORK_TYPE = "1. Kohonen neural network";
    private static final String NEURAL_GAS_NETWORK_TYPE = "2. Neural gas";

    private int numberOfIterations;
    private Matrix x;
    private int nummberOfNeurons;
    private NeighborhoodFunctionType neighborhoodFunction;
    private double alpha;
    private double lambda;
    private double adaptationPotential;
    private int type;

    @Override
    public void run() {
        initParams();
        runNetwork();
    }

    private void runNetwork() {
        SelfOrganizingNeuralNetwork network = type == 1
                ? new KohonenNeuralNetwork(numberOfIterations, x, nummberOfNeurons, neighborhoodFunction, lambda, alpha,
                        adaptationPotential)
                : new NeuralGas(numberOfIterations, x, nummberOfNeurons, lambda, alpha, adaptationPotential);
        network.fit();
    }

    private void initParams() {
        commandLineUtil.print(SELECT_NETWORK_TYPE);
        commandLineUtil.print(KOHONEN_NETWORK_TYPE);
        commandLineUtil.print(NEURAL_GAS_NETWORK_TYPE);
        type = commandLineUtil.getInt(1, 2);
        commandLineUtil.print(SELECT_NUMBER_OF_ITERATIONS);
        numberOfIterations = commandLineUtil.getInt(1, 1000);
        commandLineUtil.print(SELECT_NUMBER_OF_NEURONS);
        nummberOfNeurons = commandLineUtil.getInt(1, 10000);
        commandLineUtil.print(SELECT_ALPHA);
        alpha = commandLineUtil.getDouble(0.0, 1.0);
        commandLineUtil.print(SELECT_LAMBDA);
        lambda = commandLineUtil.getDouble(0.0, 1.0);
        commandLineUtil.print(SELECT_ADAPTATION_POTENTIAL);
        adaptationPotential = commandLineUtil.getDouble(0.0, 1.0);
        String datasetPath = "./datasets/dataset.txt";
        x = DatasetUtil.getInstance().readTestData(datasetPath);
        neighborhoodFunction = NeighborhoodFunctionType.GAUSSIAN;
    }
}
