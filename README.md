# CS611-Assignment < #3 >
## < QUORIDOR >
---------------------------------------------------------------------------
- Name: Zhuojun Lyu
- Email: lzj2729@bu.edu
- Student ID: U06761622
- Name: Priyanshu Singh
- Email: ps2001@bu.edu
- Student ID: U73441029
## Files
---------------------------------------------------------------------------
The main source files included in this project are:
QuoridorGame.java : This is the main controller of the Quoridor game. It handles player turns,
input validation, move and wall logic, and the game loop. It also integrates global statistics and
ensures a consistent user interface experience.
QuoridorBoard.java : Represents the game board structure. It manages pawn and wall
placements, board rendering, and wall blocking logic.
QuoridorRules.java : Contains all movement and rule validation logic. It ensures that each
move follows the official Quoridor game rules, including jump and diagonal move cases.
Pawn.java : Defines the pawn object for each player, including its identity and position tracking
on the board.
Position.java : Represents a coordinate pair (row, column) on the board, used by both pawns
and walls for movement and placement.
WallPiece.java : Describes the wall object, including its position and orientation (horizontal or
vertical)
## Notes
---------------------------------------------------------------------------
1.The program supports both 2-player and 4-player modes, with automatic pawn placement and
individual wall limits
2.Implemented a custom game loop so that invalid actions (illegal moves or wall placements) do
not skip the player’s turn
3.Integrated global statistics tracking, recording each player’s total games, wins, average
moves, and average time
4.Added color-enhanced terminal display to make turns, errors, and results more readable and
engaging
5.Carefully designed rule validation logic in QuoridorRules.java, including jump-over and
diagonal move handling when pawns face each other
6.The design ensures compatibility with previous assignments(A1 and A2) without breaking
shared components such as Game, Player, and TextUI
## How to compile and run
---------------------------------------------------------------------------
Your directions on how to run the code. Make sure to be as thorough as possible!
1. Open a terminal and navigate to the src directory of the project
2. Compile the code using:
 javac -encoding UTF-8 Main.java
3. Run the program with:
java Main
4. From the main menu, select option [3] Play Quoridor (A3)
5. Use M <dir> to move (N/S/E/W).
Use W <r> <c> <H/V> to place a wall.
Use H for help or Q to quit.
## Input/Output Example
---------------------------------------------------------------------------
>>
╔════════════════════════╗
║ CS611 GAME SUITE ║
║ Turn-Based Strategy Games ║
╚════════════════════════╝
Welcome! This suite packs three different turn-based games.
Each highlights how awesome and flexible our game framework is.
══ Main Menu ══
 [1] Play Sliding Puzzle (A1) - Single Player
 [2] Play Dots & Boxes (A2) - 2 Players
 [3] Play Quoridor (A3) - 2-4 Players
 [4] Toggle Color Display - Currently ON
 [5] View Global Statistics
 [q] Quit
> 3
Launching Quoridor...
=== Quoridor ===
Number of players (2 or 4):
> 4
Setting up 4 players for Quoridor...
Player 1 name (default P1): 1
Player 2 name (default P2):
Player 3 name (default P3):
Player 4 name (default P4):
Game Started!
Each player gets 5 walls.
Goal: Be first to reach the opposite side!
Type 'h' for help at any time.
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
1 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 0
 P2 (P2): Walls: 5, Moves: 0
 P3 (P3): Walls: 5, Moves: 0
 P4 (P4): Walls: 5, Moves: 0
1's turn (Player 1)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> M s
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 0
 P3 (P3): Walls: 5, Moves: 0
 P4 (P4): Walls: 5, Moves: 0
P2's turn (Player 2)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> m n
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | | 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 1
 P3 (P3): Walls: 5, Moves: 0
 P4 (P4): Walls: 5, Moves: 0
P3's turn (Player 3)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> w 0 5 h
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+══+══+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | | 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 1
 P3 (P3): Walls: 4, Moves: 1
 P4 (P4): Walls: 5, Moves: 0
P4's turn (Player 4)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> wall 6 3 v
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+══+══+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | ║ | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | ║ 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 1
 P3 (P3): Walls: 4, Moves: 1
 P4 (P4): Walls: 4, Moves: 1
1's turn (Player 1)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> asdafdaf
Invalid command. Use M for move or W for wall (or H/Q).
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+══+══+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | ║ | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | ║ 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 1
 P3 (P3): Walls: 4, Moves: 1
 P4 (P4): Walls: 4, Moves: 1
1's turn (Player 1)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> h
=== Quoridor Help ===
OBJECTIVE: Be the first to reach the opposite side of the board.
COMMANDS:
 M <dir> - Move pawn (N/S/E/W)
 W <r> <c> <o> - Place wall at row r, column c (o = H/V)
 H - Show this help
 Q - Quit game
RULES:
- Pawns move one space orthogonally
- Jump over adjacent opponents; if blocked, move diagonally as per rules
- Walls are 2 segments long; cannot overlap or fully block all paths
- Each player starts with 10 walls (2 players) or 5 walls (4 players)
VICTORY (4 players):
- P1 (North) reaches South edge
- P2 (South) reaches North edge
- P3 (West) reaches East edge
- P4 (East) reaches West edge
 0 1 2 3 4 5 6 7 8
 +----+----+----+----+----+----+----+----+----+
0 | | | | | | | | | |
 +----+----+----+----+----+══+══+----+----+
1 | | | | | 1 | | | | |
 +----+----+----+----+----+----+----+----+----+
2 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
3 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
4 | 3 | | | | | | | | 4 |
 +----+----+----+----+----+----+----+----+----+
5 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
6 | | | | ║ | | | | |
 +----+----+----+----+----+----+----+----+----+
7 | | | | ║ 2 | | | | |
 +----+----+----+----+----+----+----+----+----+
8 | | | | | | | | | |
 +----+----+----+----+----+----+----+----+----+
Game Status:
 1 (P1): Walls: 5, Moves: 1
 P2 (P2): Walls: 5, Moves: 1
 P3 (P3): Walls: 4, Moves: 1
 P4 (P4): Walls: 4, Moves: 1
1's turn (Player 1)
Walls remaining: 5
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> q
Game ended.
Final Statistics:
1 - Games: 1, Wins: 0, Win Rate: 0.0%, Avg Time: 48.01s
P2 - Games: 1, Wins: 0, Win Rate: 0.0%, Avg Time: 48.01s
P3 - Games: 1, Wins: 0, Win Rate: 0.0%, Avg Time: 48.01s
P4 - Games: 1, Wins: 0, Win Rate: 0.0%, Avg Time: 48.01s
══ Main Menu ══
 [1] Play Sliding Puzzle (A1) - Single Player
 [2] Play Dots & Boxes (A2) - 2 Players
 [3] Play Quoridor (A3) - 2-4 Players
 [4] Toggle Color Display - Currently ON
 [5] View Global Statistics
 [q] Quit
> q
Thanks for playing!
Your progress and stats are safely saved.
See you next time!
Alice wins!
Victory in 12 actions!
Final Statistics:
Alice - Games: 1, Wins: 1, Win Rate: 100%, Avg Moves: 12.00
Bob - Games: 1, Wins: 0, Win Rate: 0%, Avg Moves: 10.00
