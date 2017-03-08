package pl.pkjr.iad.console;

import java.util.Scanner;

/**
 * Created by patry on 06/03/2017.
 */
public class ConsoleController {
    private static final String kWrongValue = "You must choose value from range [%i, %i] \n";

    public static void print(String text) {
        System.out.println(text);
    }

    public static int getInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static int getInt(int a, int b) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        int res = getInt();
        while (res < a || res > b) {
            print(String.format(kWrongValue, a, b));
            res = getInt();
        }
        return res;
    }

    public static double getDouble() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public static double getDouble(int a, int b) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        double res = getDouble();
        while (res < a || res > b) {
            print(String.format(kWrongValue, a, b));
            res = getDouble();
        }
        return res;
    }
}
