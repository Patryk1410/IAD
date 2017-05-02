package imageCompression;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by patry on 01/05/17.
 */
public class ImageSaver {

    private static ImageSaver instance = new ImageSaver();

    public static ImageSaver getInstance() {
        return instance;
    }

    private ImageSaver(){}

    public void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveColors(int[] meanColors, BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
//        for (int c = 0; c < meanColors.length; ++c) {
//            for (int i = 0; i < height; ++i) {
//
//            }
//        }
    }
}
