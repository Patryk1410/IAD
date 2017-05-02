package model;

/**
 * Created by patry on 01/05/17.
 */
public class Pixel {

    private int red;
    private int green;
    private int blue;

    public Pixel(int color) {
        this.red = (color >> 16) & 0x000000FF;
        this.green = (color >> 8) & 0x000000FF;
        this.blue = color & 0x000000FF;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
