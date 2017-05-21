package machineLearning;

import machineLearning.positionTrainer.PositionTrainer;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

/**
 * Created by patry on 20/05/17.
 */
public class RadialNeuralNetwork {

    private static final double EPSILON = 1;

    PositionTrainer positionTrainer;

    private Matrix x; //input data
    private Matrix y; //answers
    private Matrix c; //radial neurons positions
    private Matrix r; //radial neurons widths
    private Matrix theta; //linear neurons weights

    private int numberOfNeuronsInHiddenLayer;
    private int numberOfNeuronsInOutpuLayer;
    private int numberOfIterations;

    private double alpha; //learning rate

    public RadialNeuralNetwork(Matrix x, Matrix y, double alpha) {
        this.x = x;
        this.y = y;
        this.alpha = alpha;
        r = new Basic2DMatrix(numberOfNeuronsInHiddenLayer, x.columns());
        randomlyInitTheta();
    }

    private void randomlyInitTheta() {
        theta = new Basic2DMatrix(numberOfNeuronsInOutpuLayer, numberOfNeuronsInHiddenLayer + 1);
        theta.each((i, j, v) -> theta.set(i, j,Math.random() * 2 * EPSILON - EPSILON));
    }

    public void fit() {
        trainHiddenLayer();
        for (int i = 0; i < numberOfIterations; i++) {
            forwardPropagate();
            backPropagate();
        }
    }

    private void forwardPropagate() {
        //TODO: implement
    }

    private void backPropagate() {
        //TODO: implement
    }

    private void trainHiddenLayer() {
        computeNeuronsPositions();
        computeNeuronsWidths();
    }

    private void computeNeuronsPositions() {
//        KMeans kMeans = new KMeans(20, x, numberOfNeuronsInHiddenLayer);
//        kMeans.fit();
//        c = kMeans.getCentroidPositions();
        positionTrainer.train(x, numberOfNeuronsInHiddenLayer);
    }

    private void computeNeuronsWidths() {
        KNearestNeighbors kNearestNeighbors = new KNearestNeighbors(20, x, c);
        kNearestNeighbors.fit();
        r = kNearestNeighbors.getWeights();
    }
}
