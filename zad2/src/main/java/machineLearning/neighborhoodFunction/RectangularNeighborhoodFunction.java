package machineLearning.neighborhoodFunction;

import org.la4j.Vector;
import util.VectorUtil;

/**
 * Created by patry on 29/04/17.
 */
public class RectangularNeighborhoodFunction implements NeighborhoodFunction {

    @Override
    public double compute(Vector bestMatchingUnit, Vector node, int iteration, int numberOfIterations, double lambda) {
        double distance = VectorUtil.getInstance().euclideanDistance(bestMatchingUnit, node);
        double neighborhoodRadius = lambda * ((numberOfIterations - iteration) * 1.0 / numberOfIterations);
        return distance > neighborhoodRadius ? 1.0 : 0.0;
    }
}
