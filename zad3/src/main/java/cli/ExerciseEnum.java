package cli;

/**
 * Created by patry on 20/05/17.
 */
public enum ExerciseEnum {
    exercise1(1),
    exercise2(2),
    exercise3(3);

    private int value;

    ExerciseEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
