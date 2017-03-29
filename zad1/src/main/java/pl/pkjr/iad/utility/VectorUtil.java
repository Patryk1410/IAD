package pl.pkjr.iad.utility;

import org.la4j.Vector;

/**
 * Created by patry on 29/03/2017.
 */
public class VectorUtil {
    public static int getIndexOfMaxElement(Vector vector) {
        double maxElem = vector.max();
        for (int i = 0; i < vector.length(); ++i) {
            if (vector.get(i) == maxElem) {
                return i;
            }
        }
        return 0;
    }
}
