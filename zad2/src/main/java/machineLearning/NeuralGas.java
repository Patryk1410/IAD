package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by patry on 01/05/17.
 */
public class NeuralGas extends SelfOrganizingNeuralNetwork {

    private static final double ADAPTATION_PENALTY = 100.0;

    private List<Integer> neuronOrder;

    public NeuralGas(int numberOfIterations, Matrix x, int numberOfNeurons, double lambda, double alpha,
                     double adaptationPotential) {
        super(numberOfIterations, x, numberOfNeurons, lambda, alpha, "NeuralGas", adaptationPotential);
        neuronOrder = new ArrayList<>();
        for (int i = 0; i < theta.rows(); ++i) {
            neuronOrder.add(i);
        }
    }

    @Override
    protected int findBestMatchingUnit(Vector sample) {
        neuronOrder.sort(Comparator.comparingDouble(o
                -> VectorUtil.getInstance().euclideanDistance(theta.getRow(o), sample)
                + (potentials.get(o) < adaptationPotential ? ADAPTATION_PENALTY : 0.0)));
        int index = 0;
        updatePotentials(neuronOrder.get(index));
        return neuronOrder.get(index);
    }

    @Override
    protected void update(Vector sample, int bestMatchingUnitIndex, int iterationNumber) {
        theta.each((i, j, value) -> {
            double newValue = value + (neighborhoodFunction(i, iterationNumber)
                    * getLearningRateParameter(iterationNumber) * (sample.get(j) - value));
            theta.set(i, j, newValue);
        });
    }

    private double neighborhoodFunction(int neuron, int iteration) {
        int positionInOrder = neuronOrder.indexOf(neuron);
        double neighborhoodRadius = lambda * ((numberOfIterations - iteration) * 1.0 / numberOfIterations);
        return Math.exp(-1.0 * positionInOrder / neighborhoodRadius);
    }
}
