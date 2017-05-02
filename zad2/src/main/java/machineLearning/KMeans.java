package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.ChartsUtil;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.List;

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

    public KMeans(int numberOfIterations, Matrix x, int k) {
        this.numberOfIterations = numberOfIterations;
        this.x = x;
        K = k;
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
        for (int i = 0; i < numberOfIterations; ++i) {
            System.out.println("Iteration: " + i);
            String filepath = "./plots/" + plotsFolder + "/state" + Integer.toString(i) + ".jpg";
//            ChartsUtil.getInstance().plotNetworkState(filepath, x, centroidPositions);
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
                    double currentValue = mean.get(k);
                    double numberOfOccurrences = mean.get(lastColumnIndex);
                    double newValue = currentValue / numberOfOccurrences;
                    centroidPositions.set(j, k, newValue);
                }
            }
            cleanMeans();
        }
    }

    protected int findClosestCentroid(Vector sample) {
        int bestMatchingUnitIndex = 0;
        double lowestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroidPositions.rows(); ++i) {
            Vector currentRow = centroidPositions.getRow(i);
            double distance = VectorUtil.getInstance().euclideanDistance(currentRow, sample);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                bestMatchingUnitIndex = i;
            }
        }
        return bestMatchingUnitIndex;
    }

    private void initializePositions() {
        centroidPositions = new Basic2DMatrix(K, x.columns());
        centroidPositions.each((int i, int j, double value) -> centroidPositions.set(i, j, Math.random() * 2 * EPSILON - EPSILON));
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
}
