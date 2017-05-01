import org.junit.Test;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

/**
 * Created by patry on 29/04/17.
 */
public class VectorUtil {

    @Test
    public void normTest() {
        double[] arr = {1, 2, 3, 4, 5};
        Vector v = new BasicVector(arr);
        System.out.print(v.norm());
    }
}
