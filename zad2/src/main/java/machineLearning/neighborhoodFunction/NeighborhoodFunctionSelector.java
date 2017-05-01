package machineLearning.neighborhoodFunction;

import java.util.HashMap;
import java.util.Map;

import static machineLearning.neighborhoodFunction.NeighborhoodFunctionType.GAUSSIAN;
import static machineLearning.neighborhoodFunction.NeighborhoodFunctionType.RECTANGULAR;

/**
 * Created by patry on 29/04/17.
 */
public class NeighborhoodFunctionSelector {

    private static NeighborhoodFunctionSelector instance = new NeighborhoodFunctionSelector();

    private Map<NeighborhoodFunctionType, NeighborhoodFunction> functionMap = new HashMap<>();

    private NeighborhoodFunctionSelector() {
        functionMap.put(RECTANGULAR, new RectangularNeighborhoodFunction());
        functionMap.put(GAUSSIAN, new GaussianNeighborhoodFunction());
    }

    public static NeighborhoodFunctionSelector getInstance() {
        return instance;
    }

    public NeighborhoodFunction getFunction(NeighborhoodFunctionType type) {
        return functionMap.get(type);
    }
}
