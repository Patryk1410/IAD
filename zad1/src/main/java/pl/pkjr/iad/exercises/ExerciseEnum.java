package pl.pkjr.iad.exercises;

/**
 * Created by patry on 08/03/2017.
 */
public enum  ExerciseEnum {
    firstExercise(1),
    secondExercise(2),
    thirdExercise(3);

    private Integer value;

    private ExerciseEnum(Integer value) {
        this.value = value;
    }
}
