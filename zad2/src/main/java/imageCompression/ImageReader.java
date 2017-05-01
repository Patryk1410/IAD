package imageCompression;

import model.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Created by patry on 01/05/17.
 */
public class ImageReader {

    private static ImageReader instance = new ImageReader();

    public static ImageReader getInstance() {
        return instance;
    }

    private ImageReader(){}

    public int[][] getPixels(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            int width = image.getWidth();
            int height = image.getHeight();
//            Pixel[][] pixels = new Pixel[width][height];
            int[][] pixels = new int[height][width];
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    int color = image.getRGB(i, j);
                    pixels[i][j] = color;
                }
            }
            return pixels;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage getImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
