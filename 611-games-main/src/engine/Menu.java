package engine;

import a1.SlidingPuzzleGame;
import a2.DotsAndBoxesGame;

/**
 * Top-level menu for the project.
 * This class is about:
 * 1.Display the menu(colored or not, play game a1 or a2, ...)
 * 2.Exit program
 */
public class Menu {
    private final TextUI ui = new TextUI();

    public void start(){
        ui.println(ui.bold("-- CS611 Games --"));
        while(true){
            ui.println("Choose: [1] A1 Sliding Puzzle  [2] A2 Dots&Boxes  [3] Toggle Color  [q] Quit");
            ui.print("> ");
            String s = ui.nextLine().trim();
            if (s.equalsIgnoreCase("q")) {
                ui.println("Bye!");
                return;
            }
            switch (s){
                case "1":
                    new SlidingPuzzleGame(ui).start();
                    break;
                case "2":
                    new DotsAndBoxesGame(ui).start();
                    break;
                case "3":
                    ui.setColor(!ui.isColor()); // toggle colors
                    ui.println("Color: " + (ui.isColor()?"ON":"OFF"));
                    break;
                default:
                    ui.println(ui.red("Invalid choice."));
            }
        }
    }
}
