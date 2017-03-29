package pl.pkjr.iad.exercises;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 06/03/2017.
 */
public class ExerciseSelector {

    private Map<Integer, Exercise> exercises;

    private static ExerciseSelector instance = null;

    public static ExerciseSelector getInstance() {
        if (instance == null) {
            instance = new ExerciseSelector();
        }
        return instance;
    }

    private ExerciseSelector() {
        exercises = new HashMap<>();
        exercises.put(ExerciseEnum.firstExercise.getValue(), new Exercise1());
        exercises.put(ExerciseEnum.secondExercise.getValue(), new Exercise2());
        exercises.put(ExerciseEnum.thirdExercise.getValue(), new Exercise3());
        exercises.put(ExerciseEnum.testExercise.getValue(), new TestExercise());
    }

    public Exercise getExercise(Integer exerciseNumber) {
        return exercises.get(exerciseNumber);
    }
}
