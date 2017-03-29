package pl.pkjr.iad.utility;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.la4j.Matrix;
import org.la4j.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by patry on 28/03/2017.
 */
public class ChartsUtil {
    public static void plotScatter(Matrix X, Vector y) {
        XYSeriesCollection points = getPoints(X, y);
        JFreeChart scatterPlot = ChartFactory.createScatterPlot("Points", "x1", "x2", points);
        try {
            ChartUtilities.saveChartAsJPEG(new File("./plots/plot1.jpg"), scatterPlot, 600, 400);
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
        JFreeChart plot = ChartFactory.createScatterPlot("Prediction", "x1", "x2", points);
        try {
            ChartUtilities.saveChartAsJPEG(new File("./plots/plotPrediction" + Integer.toString(index) + ".jpg"), plot, 600, 400);
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
        JFreeChart plot = ChartFactory.createXYLineChart("Accuracy", "Epochs", "Accuracy", dataset);
        try {
            ChartUtilities.saveChartAsJPEG(new File("./plots/accuracy.jpg"), plot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
