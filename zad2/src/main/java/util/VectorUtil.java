package util;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

/**
 * Created by patry on 29/04/17.
 */
public class VectorUtil {

    private static VectorUtil instance = new VectorUtil();

    private VectorUtil() {}

    public static VectorUtil getInstance() {
        return instance;
    }

    public double euclideanDistance(Vector first, Vector other) {
        if (first.length() != other.length()) {
            throw new IllegalArgumentException("Vectors must be of the same length to compute the euclidean distance");
        }
        double res = 0;
        for (int i = 0; i < first.length(); ++i) {
            res += (first.get(i) - other.get(i)) * (first.get(i) - other.get(i));
        }
        return Math.sqrt(res);
    }

    public double colorDistance(Vector first, Vector other) {
        Vector labFirst = fromRGB(first.get(0), first.get(1), first.get(2));
        Vector labSecond = fromRGB(other.get(0), other.get(1), other.get(2));
        double res = 0;
        for (int i = 0; i < labFirst.length(); ++i) {
            res += (labFirst.get(i) - labSecond.get(i)) * (labFirst.get(i) - labSecond.get(i));
        }
        return Math.sqrt(res);
    }

    public static Vector fromRGB(double ri, double gi, double bi) {
        // first, normalize RGB values
        double r = ri / 255.0;
        double g = gi / 255.0;
        double b = bi / 255.0;

        // D65 standard referent
        double X = 0.950470, Y = 1.0, Z = 1.088830;

        // second, map sRGB to CIE XYZ
        r = r <= 0.04045 ? r/12.92 : Math.pow((r+0.055)/1.055, 2.4);
        g = g <= 0.04045 ? g/12.92 : Math.pow((g+0.055)/1.055, 2.4);
        b = b <= 0.04045 ? b/12.92 : Math.pow((b+0.055)/1.055, 2.4);
        double x = (0.4124564*r + 0.3575761*g + 0.1804375*b) / X,
                y = (0.2126729*r + 0.7151522*g + 0.0721750*b) / Y,
                z = (0.0193339*r + 0.1191920*g + 0.9503041*b) / Z;

        // third, map CIE XYZ to CIE L*a*b* and return
        x = x > 0.008856 ? Math.pow(x, 1.0/3) : 7.787037*x + 4.0/29;
        y = y > 0.008856 ? Math.pow(y, 1.0/3) : 7.787037*y + 4.0/29;
        z = z > 0.008856 ? Math.pow(z, 1.0/3) : 7.787037*z + 4.0/29;

        double L = 116*y - 16,
                A = 500*(x-y),
                B = 200*(y-z);

//        if (binSize > 0) {
//            L = binSize * Math.floor(L / binSize);
//            A = binSize * Math.floor(A / binSize);
//            B = binSize * Math.floor(B / binSize);
//        }
        double[] resArr = {L, A, B};
        return new BasicVector(resArr);
    }

}
