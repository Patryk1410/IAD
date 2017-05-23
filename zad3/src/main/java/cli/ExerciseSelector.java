package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 20/05/17.
 */
public class ExerciseSelector {
    private Map<ExerciseEnum, InterfaceModule> exercises;

    private static ExerciseSelector instance = new ExerciseSelector();

    public static ExerciseSelector getInstance() {
        return instance;
    }

    private ExerciseSelector() {
        exercises = new HashMap<>();
        exercises.put(ExerciseEnum.exercise1, new ApproximationExercise());
        exercises.put(ExerciseEnum.exercise2, new ClassificationExercise());
    }

    public InterfaceModule getExercise(ExerciseEnum exercise) {
        return exercises.get(exercise);
    }
}
