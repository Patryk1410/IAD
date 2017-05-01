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
}
