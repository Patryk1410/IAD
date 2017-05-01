package imageCompression;

import machineLearning.KMeans;
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

    public ImageCompressor(String imageName, int numberOfColors) {
        this.imageName = imageName;
        this.numberOfColors = numberOfColors;
        meanColors = new int[numberOfColors];
    }

    public void compress() {
        getMeanColors();
        updateColors();
    }

    private void updateColors() {
    }

    private void getMeanColors() {
        String smallImagePath = "./pictures/" + imageName + "_small.jpg";
        BufferedImage smallImage = ImageReader.getInstance().getImage(smallImagePath);
        int[][] smallImagePixels = ImageReader.getInstance().getPixels(smallImagePath);
        initKMeans(smallImage, smallImagePixels);
    }

    private void initKMeans(BufferedImage image, int[][] pixels) {
        Matrix dataset = createDataset(image, pixels);
        kMeans = new KMeans(20, dataset, numberOfColors);
        kMeans.fit();
    }

    private Matrix createDataset(BufferedImage image, int[][] pixels) {
        int width = image.getWidth();
        int height = image.getHeight();
        Matrix dataset = new Basic2DMatrix(width*height, 1);
        int pos = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                dataset.set(pos++, 0, pixels[i][j]);
            }
        }
        return dataset;
    }
}
