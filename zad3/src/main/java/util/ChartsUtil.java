package util;

import machineLearning.RadialNeuralNetwork;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.jfree.chart.ChartFactory.createScatterPlot;
import static org.jfree.chart.ChartFactory.createXYLineChart;
import static org.jfree.chart.ChartUtilities.saveChartAsJPEG;

/**
 * Created by patry on 24/05/17.
 */
public class ChartsUtil {

    private static ChartsUtil instance = new ChartsUtil();

    public static ChartsUtil getInstance() {
        return instance;
    }

    private ChartsUtil() {}

    public void plotErrorHistory(List<Double> errors, String fileName) {
        XYSeries errorSeries = new XYSeries("training set error");
        for (int i = 0; i < errors.size(); ++i) {
            errorSeries.add(i, errors.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);
        JFreeChart plot = createXYLineChart("Error", "Epochs", "Error", dataset);
        try {
            saveChartAsJPEG(new File(fileName), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plotFunctionAndApproximation(Matrix X, Matrix Y, RadialNeuralNetwork network, String fileName) {
        if (X.columns() > 1 || Y.columns() > 1) {
            throw new IllegalArgumentException();
        }
        XYSeries points = new XYSeries("Training data");
        int m = X.rows();
        for (int i = 0; i < m; ++i) {
            points.add(X.get(i, 0), Y.get(i, 0));
        }
        XYSeries approximation = new XYSeries("Approximation");
        double min = X.min(), max = X.max();
        for (double i = min; i < max; i += 0.01) {
            Vector input = new BasicVector(1);
            input.set(0, i);
            approximation.add(i, network.predict(input).get(0, 0));
        }
        XYSeriesCollection pointsCollection = new XYSeriesCollection();
        pointsCollection.addSeries(points);
        pointsCollection.addSeries(approximation);
        JFreeChart plot = createScatterPlot("Initial function and approximation",
                "x", "y", pointsCollection);
        try {
            saveChartAsJPEG(new File(fileName), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
