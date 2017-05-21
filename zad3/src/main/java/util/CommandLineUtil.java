package util;

import java.util.Scanner;

/**
 * Created by patry on 20/05/17.
 */
public class CommandLineUtil {
    private static final String WRONG_VALUE = "You must choose value from range [%d, %d] \n";

    private static CommandLineUtil instance = new CommandLineUtil();

    private CommandLineUtil(){}

    public static CommandLineUtil getInstance() {
        return instance;
    }

    public void print(String text) {
        System.out.println(text);
    }

    public int getInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public int getInt(int a, int b) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        int res = getInt();
        while (res < a || res > b) {
            print(String.format(WRONG_VALUE, a, b));
            res = getInt();
        }
        return res;
    }

    public double getDouble() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public double getDouble(double a, double b) {
        if (a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double res = getDouble();
        while (res < a || res > b) {
            print(String.format(WRONG_VALUE, a, b));
            res = getDouble();
        }
        return res;
    }

    public char getChar() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }

    public String getString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
