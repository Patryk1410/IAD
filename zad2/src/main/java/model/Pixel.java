package model;

/**
 * Created by patry on 01/05/17.
 */
public class Pixel {

    private int red;
    private int green;
    private int blue;

    public Pixel(int color) {
        int red = (color >> 16) & 0x000000FF;
        int green = (color >> 8) & 0x000000FF;
        int blue = (color) & 0x000000FF;
    }
}
