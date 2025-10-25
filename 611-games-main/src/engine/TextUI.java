package engine;

import java.util.Scanner;

/**
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class TextUI {
    private final Scanner sc = new Scanner(System.in);
    private boolean color = true;

    public void setColor(boolean on) {
        color = on;
    }

    public boolean isColor() {
        return color;
    }

    // Basic I/O operations
    public void println(String s) {
        System.out.println(s);
    }

    public void print(String s) {
        System.out.print(s);
    }

    public String nextLine() {
        return sc.nextLine();
    }

    /**
     * Wrap text with ANSI color code.
     * If color is disabled, returns plain text.
     */
    private String wrap(String code, String s) {
        return color ? code + s + "\u001B[0m" : s;
    }

    // ANSI color helpers
    public String bold(String s) {
        return wrap("\u001B[1m", s);
    }

    public String green(String s) {
        return wrap("\u001B[32m", s);
    }

    public String red(String s) {
        return wrap("\u001B[31m", s);
    }

    public String blue(String s) {
        return wrap("\u001B[34m", s);
    }

    public String cyan(String s) {
        return wrap("\u001B[36m", s);
    }

    public String yellow(String s) {
        return wrap("\u001B[33m", s);
    }

    public String magenta(String s) {
        return wrap("\u001B[35m", s);
    }

    /**
     * Safely parse a string to integer.
     * @return parsed integer or null if parsing fails
     */
    public Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }
}