package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.la4j.Matrix;

import java.io.File;
import java.io.IOException;

/**
 * Created by patry on 30/04/17.
 */
public class ChartsUtil {

    private static ChartsUtil instance = new ChartsUtil();

    private ChartsUtil(){}

    public static ChartsUtil getInstance() {
        return instance;
    }

    public void plotDataset(String path, Matrix dataset) {
        XYSeries data = new XYSeries("Training data");
        for (int i = 0; i < dataset.rows(); ++i) {
            data.add(dataset.get(i, 0), dataset.get(i, 1));
        }
        XYSeriesCollection points = new XYSeriesCollection();
        points.addSeries(data);
        JFreeChart chart = ChartFactory.createScatterPlot("Training data", "x1", "x2", points);
        try {
            ChartUtilities.saveChartAsJPEG(new File(path), chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plotNetworkState(String path, Matrix dataset, Matrix theta) {
        XYSeries data = new XYSeries("Training data");
        XYSeries neurons = new XYSeries("Neurons");
        for (int i = 0; i < dataset.rows(); ++i) {
            data.add(dataset.get(i, 0), dataset.get(i, 1));
        }
        for (int i = 0; i < theta.rows(); ++i) {
            neurons.add(theta.get(i, 0), theta.get(i, 1));
        }
        XYSeriesCollection points = new XYSeriesCollection();
        points.addSeries(neurons);
        points.addSeries(data);
        JFreeChart chart = ChartFactory.createScatterPlot("Training data", "x1", "x2", points);
        try {
            ChartUtilities.saveChartAsJPEG(new File(path), chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
