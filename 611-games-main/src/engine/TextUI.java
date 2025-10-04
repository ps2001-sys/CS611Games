package engine;

import java.util.Scanner;

/**
 * Utility for terminal I/O.
 * Adds basic ANSI colors; can be toggled on/off.
 */
public class TextUI {
    private final Scanner sc = new Scanner(System.in);
    private boolean color = true;  // whether colored text is enabled

    public void setColor(boolean on) { color = on; }
    public boolean isColor() { return color; }

    // I/O
    public void println(String s) { System.out.println(s); }
    public void print(String s) { System.out.print(s); }
    public String nextLine() { return sc.nextLine(); }

    // ANSI helpers
    private String wrap(String code, String s){ return color ? code + s + "\u001B[0m" : s; }

    public String bold(String s){ return wrap("\u001B[1m", s); }
    public String green(String s){ return wrap("\u001B[32m", s); }
    public String red(String s){ return wrap("\u001B[31m", s); }
    public String blue(String s){ return wrap("\u001B[34m", s); }
    public String cyan(String s){ return wrap("\u001B[36m", s); }
    public String yellow(String s){ return wrap("\u001B[33m", s); }
    public String magenta(String s){ return wrap("\u001B[35m", s); }

    // Safe parse
    public Integer tryParseInt(String s){
        try { return Integer.parseInt(s.trim()); } catch(Exception e){ return null; }
    }
}
