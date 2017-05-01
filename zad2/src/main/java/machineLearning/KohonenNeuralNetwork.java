package machineLearning;

import machineLearning.neighborhoodFunction.NeighborhoodFunction;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionSelector;
import machineLearning.neighborhoodFunction.NeighborhoodFunctionType;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.ChartsUtil;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patry on 29/04/17.
 */
public class KohonenNeuralNetwork extends SelfOrganizingNeuralNetwork {

    private NeighborhoodFunction neighborhoodFunction;

    public KohonenNeuralNetwork(int numberOfIterations, Matrix x, int numberOfNeurons,
                                NeighborhoodFunctionType neighborhoodFunction, double lambda, double alpha) {
        super(numberOfIterations, x, numberOfNeurons, lambda, alpha, "Kohonen");
        this.neighborhoodFunction = NeighborhoodFunctionSelector.getInstance().getFunction(neighborhoodFunction);
    }

    @Override
    protected int findBestMatchingUnit(Vector sample) {
        int bestMatchingUnitIndex = 0;
        double lowestDistance = Double.MAX_VALUE;
        int count = 0;
        for (int i = 0; i < theta.rows(); ++i) {
            if (potentials.get(i) < ADAPTATION_POTENTIAL) {
                continue;
            }
            ++count;
            Vector currentRow = theta.getRow(i);
            double distance = VectorUtil.getInstance().euclideanDistance(currentRow, sample);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                bestMatchingUnitIndex = i;
            }
        }
//        System.out.println("Processed " + count + " units");
        updatePotentials(bestMatchingUnitIndex);
        return bestMatchingUnitIndex;
    }

    @Override
    protected void update(Vector sample, int bestMatchingUnitIndex, int iterationNumber) {
        theta.each((int i, int j, double value) -> {
            Vector bestMatchingUnit = theta.getRow(bestMatchingUnitIndex);
            double newValue = value + (neighborhoodFunction.compute(bestMatchingUnit, theta.getRow(i),
                    iterationNumber, numberOfIterations, lambda) * getLearningRateParameter(iterationNumber)
                    * (sample.get(j) - value));
            theta.set(i, j, newValue);
        });
    }
}
