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

QuoridorGame.java  : This is the main controller of the Quoridor game. It handles player turns, input validation, move and wall logic, and the game loop. It also integrates global statistics and ensures a consistent user interface experience.

QuoridorBoard.java  : Represents the game board structure. It manages pawn and wall placements, board rendering, and wall blocking logic.

QuoridorRules.java  : Contains all movement and rule validation logic. It ensures that each move follows the official Quoridor game rules, including jump and diagonal move cases.
Pawn.java  : Defines the pawn object for each player, including its identity and position tracking on the board.

Position.java  : Represents a coordinate pair (row, column) on the board, used by both pawns and walls for movement and placement.

WallPiece.java : Describes the wall object, including its position and orientation (horizontal or vertical)


## Notes
---------------------------------------------------------------------------
1.The program supports both 2-player and 4-player modes, with automatic pawn placement and individual wall limits
2.Implemented a custom game loop so that invalid actions (illegal moves or wall placements) do not skip the player’s turn
3.Integrated global statistics tracking, recording each player’s total games, wins, average moves, and average time
4.Added color-enhanced terminal display to make turns, errors, and results more readable and engaging
5.Carefully designed rule validation logic in QuoridorRules.java, including jump-over and diagonal move handling when pawns face each other
6.The design ensures compatibility with previous assignments(A1 and A2) without breaking shared components such as Game, Player, and TextUI



## How to compile and run
---------------------------------------------------------------------------
Your directions on how to run the code. Make sure to be as thorough as possible!
1.	Open a terminal and navigate to the src directory of the project
2.	Compile the code using:
  	javac -encoding UTF-8 Main.java
3.	Run the program with:
java Main
4.	From the main menu, select option [3] Play Quoridor (A3)
5.	Use M <dir> to move (N/S/E/W).
Use W <r> <c> <H/V> to place a wall.
Use H for help or Q to quit.
 

## Input/Output Example
---------------------------------------------------------------------------
=== Quoridor ===
Game Started!
Each player gets 10 walls.
Goal: Be first to reach the opposite side!

→ Alice's turn (Player 1)
Walls remaining: 10
Enter: [M]ove <dir>, [W]all <r> <c> <H/V>, [H]elp, [Q]uit
> M S
Move successful!

→ Bob's turn (Player 2)
Walls remaining: 10
> W 3 4 H
Wall placed successfully!

...
🎉 Alice wins! 🎉
Victory in 12 actions!

Final Statistics:
Alice - Games: 1, Wins: 1, Win Rate: 100%, Avg Moves: 12.00
Bob - Games: 1, Wins: 0, Win Rate: 0%, Avg Moves: 10.00
 
 

 
 

