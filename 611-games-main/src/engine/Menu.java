package engine;

import a1.SlidingPuzzleGame;
import a2.DotsAndBoxesGame;

/**
 * Main menu for the CS611 Games project.
 * Lets the user pick a game or toggle color mode.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Menu {
    private final TextUI ui = new TextUI();

    /**
     * Starts the menu and handles user choices until they quit.
     */
    public void start() {
        ui.println(ui.bold("-- CS611 Games --"));

        while (true) {
            ui.println("\nChoose an option:");
            ui.println("  [1] Play Sliding Puzzle (A1)");
            ui.println("  [2] Play Dots & Boxes (A2)");
            ui.println("  [3] Toggle Color Display");
            ui.println("  [q] Quit");
            ui.print("> ");

            String choice = ui.nextLine().trim();

            if (choice.equalsIgnoreCase("q")) {
                ui.println("Goodbye!");
                return;
            }

            if (choice.equals("1")) {
                new SlidingPuzzleGame(ui).start();
            } else if (choice.equals("2")) {
                new DotsAndBoxesGame(ui).start();
            } else if (choice.equals("3")) {
                boolean newColorSetting = !ui.isColor();
                ui.setColor(newColorSetting);
                ui.println("Color is now " + (newColorSetting ? "ON" : "OFF") + ".");
            } else {
                ui.println(ui.red("That’s not a valid choice. Please try again."));
            }
        }
    }
}
