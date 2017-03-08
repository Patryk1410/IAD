package pl.pkjr.iad.exercises;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patry on 06/03/2017.
 */
public class ExerciseSelector {

    private Map<ExerciseEnum, Exercise> exercises;

    private static ExerciseSelector instance = null;

    public static ExerciseSelector getInstance() {
        if (instance == null) {
            instance = new ExerciseSelector();
        }
        return instance;
    }

    private ExerciseSelector() {
        exercises = new HashMap<>();
        exercises.put(ExerciseEnum.firstExercise, new Exercise1());
        exercises.put(ExerciseEnum.secondExercise, new Exercise2());
        exercises.put(ExerciseEnum.thirdExercise, new Exercise3());
    }

    public Exercise getExercise(int exerciseNumber) {
        return exercises.get(exerciseNumber);
    }
}
