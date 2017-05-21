package machineLearning;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.VectorUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by patry on 20/05/17.
 */
public class KNearestNeighbors {

    private int K;

    private Matrix x;
    private Matrix positions;
    private Matrix weights;

    private List<Integer> neuronOrder;

    public KNearestNeighbors(int k, Matrix x, Matrix positions) {
        K = k;
        this.x = x;
        this.positions = positions;
        neuronOrder = new ArrayList<>();
        for (int i = 0; i < x.rows(); ++i) {
            neuronOrder.add(i);
        }
        weights = new Basic2DMatrix(K, x.columns());
    }

    public void fit() {
        for (int i = 0; i < positions.rows(); i++) {
            Vector position = positions.getRow(i);
            neuronOrder.sort(Comparator.comparingDouble(o
                    -> VectorUtil.getInstance().euclideanDistance(x.getRow(o), position)));
            double squaredSum = 0;
            for (int j = 0; j < K && j < neuronOrder.size(); j++) {
                int currentClosestNeuronIndex = neuronOrder.get(j);
                Vector currentClosestNeuron = x.getRow(currentClosestNeuronIndex);
                double distance = VectorUtil.getInstance().euclideanDistance(position, currentClosestNeuron);
                squaredSum += distance * distance;
            }
            squaredSum /= K;
            weights.set(i, 0, Math.sqrt(squaredSum));
        }
    }

    public Matrix getWeights() {
        return weights;
    }
}
