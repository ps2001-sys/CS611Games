/**
 * Main entry point for the CS611 Games project.
 * This class simply launches the main menu; it does not manage any game logic itself.
 *
 * Authors: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class Main {
    public static void main(String[] args) {
        new engine.Menu().start();  // Start the top-level menu
    }
}
