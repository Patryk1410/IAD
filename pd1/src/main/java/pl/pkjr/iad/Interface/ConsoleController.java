package pl.pkjr.iad.Interface;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by patry on 04/03/2017.
 */
public class ConsoleController {

    public static void clear() {
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(String text) {
        System.out.println(text);
    }

    public static int getInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
