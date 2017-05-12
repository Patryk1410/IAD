package cli;

import imageCompression.ImageCompressor;
import util.DatasetUtil;

/**
 * Created by patry on 10/05/17.
 */
public class ImageCompressionExercise implements InterfaceModule {

    private static final String SELECT_NUMBER_OF_CENTROIDS = "Select number of centroids";
    private static final String SELECT_PICTURE = "Select picture";
    private int k;
    private String picture;

    @Override
    public void run() {
        initParams();
        runCompression();
    }

    private void runCompression() {
        ImageCompressor imageCompressor = new ImageCompressor(picture, k);
        imageCompressor.compress();
    }

    private void initParams() {
        commandLineUtil.print(SELECT_NUMBER_OF_CENTROIDS);
        k = commandLineUtil.getInt(1, 10000);
        commandLineUtil.print(SELECT_PICTURE);
        picture = commandLineUtil.getString();
    }
}
