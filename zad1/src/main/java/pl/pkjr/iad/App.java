package pl.pkjr.iad;

import pl.pkjr.iad.console.ConsoleController;
import pl.pkjr.iad.exercises.Exercise;
import pl.pkjr.iad.exercises.ExerciseSelector;

/**
 * Hello world!
 *
 */
public class App 
{
    private final String kSelectExercise = "Select exercise:";
    private final String kExerciseOne = "Exercise 1: Approximation";
    private final String kExerciseTwo = "Exercise 2: Classification";
    private final String kExerciseThree = "Exercise 3: Transformation";
    private final String kTest = "Test";

    private final int kFirstExerciseIndex = 1;
    private final int kNumberOfExercises = 3;
    private final int kTestEnabled = 1;

    private Exercise exercise;

    private static boolean shouldContinue;
    private int input;

    private App() {
        shouldContinue = true;
    }

    private void run() {
        while (shouldContinue) {
            selectExercise();
            performExercise();
        }
    }

    private void selectExercise() {
        printExerciseSelection();
        input = ConsoleController.getInt(kFirstExerciseIndex, kNumberOfExercises + kTestEnabled);
        exercise = ExerciseSelector.getInstance().getExercise(input);
    }

    private void performExercise() {
        exercise.run();
    }

    private void printExerciseSelection() {
        ConsoleController.print(kSelectExercise + '\n');
        ConsoleController.print(kExerciseOne);
        ConsoleController.print(kExerciseTwo);
        ConsoleController.print(kExerciseThree);
        if (kTestEnabled == 1) {
            ConsoleController.print(kTest);
        }
    }

    public static void setShouldContinue(boolean shouldContinue) {
        App.shouldContinue = shouldContinue;
    }

    public static void main( String[] args ) {
        App app = new App();
        app.run();
    }
}
