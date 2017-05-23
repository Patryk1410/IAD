package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.MachineLearningUtil;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by patry on 20/05/17.
 */
public class KMeans {

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
        for (int i = 0; i < numberOfIterations; ++i) {
            System.out.println("Iteration: " + i);
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
//            errorHistory.add(getError());
            cleanMeans();
        }
    }

    private int findClosestCentroid(Vector sample) {
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

    private double getError() {
        double sum = 0;
        for (int i = 0; i < x.rows(); ++i) {
            Vector currentRow = x.getRow(i);
            int closestCentroidIndex = findClosestCentroid(currentRow);
            Vector closestCentroid = centroidPositions.getRow(closestCentroidIndex);
            double distance = VectorUtil.getInstance().euclideanDistance(closestCentroid, currentRow);
            sum += distance * distance;
        }
        return sum / x.rows();
    }

    private void initializePositions() {
        centroidPositions = MachineLearningUtil.getInstance().setRandomPositions(x, K);
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
