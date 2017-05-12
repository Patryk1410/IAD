package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 10/05/17.
 */
public class ExerciseSelector {
    private Map<Integer, InterfaceModule> exercises;

    private static ExerciseSelector instance = new ExerciseSelector();

    public static ExerciseSelector getInstance() {
        return instance;
    }

    private ExerciseSelector() {
        exercises = new HashMap<>();
        exercises.put(ExerciseEnum.exercise1.getValue(), new SelfOrganizingNeuralNetworkExercise());
        exercises.put(ExerciseEnum.exercise2.getValue(), new KMeansExercise());
        exercises.put(ExerciseEnum.exercise3.getValue(), new ImageCompressionExercise());
    }

    public InterfaceModule getExercise(Integer exerciseNumber) {
        return exercises.get(exerciseNumber);
    }
}
