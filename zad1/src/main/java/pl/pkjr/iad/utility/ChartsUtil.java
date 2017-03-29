package pl.pkjr.iad.utility;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import pl.pkjr.iad.machineLearning.NeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.jfree.chart.ChartFactory.createScatterPlot;
import static org.jfree.chart.ChartFactory.createXYLineChart;
import static org.jfree.chart.ChartUtilities.saveChartAsJPEG;

/**
 * Created by patry on 28/03/2017.
 */
public class ChartsUtil {
    public static void plotScatter(Matrix X, Vector y) {
        XYSeriesCollection points = getPoints(X, y);
        JFreeChart scatterPlot = createScatterPlot("Points", "x1", "x2", points);
        try {
            saveChartAsJPEG(new File("./plots/plot1.jpg"), scatterPlot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void plotLine(Matrix X, Vector y, Vector theta, int index) {
        X = X.removeFirstColumn();
        XYSeriesCollection points = getPoints(X, y);
        XYSeries predictionLine = new XYSeries("prediction");
        for (int i = 0; i < 1000; ++i) {
            double x1 = (i - 500.0) / 100.0;
            double x2 = -1 * (1 / theta.get(2)) * (theta.get(0) + theta.get(1) * x1);
            predictionLine.add(x1, x2);
        }
        points.addSeries(predictionLine);
        JFreeChart plot = createScatterPlot("Prediction", "x1", "x2", points);
        try {
            saveChartAsJPEG(new File("./plots/plotPrediction" + Integer.toString(index) + ".jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static XYSeriesCollection getPoints(Matrix X, Vector y) {
        XYSeries positive = new XYSeries("positve");
        XYSeries negative = new XYSeries("negative");
        int m = X.rows(), n = X.columns();
        for (int i = 0; i < m; ++i) {
            if (y.get(i) == 0) {
                positive.add(X.get(i, 0), X.get(i, 1));
            } else {
                negative.add(X.get(i, 0), X.get(i, 1));
            }
        }
        XYSeriesCollection points = new XYSeriesCollection();
        points.addSeries(positive);
        points.addSeries(negative);
        return points;
    }

    public static void plotAccuracy(List<Double> accuracy) {
        XYSeries series = new XYSeries("graph");
        for (int i = 0; i < accuracy.size(); ++i) {
            series.add(i, accuracy.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart plot = createXYLineChart("Accuracy", "Epochs", "Accuracy", dataset);
        try {
            saveChartAsJPEG(new File("./plots/accuracy.jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void plotError(List<Double> accuracy) {
        XYSeries series = new XYSeries("graph");
        for (int i = 0; i < accuracy.size(); ++i) {
            series.add(i, accuracy.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart plot = createXYLineChart("Error", "Epochs", "Error", dataset);
        try {
            saveChartAsJPEG(new File("./plots/error.jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void plotFunction(Matrix X, Matrix Y, String fileName) {
        if (X.columns() > 1 || Y.columns() > 1) {
            throw new IllegalArgumentException();
        }
        XYSeries points = new XYSeries("Initial function");
        int m = X.rows();
        for (int i = 0; i < m; ++i) {
            points.add(X.get(i, 0), Y.get(i, 0));
        }
        XYSeriesCollection pointsCollection = new XYSeriesCollection();
        pointsCollection.addSeries(points);
        JFreeChart plot = createScatterPlot("Initial function", "x", "y", pointsCollection);
        try {
            saveChartAsJPEG(new File("./plots/" + fileName + ".jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void plotFunctionAndApproximation(Matrix X, Matrix Y, NeuralNetwork network, String fileName) {
        if (X.columns() > 1 || Y.columns() > 1) {
            throw new IllegalArgumentException();
        }
        XYSeries points = new XYSeries("Initial function");
        int m = X.rows();
        for (int i = 0; i < m; ++i) {
            points.add(X.get(i, 0), Y.get(i, 0));
        }
        XYSeries approximation = new XYSeries("Approximation");
        double min = X.min(), max = X.max();
        for (double i = min; i < max; i += 0.01) {
            Matrix input = new Basic2DMatrix(1, 1);
            input.set(0, 0, i);
            approximation.add(i, network.predict(input).get(0, 0));
        }
        XYSeriesCollection pointsCollection = new XYSeriesCollection();
        pointsCollection.addSeries(points);
        pointsCollection.addSeries(approximation);
        JFreeChart plot = createScatterPlot("Initial function and approximation",
                "x", "y", pointsCollection);
        try {
            saveChartAsJPEG(new File("./plots/" + fileName + ".jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
