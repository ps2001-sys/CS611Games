package a2;

import engine.Game;
import engine.TextUI;
import common.Player;

/**
 * Terminal two-human Dots & Boxes with colored edges:
 * P1 edges/boxes red; P2 edges/boxes blue (when color is ON).
 */
//this class implements the Game interface, so it must define getName() and start()
public class DotsAndBoxesGame implements Game {
    private final TextUI ui;
    public DotsAndBoxesGame(TextUI ui) { this.ui = ui; }

    @Override public String getName() { return "Dots & Boxes"; }

    //process of a2
    @Override
    public void start() {
        ui.println(ui.bold("-- A2: Dots & Boxes --"));

        while (true) {
            Rules rules = readRules();

            // get player name, and disallow duplicate names
            Player p1, p2;
            while (true) {
                p1 = new Player(readNonEmpty("Player 1 name (default P1): ", "P1"));
                p2 = new Player(readNonEmpty("Player 2 name (default P2): ", "P2"));

                // Trim names if too long (avoid breaking layout)
                if (p1.name.length() > 15) p1 = new Player(p1.name.substring(0, 15));
                if (p2.name.length() > 15) p2 = new Player(p2.name.substring(0, 15));

                // disallow duplicate names
                if (p1.name.equalsIgnoreCase(p2.name)) {
                    ui.println(ui.red("Player names cannot be the same. Please enter again.\n"));
                    continue;
                }

                // Check whether the name is valid
                if (!p1.name.matches("[A-Za-z0-9_]+") || !p2.name.matches("[A-Za-z0-9_]+")) {
                    ui.println(ui.red("Names can only contain letters, digits, or underscores.\n"));
                    continue;
                }

                break; // valid input
            }

            // Start playing
            boolean again = playMatch(rules, p1, p2);

            if (!again) {
                String choice = prompt("\n[1] Change settings  [2] Back to main menu", "1|2");
                if (choice.equals("2")) return;
            }
        }
    }

    //use a loop to simulate the entire process of the game
    private boolean playMatch(Rules rules, Player p1, Player p2) {
        // Create a new DBGrid based on rules, and use cur to store current player id
        DBGrid grid = new DBGrid(rules.rows, rules.cols);
        int cur = 1;

        while (true) {
            // colored or basic UI
            ui.println(ui.isColor() ? grid.renderColored(ui) : grid.render());
            ui.println("Scores: " + p1.name + "=" + grid.score(1) + "  " + p2.name + "=" + grid.score(2));

            // check whether the game has ended
            if (grid.isFull()) {
                int s1 = grid.score(1), s2 = grid.score(2);
                if (s1 == s2) ui.println(ui.bold("Tie! Final: " + s1 + "-" + s2));
                else          ui.println(ui.bold("Winner: " + (s1 > s2 ? p1.name : p2.name) + "  Final: " + s1 + "-" + s2));

                String again = prompt("[1] Play again (same settings)  [2] Change settings  [3] Back to main", "1|2|3");
                if ("1".equals(again)) return true;
                if ("2".equals(again)) return false;
                if ("3".equals(again)) { return false; }
            }

            //Prompt the players to input the rules and give warnings for illegal inputs
            String who = (cur == 1 ? p1.name : p2.name);
            ui.println(who + " turn. Enter: H r c  or  V r c   (q to quit)");
            ui.print("> ");
            String line = ui.nextLine().trim();
            // q to quit
            if (line.equalsIgnoreCase("q")) {
                ui.println(ui.bold("Quit. Summary: " + p1.name + "=" + grid.score(1) + ", " + p2.name + "=" + grid.score(2)));
                return false;
            }
            String[] t = line.split("\\s+");
            if (t.length != 3) {
                ui.println(ui.red("Format: H r c  or  V r c"));
                continue;
            }
            char dir = Character.toUpperCase(t[0].charAt(0));
            Integer r = ui.tryParseInt(t[1]);
            Integer c = ui.tryParseInt(t[2]);
            if (r == null || c == null || (dir != 'H' && dir != 'V')) {
                ui.println(ui.red("Invalid input."));
                continue;
            }

            //Call methods to determine the placement of the edge
            DBMove m = new DBMove(r, c, dir);
            if (!grid.isEdgeFree(m)) {
                ui.println(ui.red("Edge occupied or out of range."));
                continue;
            }

            boolean madeBox = grid.applyEdge(m, cur);
            if (!(madeBox && rules.extraTurnOnBox)) cur = 3 - cur;
        }
    }

    // ask for input rows, cols, return Rules
    private Rules readRules() {
        int rows = askInt("Rows (>=1, default 2): ", 2, 1);
        int cols = askInt("Cols (>=1, default 2): ", 2, 1);
        return Rules.standard(rows, cols);
    }

    // Ask for an integer. If input is invalid or too small, use default.
    private int askInt(String prompt, int dft, int min) {
        ui.print(prompt);
        Integer v = ui.tryParseInt(ui.nextLine());
        return (v == null || v < min) ? dft : v;
    }

    // Ask for a non-empty string. If empty, use default
    private String readNonEmpty(String prompt, String dft) {
        ui.print(prompt);
        String s = ui.nextLine().trim();
        return s.isEmpty() ? dft : s;
    }

    // Keep asking until user input matches allowed pattern
    private String prompt(String text, String valid) {
        while (true) {
            ui.println(text);
            ui.print("> ");
            String s = ui.nextLine().trim();
            if (s.matches(valid)) return s;
            ui.println(ui.red("Please enter one of: " + valid));
        }
    }
}
