package machineLearning;

import machineLearning.outputFunction.OutputFunction;
import machineLearning.outputFunction.OutputFunctionSelector;
import machineLearning.outputFunction.OutputFunctionType;
import machineLearning.positionTrainer.PositionTrainer;
import machineLearning.positionTrainer.PositionTrainerSelector;
import machineLearning.positionTrainer.PositionTrainerType;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import util.MatrixUtil;
import util.VectorUtil;

/**
 * Created by patry on 20/05/17.
 */
public class RadialNeuralNetwork {

    private static final double EPSILON = 1;

    private PositionTrainer positionTrainer;
    private OutputFunction outputFunction;

    private Matrix x; //input data
    private Matrix y; //answers
    private Matrix c; //radial neurons positions
    private Matrix r; //radial neurons widths
    private Matrix theta; //linear neurons weights

    private Matrix hiddenLayerOutputs;
    private Matrix outputLayerInputs;
    private Matrix outputLayerOutputs;

    private int numberOfNeuronsInHiddenLayer;
    private int numberOfNeuronsInOutputLayer;
    private int numberOfIterations;

    private double alpha; //learning rate

    public RadialNeuralNetwork(Matrix x, Matrix y, double alpha, PositionTrainerType trainerType,
                               int numberOfNeuronsInHiddenLayer, int numberOfNeuronsInOutputLayer,
                               int numberOfIterations, OutputFunctionType outputFunctionType) {
        this.x = x;
        this.y = y;
        this.alpha = alpha;
        r = new Basic2DMatrix(this.numberOfNeuronsInHiddenLayer, x.columns());
        this.numberOfNeuronsInHiddenLayer = numberOfNeuronsInHiddenLayer;
        this.numberOfNeuronsInOutputLayer = numberOfNeuronsInOutputLayer;
        this.numberOfIterations = numberOfIterations;
        randomlyInitTheta();
        positionTrainer = PositionTrainerSelector.getInstance().selectTrainer(trainerType);
        outputFunction = OutputFunctionSelector.getInstance().getOutputFunction(outputFunctionType);
        this.hiddenLayerOutputs = new Basic2DMatrix(x.rows(), numberOfNeuronsInHiddenLayer);
        this.outputLayerInputs = new Basic2DMatrix(x.rows(), numberOfNeuronsInOutputLayer);
        this.outputLayerOutputs = new Basic2DMatrix(x.rows(), numberOfNeuronsInOutputLayer);
    }

    private void randomlyInitTheta() {
        theta = new Basic2DMatrix(numberOfNeuronsInOutputLayer, numberOfNeuronsInHiddenLayer + 1);
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
        computeHiddenLayer();
        computeOutputLayer();
    }

    private void computeHiddenLayer() {
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < hiddenLayerOutputs.columns(); j++) {
                Vector sample = x.getRow(i);
                Vector neuron = c.getRow(j);
                double value = VectorUtil.getInstance().euclideanDistance(sample, neuron);
                double radius = r.get(j, 0);
                hiddenLayerOutputs.set(i, j, Math.exp(-((value * value) / (radius * radius))));
            }
        }
    }

    private void computeOutputLayer() {
        Matrix hiddenLayerOutputsWithBias = MatrixUtil.getInstance().addColumnOfOnesToMatrix(hiddenLayerOutputs);
        outputLayerInputs = hiddenLayerOutputsWithBias.multiply(theta.transpose());
        outputLayerOutputs = outputFunction.activate(outputLayerInputs);
    }

    private void backPropagate() {
        //TODO: implement
    }

    private void trainHiddenLayer() {
        computeNeuronsPositions();
        computeNeuronsWidths();
    }

    private void computeNeuronsPositions() {
        c = positionTrainer.train(x, numberOfNeuronsInHiddenLayer);
    }

    private void computeNeuronsWidths() {
        KNearestNeighbors kNearestNeighbors = new KNearestNeighbors(15, x, c);
        kNearestNeighbors.fit();
        r = kNearestNeighbors.getWeights();
    }
}
