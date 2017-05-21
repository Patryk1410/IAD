package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 20/05/17.
 */
public class ExerciseSelector {
    private Map<Integer, InterfaceModule> exercises;

    private static ExerciseSelector instance = new ExerciseSelector();

    public static ExerciseSelector getInstance() {
        return instance;
    }

    private ExerciseSelector() {
        exercises = new HashMap<>();
        //TODO: Insert exercises here
    }

    public InterfaceModule getExercise(Integer exerciseNumber) {
        return exercises.get(exerciseNumber);
    }
}
