package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.ChartsUtil;
import util.FilesUtil;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;

/**
 * Created by patry on 01/05/17.
 */
public class KMeans {

    private static final String plotsFolder = "KMeans";
    private static final double EPSILON = 7.0;
    private static final double EPSILON_2 = 255.0;

    private int numberOfIterations;
    private Matrix x; //input data
    private Matrix centroidPositions;
    private int K; //number of centroids
    private List<Double> errorHistory;
    private Matrix means;
    private Random random;

    public KMeans(int numberOfIterations, Matrix x, int k) {
        this.numberOfIterations = numberOfIterations;
        this.x = x;
        K = k;
        random = new Random();
        initializePositions();
//        initializePositionsForImageCompression();
        errorHistory = new ArrayList<>();
        cleanMeans();
    }

    public KMeans(int numberOfIterations, Matrix x, int k, Matrix centroidPositions) {
        this.numberOfIterations = numberOfIterations;
        this.x = x;
        this.K = k;
        this.centroidPositions = centroidPositions;
        errorHistory = new ArrayList<>();
        cleanMeans();
    }

    public void fit() {
        int lastColumnIndex = means.columns() - 1;
        FilesUtil.getInstance().clearDirectory("./plots/KMeans");
        for (int i = 0; i < numberOfIterations; ++i) {
            System.out.println("Iteration: " + i);
            String filepath = "./plots/" + plotsFolder + "/state" + Integer.toString(i) + ".jpg";
            ChartsUtil.getInstance().plotNetworkState(filepath, x, centroidPositions);
            for (int j = 0; j < x.rows(); ++j) {
                Vector currentSample = x.getRow(j);
                int closestCentroidIndex = findClosestCentroid(currentSample);
                for (int k = 0; k < currentSample.length(); ++k) {
                    double currentValue = means.get(closestCentroidIndex, k);
                    means.set(closestCentroidIndex, k, currentValue + currentSample.get(k));
                }
                double currentCount = means.get(closestCentroidIndex, lastColumnIndex);
                means.set(closestCentroidIndex, lastColumnIndex, currentCount + 1);
            }
            for (int j = 0; j < centroidPositions.rows(); ++j) {
                Vector mean = means.getRow(j);
                for (int k = 0; k < mean.length() - 1; ++k) {
                    double currentValue = centroidPositions.get(j, k);
                    double currentMeanValue = mean.get(k);
                    double numberOfOccurrences = mean.get(lastColumnIndex);
                    double newValue = numberOfOccurrences == 0.0 ? currentValue : currentMeanValue / numberOfOccurrences;
                    centroidPositions.set(j, k, newValue);
                }
            }
            errorHistory.add(getError());
            FilesUtil.getInstance().saveMeans(means, "./plots/KMeans_means/means" + i + ".txt");
            cleanMeans();
        }
        FilesUtil.getInstance().saveErrorHistory(errorHistory, "./plots/KMeans/errorHistory.txt");
    }

    protected int findClosestCentroid(Vector sample) {
        int bestMatchingUnitIndex = 0;
        double lowestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroidPositions.rows(); ++i) {
            Vector currentRow = centroidPositions.getRow(i);
            double distance = VectorUtil.getInstance().euclideanDistance(currentRow, sample);
//            double distance = VectorUtil.getInstance().colorDistance(currentRow, sample);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                bestMatchingUnitIndex = i;
            }
        }
        return bestMatchingUnitIndex;
    }

    private double getError() {
        double sum = 0;
        for (int i = 0; i < x.rows(); ++i) {
            Vector currentRow = x.getRow(i);
            int closestCentroidIndex = findClosestCentroid(currentRow);
            Vector closestCentroid = centroidPositions.getRow(closestCentroidIndex);
            double distance = VectorUtil.getInstance().euclideanDistance(closestCentroid, currentRow);
            sum += distance*distance;
        }
        return sum / x.rows();
    }

    private void initializePositions() {
        centroidPositions = new Basic2DMatrix(K, x.columns());
        for (int i = 0; i < centroidPositions.rows(); ++i) {
            int randomPointIndex = random.nextInt(x.rows());
            for (int j = 0; j < centroidPositions.columns(); ++j) {
                double value = x.get(randomPointIndex, j);
                centroidPositions.set(i, j, value);
            }
        }
    }

    private void initializePositionsForImageCompression() {
        centroidPositions = new Basic2DMatrix(K, x.columns());
        centroidPositions.each((int i, int j, double value) -> centroidPositions.set(i, j, Math.random() * EPSILON_2));
    }

    private void cleanMeans() {
        means = Matrix.zero(K, x.columns() + 1);
    }

    public Matrix getCentroidPositions() {
        return centroidPositions;
    }

    public List<Double> getErrorHistory() {
        return errorHistory;
    }

    public int getK() {
        return K;
    }

    public double getLastError() {
        int lastIndex = errorHistory.size() - 1;
        return errorHistory.get(lastIndex);
    }
}
