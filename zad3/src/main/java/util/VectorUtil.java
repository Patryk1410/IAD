package util;

import org.la4j.Vector;

/**
 * Created by patry on 20/05/17.
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
}
