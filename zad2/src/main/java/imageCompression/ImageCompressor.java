package imageCompression;

import machineLearning.KMeans;
import model.Pair;
import model.Pixel;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import util.FilesUtil;
import util.VectorUtil;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by patry on 01/05/17.
 */
public class ImageCompressor {

    private String imageName;
    private KMeans kMeans;
    private int numberOfColors;
    private int[] meanColors;
    private Pixel[] initialPositions;
    private byte[] compressedImageData;
    private int currentIndex;

    public ImageCompressor(String imageName, int numberOfColors) {
        this.imageName = imageName;
        this.numberOfColors = numberOfColors;
        meanColors = new int[numberOfColors];
        initialPositions = new Pixel[numberOfColors];
    }

    public void compress() {
        computeMeanColors();
        updateColors();
        saveCompressedImage();
    }

    private void saveCompressedImage() {
        String filePath = "./pictures/" + imageName + "_compressedData";
//        byte[] testData = {0x10, 0x16, 0x1a, 0x0d, 0x2c};
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(compressedImageData);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateColors() {
        getMeanColors();
        saveColors();
        BufferedImage largeImage = readLargeImage();
        int height = largeImage.getHeight();
        int width = largeImage.getWidth();
        compressedImageData = new byte[4 + (numberOfColors * 3) + ((height * width * numberOfColors) / 256)];
        setUpColorsDictionary(height, width);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int color = largeImage.getRGB(j, i);
                Pair<Integer, Byte> newColor = findNewColor(color);
                largeImage.setRGB(j, i, newColor.getFirst());
                compressedImageData[currentIndex++] = newColor.getSecond();
            }
        }
        saveLargeImage(largeImage);
    }

    private void setUpColorsDictionary(int height, int width) {
        byte height1 = (byte) ((height & 0x0000ff00) >> 8);
        byte height2 = (byte) (height & 0x000000ff);
        byte width1 = (byte) ((width & 0x0000ff00) >> 8);
        byte width2 = (byte) (width & 0x000000ff);
        compressedImageData[0] = height1;
        compressedImageData[1] = height2;
        compressedImageData[2] = width1;
        compressedImageData[3] = width2;
        for (int i = 0; i < numberOfColors; i++) {
            byte red = (byte) ((meanColors[i] >> 16) & 0x000000FF);
            byte green = (byte) ((meanColors[i] >> 8) & 0x000000FF);
            byte blue = (byte) (meanColors[i] & 0x000000FF);
            compressedImageData[4 + i * 3] = red;
            compressedImageData[4 + i * 3 + 1] = green;
            compressedImageData[4 + i * 3 + 2] = blue;
        }
        currentIndex = numberOfColors * 3 + 4;
    }

    private void saveColors() {
        FilesUtil.getInstance().clearDirectory("./pictures/colors");
        String smallImagePath = "./pictures/" + imageName + "_small.jpg";
        BufferedImage image = ImageReader.getInstance().getImage(smallImagePath);
        ImageSaver.getInstance().saveColors(meanColors, image);
    }

    private void saveLargeImage(BufferedImage largeImage) {
        String path = "./pictures/" + imageName + "_compressed.jpg";
        ImageSaver.getInstance().saveImage(largeImage, path);
    }

    private Pair<Integer, Byte> findNewColor(int color) {
        int newColor = meanColors[0];
        byte newColorIndex = 0;
        for (int i = 1; i < meanColors.length; ++i) {
//            if (meanColors[i] != 0 && Math.abs(color - meanColors[i]) < Math.abs(color - newColor)) {
            int red = (color >> 16) & 0x000000FF;
            int green = (color >> 8) & 0x000000FF;
            int blue = color & 0x000000FF;
            int newRed = (newColor >> 16) & 0x000000FF;
            int newGreen = (newColor >> 8) & 0x000000FF;
            int newBlue = newColor & 0x000000FF;
            int meanRed = (meanColors[i] >> 16) & 0x000000FF;
            int meanGreen = (meanColors[i] >> 8) & 0x000000FF;
            int meanBlue = meanColors[i] & 0x000000FF;
            double[] colorArr = {red, green, blue};
            double[] newColorArr = {newRed, newGreen, newBlue};
            double[] meanColorArr = {meanRed, meanGreen, meanBlue};
            Vector colorVector = new BasicVector(colorArr);
            Vector newColorVector = new BasicVector(newColorArr);
            Vector meanColorVector = new BasicVector(meanColorArr);
            if (VectorUtil.getInstance().euclideanDistance(colorVector, meanColorVector)
                    < VectorUtil.getInstance().euclideanDistance(colorVector, newColorVector)) {
                newColor = meanColors[i];
                newColorIndex = (byte) i;
            }
        }
        return new Pair<>(newColor, newColorIndex);
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
//        Matrix centroids = getInitialCentroidPosiions(image);
        kMeans = new KMeans(20, dataset, numberOfColors);
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
