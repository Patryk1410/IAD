package imageCompression;

import machineLearning.KMeans;
import model.Pixel;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.awt.image.BufferedImage;

/**
 * Created by patry on 01/05/17.
 */
public class ImageCompressor {

    private String imageName;
    private KMeans kMeans;
    private int numberOfColors;
    private int[] meanColors;
    private Pixel[] initialPositions;

    public ImageCompressor(String imageName, int numberOfColors) {
        this.imageName = imageName;
        this.numberOfColors = numberOfColors;
        meanColors = new int[numberOfColors];
        initialPositions = new Pixel[numberOfColors];
    }

    public void compress() {
        computeMeanColors();
        updateColors();
    }

    private void updateColors() {
        getMeanColors();
        BufferedImage largeImage = readLargeImage();
        int height = largeImage.getHeight();
        int width = largeImage.getWidth();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int color = largeImage.getRGB(j, i);
                int newColor = findNewColor(color);
                largeImage.setRGB(j, i, newColor);
            }
        }
        saveLargeImage(largeImage);
    }

    private void saveLargeImage(BufferedImage largeImage) {
        String path = "./pictures/" + imageName + "_compressed.jpg";
        ImageSaver.getInstance().saveImage(largeImage, path);
    }

    private int findNewColor(int color) {
        int newColor = Integer.MAX_VALUE;
        for (int i = 0; i < meanColors.length; ++i) {
            if (meanColors[i] != 0 && Math.abs(color - meanColors[i]) < Math.abs(color - newColor)) {
                newColor = meanColors[i];
            }
        }
        return newColor;
    }

    private void computeMeanColors() {
        String smallImagePath = "./pictures/" + imageName + "_small.jpg";
        BufferedImage smallImage = ImageReader.getInstance().getImage(smallImagePath);
        Pixel[][] smallImagePixels = ImageReader.getInstance().getPixels(smallImagePath);
        initKMeans(smallImage, smallImagePixels);
        kMeans.fit();
    }

    private void initKMeans(BufferedImage image, Pixel[][] pixels) {
        Matrix dataset = createDataset(image, pixels);
        Matrix centroids = getInitialCentroidPosiions(image);
        kMeans = new KMeans(30, dataset, numberOfColors, centroids);
    }

    private Matrix createDataset(BufferedImage image, Pixel[][] pixels) {
        int width = image.getWidth();
        int height = image.getHeight();
        Matrix dataset = new Basic2DMatrix(width*height, 3);
        int pos = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                dataset.set(pos, 0, pixels[i][j].getRed());
                dataset.set(pos, 1, pixels[i][j].getGreen());
                dataset.set(pos++, 2, pixels[i][j].getBlue());
            }
        }
        return dataset;
    }

    private void getMeanColors() {
        Matrix centroids = kMeans.getCentroidPositions();
        int alpha = 255;
        for (int i = 0; i < centroids.rows(); ++i) {
            int red = Math.toIntExact(Math.round(centroids.get(i, 0)));
            int green = Math.toIntExact(Math.round(centroids.get(i, 1)));
            int blue = Math.toIntExact(Math.round(centroids.get(i, 2)));
            meanColors[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    private BufferedImage readLargeImage() {
        String largeImagePath = "./pictures/" + imageName + "_large.jpg";
        return ImageReader.getInstance().getImage(largeImagePath);
    }

    private Matrix getInitialCentroidPosiions(BufferedImage image) {
        Matrix centroids = new Basic2DMatrix(numberOfColors, 3);
        int height = image.getHeight();
        int width = image.getWidth();
        for (int i = 0; i < numberOfColors; ++i) {
            int x = Math.toIntExact(Math.round(Math.random() * (height - 1)));
            int y = Math.toIntExact(Math.round(Math.random() * (width - 1)));
            int color = image.getRGB(x, y);
            Pixel temp = new Pixel(color);
            centroids.set(i, 0, temp.getRed());
            centroids.set(i, 1, temp.getGreen());
            centroids.set(i, 2, temp.getBlue());
        }
        return centroids;
    }

}
