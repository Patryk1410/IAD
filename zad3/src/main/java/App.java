import cli.ExerciseSelectionMenu;

/**
 * Created by patry on 20/05/17.
 */
public class App {
    
    public static void main(String... args) {
        App app = new App();
        app.run();
    }

    private void run() {
        ExerciseSelectionMenu menu = new ExerciseSelectionMenu();
        menu.run();
    }
}
