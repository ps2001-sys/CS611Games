package engine;

import a1.SlidingPuzzleGame;
import a2.DotsAndBoxesGame;

/**
 * Top-level menu for the CS611 Games project.
 * Allows users to select between games and toggle color display.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-01-05
 */
public class Menu {
    private final TextUI ui = new TextUI();

    /**
     * Start the interactive menu system.
     * Displays options and processes user choices.
     */
    public void start() {
        ui.println(ui.bold("-- CS611 Games --"));

        while (true) {
            ui.println("\nChoose: [1] A1 Sliding Puzzle  [2] A2 Dots&Boxes  [3] Toggle Color  [q] Quit");
            ui.print("> ");
            String choice = ui.nextLine().trim();

            if (choice.equalsIgnoreCase("q")) {
                ui.println("Bye!");
                return;
            }

            switch (choice) {
                case "1":
                    new SlidingPuzzleGame(ui).start();
                    break;

                case "2":
                    new DotsAndBoxesGame(ui).start();
                    break;

                case "3":
                    ui.setColor(!ui.isColor());
                    ui.println("Color: " + (ui.isColor() ? "ON" : "OFF"));
                    break;

                default:
                    ui.println(ui.red("Invalid choice."));
            }
        }
    }
}