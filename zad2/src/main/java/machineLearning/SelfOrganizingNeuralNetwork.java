package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.ChartsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import util.FilesUtil;

/**
 * Created by patry on 01/05/17.
 */
public abstract class SelfOrganizingNeuralNetwork {

    protected static final double EPSILON = 7.0;
    protected static final double EPSILON_PRIME = 2.0;

    protected int numberOfIterations;
    protected Matrix x; //input data
    protected Matrix theta; //weights
    protected int numberOfNeurons;
    protected int[] bestMatchingUnits;
    protected double lambda; //neighborhood radius
    protected double alpha; //learning rate
    protected Vector potentials;
    protected List<Double> errorHistory;
    protected String plotsFolder;
    protected double adaptationPotential;
    protected int iterationNumber;

    protected SelfOrganizingNeuralNetwork(int numberOfIterations, Matrix x, int numberOfNeurons, double lambda,
                                          double alpha, String plotsFolder, double adaptationPotential) {
        this.numberOfIterations = numberOfIterations;
        this.x = x;
        this.numberOfNeurons = numberOfNeurons;
        this.lambda = lambda;
        this.alpha = alpha;
        this.plotsFolder = plotsFolder;
        this.adaptationPotential = adaptationPotential;
        this.iterationNumber = 0;
        initializeTheta();
        bestMatchingUnits = new int[x.rows()];
        potentials = Vector.constant(theta.rows(), adaptationPotential);
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

    private void initializeTheta() {
        theta = new Basic2DMatrix(numberOfNeurons, x.columns());
        theta.each((int i, int j, double value) -> theta.set(i, j, Math.random() * 2 * EPSILON - EPSILON +
                (j == 0 ? EPSILON_PRIME : (-1) * EPSILON_PRIME)));
    }

    public void fit() {
        iterationNumber = 0;
        FilesUtil.getInstance().clearDirectory("./plots/" + plotsFolder);
        for (; iterationNumber < numberOfIterations; ++iterationNumber) {
            System.out.println("Iteration: " + iterationNumber);
            for (int j = 0; j < x.rows(); ++j) {
                if (j % (x.rows()/50) == 0) {
                    System.out.println("Sample: " + j);
                    String filepath = "./plots/" + plotsFolder + "/state" + Integer.toString(j / (x.rows() / 50) + iterationNumber * 50) + ".jpg";
                    ChartsUtil.getInstance().plotNetworkState(filepath, x, theta);
                }
                Vector currentSample = x.getRow(j);
                int bestMatchingUnitIndex = findBestMatchingUnit(currentSample);
                bestMatchingUnits[j] = bestMatchingUnitIndex;
                update(currentSample, bestMatchingUnitIndex, iterationNumber);
            }
            double error = getQuantizationError();
            System.out.println("Error: " + error);
            errorHistory.add(error);
            adaptationPotential *=  0.9;
        }
        FilesUtil.getInstance().saveErrorHistory(errorHistory, "./plots/" + plotsFolder + "/errorHistory.txt");
    }

    protected abstract int findBestMatchingUnit(Vector sample);

    protected void updatePotentials(int bestMatchingUnitIndex) {
        potentials.each((int i, double value) -> {
            double newValue = i != bestMatchingUnitIndex ? value + (1.0 / numberOfNeurons) : value - adaptationPotential;
            potentials.set(i, newValue);
        });
    }

    protected abstract void update(Vector sample, int bestMatchingUnitIndex, int iterationNumber);

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

    protected double getLearningRateParameter(int iterationNumber) {
        return ((numberOfIterations - iterationNumber) * 1.0 / numberOfIterations) * alpha;
    }

    public Matrix getTheta() {
        return theta;
    }

    public List<Double> getErrorHistory() {
        return errorHistory;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public double getLambda() {
        return lambda;
    }

    public double getAlpha() {
        return alpha;
    }

    public Vector getPotentials() {
        return potentials;
    }

    public double getAdaptationPotential() {
        return adaptationPotential;
    }

    public double getLastError() {
        int lastIndex = errorHistory.size() - 1;
        return errorHistory.get(lastIndex);
    }
}
