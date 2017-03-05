package pl.pkjr.iad.machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;
import pl.pkjr.iad.Utility.ChartsUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patry on 04/03/2017.
 */
public class LinearNeuron {

    private Vector theta;
    private Vector y;
    private Vector predictions;
    private List<Double> epochErrors;
    private Matrix X;
    private int n;
    private int m;
    private double alpha;
    private int maxEpochs;

    public LinearNeuron(Vector y, Matrix X, double alpha, int maxEpochs) {
        this.y = y;
        this.X = X;
        this.alpha = alpha;
        this.n = X.columns();
        this.m = X.rows();
        this.maxEpochs = maxEpochs;
        this.randomlyInitTheta();
        this.addOnesToX();
        this.predictions = new BasicVector(m);
        this.epochErrors = new ArrayList<>();
    }

    private void randomlyInitTheta() {
        theta = new BasicVector(n + 1);
        for (int i = 0; i < theta.length(); ++i) {
            theta.set(i, Math.random() * 2 - 1);
        }
    }

    private void addOnesToX() {
        Vector ones = BasicVector.unit(m);
        X = X.insertColumn(0, ones);
    }

    public void fit() {
        ChartsUtility.plotLine(X, y, theta, 1);
        for (int i = 0; i < maxEpochs; ++i) {
            predict();
            int numberOfErrors = 0;
            for (int j = 0; j < m; ++j) {
                if (predictions.get(j) == 1 && y.get(j) == 0) {
                    theta = theta.subtract(X.getRow(j).multiply(alpha));
                    numberOfErrors++;
                } else if (predictions.get(j) == 0 && y.get(j) == 1) {
                    theta = theta.add(X.getRow(j).multiply(alpha));
                    numberOfErrors++;
                }
            }
            epochErrors.add(1.0 * numberOfErrors / m);
            if (numberOfErrors == 0) break;
        }
        ChartsUtility.plotLine(X, y, theta, 2);
        ChartsUtility.plotEpochErrors(epochErrors);
    }

    public void predict() {
        Vector res = formula();
        for (int i = 0; i < m; ++i) {
            predictions.set(i, (res.get(i) >= 0 ? 1 : 0));
        }
    }

    private Vector formula() {
        return theta.multiply(X.transpose());
    }
}
