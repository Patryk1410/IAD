package machineLearning;

import machineLearning.neighborhoodFunction.NeighborhoodFunction;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionSelector;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.ChartsUtil;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patry on 29/04/17.
 */
public class KohonenNeuralNetwork {

    private static final double EPSILON = 7.0;
    private static final double ADAPTATION_POTENTIAL = 0.75;

    private int numberOfIterations;
    private Matrix x; //input data
    private Matrix theta; //weights
    private int numberOfNeurons;
    private int[] bestMatchingUnits;
    private NeighborhoodFunction neighborhoodFunction;
    private double lambda; //neighborhood radius
    private double alpha; //learning rate
    private Vector potentials;
    private List<Double> errorHistory;

    public KohonenNeuralNetwork(int numberOfIterations, Matrix x, int numberOfNeurons,
                                NeighborhoodFunctionType neighborhoodFunction, double lambda, double alpha) {
        this.numberOfIterations = numberOfIterations;
        this.x = x;
        this.neighborhoodFunction = NeighborhoodFunctionSelector.getInstance().getFunction(neighborhoodFunction);
        this.lambda = lambda;
        this.alpha = alpha;
//        normalizeInput();
        this.numberOfNeurons = numberOfNeurons;
        initializeTheta();
        bestMatchingUnits = new int[x.rows()];
        potentials = Vector.constant(theta.rows(), ADAPTATION_POTENTIAL);
        errorHistory = new ArrayList<>();
    }

    private void normalizeInput() {
        for (int i = 0; i < x.rows(); ++i) {
            Vector currentSample = x.getRow(i);
            double norm = currentSample.norm();
            for (int j = 0; j < x.columns(); ++j) {
                x.set(i, j, x.get(i, j) / norm);
            }
        }
    }

    public void fit() {
        for (int i = 0; i < numberOfIterations; ++i) {
            System.out.println("Iteration: " + i);
//            ChartsUtil.getInstance().plotNetworkState("./plots/state" + Integer.toString(i) + ".jpg", x, theta);
            for (int j = 0; j < x.rows(); ++j) {
                if (j % (x.rows()/50) == 0) {
                    System.out.println("Sample: " + j);
                    ChartsUtil.getInstance().plotNetworkState("./plots/state" + Integer.toString(j / (x.rows() / 50) + i * 50) + ".jpg", x, theta);
                }
                Vector currentSample = x.getRow(j);
                int bestMatchingUnitIndex = findBestMatchingUnit(currentSample);
                bestMatchingUnits[j] = bestMatchingUnitIndex;
                update(currentSample, bestMatchingUnitIndex, i);
//                if (j == 99) break;
            }
            double error = getQuantizationError();
            System.out.println("Error: " + error);
            errorHistory.add(error);
        }
    }

    private int findBestMatchingUnit(Vector sample) {
        int bestMatchingUnitIndex = 0;
        double lowestDistance = Double.MAX_VALUE;
        int count = 0;
        for (int i = 0; i < theta.rows(); ++i) {
            if (potentials.get(i) < ADAPTATION_POTENTIAL) {
                continue;
            }
            ++count;
            Vector currentRow = theta.getRow(i);
            double distance = VectorUtil.getInstance().euclideanDistance(currentRow, sample);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                bestMatchingUnitIndex = i;
            }
        }
//        System.out.println("Processed " + count + " units");
        updatePotentials(bestMatchingUnitIndex);
        return bestMatchingUnitIndex;
    }

    private void updatePotentials(int bestMatchingUnitIndex) {
        potentials.each((int i, double value) -> {
            double newValue = i != bestMatchingUnitIndex ? value + (1.0 / numberOfNeurons) : value - ADAPTATION_POTENTIAL;
            potentials.set(i, newValue);
        });
    }

    private void update(Vector sample, int bestMatchingUnitIndex, int iterationNumber) {
        theta.each((int i, int j, double value) -> {
            double currentValue = theta.get(i, j);
            Vector bestMatchingUnit = theta.getRow(bestMatchingUnitIndex);
            double newValue = currentValue + (neighborhoodFunction.compute(bestMatchingUnit, theta.getRow(i),
                    iterationNumber, numberOfIterations, lambda) * getLearningRateParameter(iterationNumber)
                    * (sample.get(j) - currentValue));
            theta.set(i, j, newValue);
        });
    }

    private double getLearningRateParameter(int iterationNumber) {
        return ((numberOfIterations - iterationNumber) * 1.0 / numberOfIterations) * alpha;
    }

    private double getQuantizationError() {
        double sum = 0;
        for (int i = 0; i < x.rows(); ++i) {
            int bestMatchingUnitIndex = bestMatchingUnits[i];
            Vector bestMatchingUnit = theta.getRow(bestMatchingUnitIndex);
            Vector currentSample = x.getRow(i);
            double norm = currentSample.subtract(bestMatchingUnit).norm();
            sum += norm * norm;
        }
        return sum / x.rows();
    }

    private void initializeTheta() {
        theta = new Basic2DMatrix(numberOfNeurons, x.columns());
        theta.each((int i, int j, double value) -> theta.set(i, j, Math.random() * 2 * EPSILON - EPSILON));
    }

    public Matrix getTheta() {
        return theta;
    }
}
