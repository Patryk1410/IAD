package pl.pkjr.iad.machineLearning.costFunction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by patry on 24/03/2017.
 */
public class CostFunctionSelector {

    private static Map<CostFunctionType, CostFunction> costFunctions = null;

    public static CostFunction getCostFunction(CostFunctionType costFunction) {
        if (costFunctions == null) {
            costFunctions = new LinkedHashMap<>();
            costFunctions.put(CostFunctionType.quadratic, new QuadraticCostFunction());
            costFunctions.put(CostFunctionType.logarithmic, new LogarithmicCostFunction());
        }
        return costFunctions.get(costFunction);
    }
}
