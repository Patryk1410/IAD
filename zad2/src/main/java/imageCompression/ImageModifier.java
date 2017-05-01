package imageCompression;

import java.awt.image.BufferedImage;

/**
 * Created by patry on 01/05/17.
 */
public class ImageModifier {

    private static ImageModifier instance = new ImageModifier();

    public static ImageModifier getInstance() {
        return instance;
    }

    private ImageModifier(){}

    public void setPixels(BufferedImage image, int[][] pixels) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int color = pixels[i][j];
                image.setRGB(i, j, color);
            }
        }
    }

    public void createArt(BufferedImage image, int[][] pixels) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int color = (int)(Math.random()*100000);
                image.setRGB(i, j, color);
            }
        }
    }
}
