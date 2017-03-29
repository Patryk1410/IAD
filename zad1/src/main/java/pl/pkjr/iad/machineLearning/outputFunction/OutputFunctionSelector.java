package pl.pkjr.iad.machineLearning.outputFunction;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.linear;
import static pl.pkjr.iad.machineLearning.outputFunction.OutputFunctionType.sigmoid;

/**
 * Created by patry on 29/03/2017.
 */
public class OutputFunctionSelector {
    private static Map<OutputFunctionType, OutputFunction> outputFunctions = null;

    public static OutputFunction getOutputFunction(OutputFunctionType outputFunction) {
        if (outputFunctions == null) {
            outputFunctions = new LinkedHashMap<>();
            outputFunctions.put(linear, new LinearOutputFunction());
            outputFunctions.put(sigmoid, new SigmoidOutputFunction());
        }
        return outputFunctions.get(outputFunction);
    }
}
