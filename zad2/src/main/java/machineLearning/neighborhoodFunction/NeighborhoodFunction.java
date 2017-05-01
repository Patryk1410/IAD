package machineLearning.neighborhoodFunction;

import org.la4j.Vector;

/**
 * Created by patry on 29/04/17.
 */
public interface NeighborhoodFunction {

    double compute(Vector bestMatchingUnit, Vector node, int iteration, int numberOfIterations, double lambda);
}
