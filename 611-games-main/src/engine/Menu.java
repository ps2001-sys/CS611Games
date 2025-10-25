package engine;

import a1.SlidingPuzzleGame;
import a2.DotsAndBoxesGame;
import a3.QuoridorGame;

/**
 * Main menu for the CS611 Games project.
 * Provides a unified interface for selecting between three different turn-based games:
 * - Sliding Puzzle (Assignment 1)
 * - Dots & Boxes (Assignment 2)
 * - Quoridor (Assignment 3)
 *
 * The menu also allows toggling color display for enhanced visual feedback.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 * Modified: 2025-01-06 - Added Quoridor game option
 */
public class Menu {
    private final TextUI ui = new TextUI();

    /**
     * Starts the menu and handles user choices until they quit.
     * Demonstrates the extensibility of our game framework by seamlessly
     * integrating multiple turn-based games.
     */
    public void start() {
        ui.println(ui.bold("╔════════════════════════════════════╗"));
        ui.println(ui.bold("║         CS611 GAME SUITE           ║"));
        ui.println(ui.bold("║    Turn-Based Strategy Games       ║"));
        ui.println(ui.bold("╚════════════════════════════════════╝"));
        ui.println("\nWelcome! This suite contains three turn-based games.");
        ui.println("Each game demonstrates different aspects of our extensible framework.\n");

        while (true) {
            ui.println("\n" + ui.cyan("═══ Main Menu ═══"));
            ui.println("  [1] Play Sliding Puzzle (A1) - Single Player");
            ui.println("  [2] Play Dots & Boxes (A2)   - 2 Players");
            ui.println("  [3] Play Quoridor (A3)       - 2-4 Players");
            ui.println("  [4] Toggle Color Display     - Currently " +
                    (ui.isColor() ? ui.green("ON") : ui.red("OFF")));
            ui.println("  [5] View Global Statistics");
            ui.println("  [q] Quit");
            ui.print("\n> ");

            String choice = ui.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    ui.println("\nLaunching Sliding Puzzle...\n");
                    new SlidingPuzzleGame(ui).start();
                    break;

                case "2":
                    ui.println("\nLaunching Dots & Boxes...\n");
                    new DotsAndBoxesGame(ui).start();
                    break;

                case "3":
                    ui.println("\nLaunching Quoridor...\n");
                    new QuoridorGame(ui).start();
                    break;

                case "4":
                    boolean newColorSetting = !ui.isColor();
                    ui.setColor(newColorSetting);
                    ui.println("\n" + ui.bold("Color display is now " +
                            (newColorSetting ? ui.green("ENABLED") : ui.yellow("DISABLED")) + "."));
                    if (newColorSetting) {
                        ui.println("Colors will enhance game visibility and player distinction.");
                    } else {
                        ui.println("Plain text mode activated for better compatibility.");
                    }
                    break;

                case "5":
                    ui.println("\n" + ui.bold("Global Statistics"));
                    ui.println("Feature coming soon - will display aggregate stats across all games.");
                    break;

                case "q":
                case "quit":
                case "exit":
                    ui.println("\n" + ui.bold("Thank you for playing!"));
                    ui.println("Your game statistics have been saved.");
                    ui.println("Goodbye!\n");
                    return;

                default:
                    ui.println(ui.red("\n✗ Invalid choice. Please select 1, 2, 3, 4, 5, or q."));
            }
        }
    }
}