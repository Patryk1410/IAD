package cli;

/**
 * Created by patry on 10/05/17.
 */
public class ExerciseSelectionMenu implements InterfaceModule {

    private static final String SELECT_EXERCISE = "Select exercise:\n";
    private static final String EXERCISE1 = "Exercise 1: Self organizing neural network";
    private static final String EXERCISE2 = "Exercise 2: K-means";
    private static final String EXERCISE3 = "Exercise 3: Image compression";

    @Override
    public void run() {
        printExerciseSelection();
        int exerciseNumber = commandLineUtil.getInt(1, 3);
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
