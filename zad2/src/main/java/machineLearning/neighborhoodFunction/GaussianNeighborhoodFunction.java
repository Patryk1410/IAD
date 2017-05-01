package machineLearning.neighborhoodFunction;

import org.la4j.Vector;
import util.VectorUtil;

/**
 * Created by patry on 29/04/17.
 */
public class GaussianNeighborhoodFunction implements NeighborhoodFunction {

    @Override
    public double compute(Vector bestMatchingUnit, Vector node, int iteration, int numberOfIterations, double lambda) {
        double distance = VectorUtil.getInstance().euclideanDistance(bestMatchingUnit, node);
        double neighborhoodRadius = lambda * ((numberOfIterations - iteration) * 1.0 / numberOfIterations);
        return Math.exp(-1.0 * (distance * distance) / (2 * neighborhoodRadius * neighborhoodRadius));
    }
}
