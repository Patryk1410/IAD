package cli;

/**
 * Created by patry on 20/05/17.
 */
public class ExerciseSelectionMenu implements InterfaceModule {
    private static final String SELECT_EXERCISE = "Select exercise:\n";
    private static final String EXERCISE1 = "Exercise 1: Approximation";
    private static final String EXERCISE2 = "Exercise 2: Classification";
    private static final String EXERCISE3 = "Exercise 3: Gradient descent on both layers";

    private static final int MIN_EXERCISE_NUMBER = 1;
    private static final int NUMBER_OF_EXERCISES = 3;

    @Override
    public void run() {
        printExerciseSelection();
        int exerciseNumber = commandLineUtil.getInt(MIN_EXERCISE_NUMBER, NUMBER_OF_EXERCISES);
        InterfaceModule exercise = ExerciseSelector.getInstance().getExercise(exerciseNumber);
        exercise.run();
    }

    private void printExerciseSelection() {
        commandLineUtil.print(SELECT_EXERCISE);
        commandLineUtil.print(EXERCISE1);
        commandLineUtil.print(EXERCISE2);
        commandLineUtil.print(EXERCISE3);
    }
}
