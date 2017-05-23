package machineLearning.outputFunction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static machineLearning.outputFunction.OutputFunctionType.linear;
import static machineLearning.outputFunction.OutputFunctionType.sigmoid;

/**
 * Created by patry on 29/03/2017.
 */
public class OutputFunctionSelector {

    private static OutputFunctionSelector instance = new OutputFunctionSelector();

    public static OutputFunctionSelector getInstance() {
        return instance;
    }

    private OutputFunctionSelector() {
        outputFunctions = new HashMap<>();
        outputFunctions.put(linear, new LinearOutputFunction());
        outputFunctions.put(sigmoid, new SigmoidOutputFunction());
    }

    private Map<OutputFunctionType, OutputFunction> outputFunctions;

    public OutputFunction getOutputFunction(OutputFunctionType outputFunction) {
        return outputFunctions.get(outputFunction);
    }
}
