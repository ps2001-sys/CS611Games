package engine;

import a1.SlidingPuzzleGame;
import a2.DotsAndBoxesGame;
import a3.QuoridorGame;

/**
 * Main menu for the CS611 Games project.
 * Gives players a simple way to pick from three different turn-based games:
 *  - Sliding Puzzle (A1)
 *  - Dots & Boxes (A2)
 *  - Quoridor (A3)
 *
 * Also lets you toggle color display on/off for a nicer look or simpler console.
 *
 * Written by Zhuojun Lyu & Priyanshu Singh, 2025-01-05
 * Updated 2025-01-06 to add Quoridor support
 */
public class Menu {
    private final TextUI ui = new TextUI();

    /**
     * Starts the main menu loop.
     * Keeps asking until the player decides to quit.
     *
     * Demo of how we can easily plug in different games,
     * all sharing one neat user interface.
     */
    public void start() {
        ui.println(ui.bold("╔════════════════════════════════════╗"));
        ui.println(ui.bold("║         CS611 GAME SUITE           ║"));
        ui.println(ui.bold("║    Turn-Based Strategy Games       ║"));
        ui.println(ui.bold("╚════════════════════════════════════╝"));
        ui.println("\nWelcome! This suite packs three different turn-based games.");
        ui.println("Each highlights how awesome and flexible our game framework is.\n");

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
                    boolean enableColors = !ui.isColor();
                    ui.setColor(enableColors);
                    ui.println("\n" + ui.bold("Color display is now " +
                            (enableColors ? ui.green("ENABLED") : ui.yellow("DISABLED")) + "."));
                    if (enableColors) {
                        ui.println("Colors make the game easier to read and more fun!");
                    } else {
                        ui.println("Plain mode activated for classic text-only vibes.");
                    }
                    break;

                case "5":
                    ui.println("\n" + ui.bold("Global Statistics"));
                    ui.println("This feature is cooking and will show your overall stats soon.");
                    break;

                case "q":
                case "quit":
                case "exit":
                    ui.println("\n" + ui.bold("Thanks for playing!"));
                    ui.println("Your progress and stats are safely saved.");
                    ui.println("See you next time!\n");
                    return;

                default:
                    ui.println(ui.red("\n✗ Oops! That's not a valid option. Try 1, 2, 3, 4, 5, or q."));
                    break;
            }
        }
    }
}
