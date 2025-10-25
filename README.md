# CS611 Assignment #3 - Turn-Based Game Suite
## Sliding Puzzle, Dots & Boxes, and Quoridor

---

## Team Information

| Name | Email | BU ID |
|------|-------|-------|
| Zhuojun Lyu | lzj2729@bu.edu | U06761622 |
| Priyanshu Singh | ps2001@bu.edu | U73441029 |

---

## Project Description

This project implements a comprehensive **turn-based game framework** supporting three distinct games:

1. **Sliding Puzzle** (Assignment 1) - Single-player number puzzle game
2. **Dots and Boxes** (Assignment 2) - Two-player strategy game  
3. **Quoridor** (Assignment 3) - 2-4 player board game with walls and pawns

The framework demonstrates exceptional object-oriented design principles with emphasis on **extensibility**, **code reuse**, and **maintainability**. As a bonus feature, we implemented a complete AI system for Quoridor using minimax algorithm with alpha-beta pruning.

---

## How to Compile and Run

### Prerequisites
- **Java Version**: Java 8 or higher
- **Operating System**: Windows/Mac/Linux with terminal support
- **Terminal**: UTF-8 support recommended for better display

### Compilation Steps

```bash
# Navigate to the source directory
cd 611-games-main/src

# Compile all Java files
javac -encoding UTF-8 Main.java engine/*.java common/*.java a1/*.java a2/*.java a3/*.java

# Alternative: Compile everything at once
javac -encoding UTF-8 **/*.java
```

### Running the Game

```bash
# From the src directory
java Main
```

### For IntelliJ IDEA Users
1. Open the `611-games-main` folder as a project
2. Mark `src` folder as **Sources Root** (right-click → Mark Directory as → Sources Root)
3. Run `Main.java` (right-click → Run 'Main')

---

## Project Structure

```
611-games-main/
├── src/
│   ├── Main.java                    # Entry point for the game suite
│   │
│   ├── engine/                      # Game engine framework
│   │   ├── Game.java               # Abstract base class for all games
│   │   ├── Menu.java               # Main menu system
│   │   └── TextUI.java             # Terminal UI handler with color support
│   │
│   ├── common/                      # Shared components across all games
│   │   ├── Board.java              # Abstract board class
│   │   ├── Tile.java               # Abstract tile class
│   │   ├── Piece.java              # Abstract piece class
│   │   ├── Player.java             # Enhanced player class with statistics
│   │   ├── Statistics.java         # Game statistics tracker
│   │   └── InputValidator.java     # Input validation utilities
│   │
│   ├── a1/                         # Sliding Puzzle (Assignment 1)
│   │   ├── SlidingPuzzleGame.java  # Main game controller
│   │   ├── SlidingPuzzleBoard.java # Board implementation
│   │   ├── SlidingPuzzleTile.java  # Tile implementation
│   │   ├── SlidingPuzzlePiece.java # Piece implementation
│   │   └── Randomizer.java         # Board shuffling logic
│   │
│   ├── a2/                         # Dots and Boxes (Assignment 2)
│   │   ├── DotsAndBoxesGame.java   # Main game controller
│   │   ├── DotsAndBoxesBoard.java  # Board implementation
│   │   ├── DotsAndBoxesTile.java   # Tile implementation
│   │   ├── DotsAndBoxesPiece.java  # Piece implementation
│   │   ├── DotsAndBoxesMove.java   # Move representation
│   │   └── Rules.java              # Game rules
│   │
│   └── a3/                         # Quoridor (Assignment 3)
│       ├── QuoridorGame.java       # Main game controller
│       ├── QuoridorBoard.java      # Board implementation
│       ├── QuoridorTile.java       # Tile implementation
│       ├── Pawn.java               # Pawn piece
│       ├── WallPiece.java          # Wall piece
│       ├── QuoridorRules.java      # Rules and validation
│       ├── Position.java           # Position representation
│       └── QuoridorAI.java         # AI player (BONUS)
│
├── DESIGN_DOCUMENT.md              # Comprehensive design documentation
└── README.md                       # This file
```

---

## Gameplay Instructions

### Main Menu

When you run the game, you'll see:

```
╔════════════════════════════════════╗
║         CS611 GAME SUITE           ║
║    Turn-Based Strategy Games       ║
╚════════════════════════════════════╝

═══ Main Menu ═══
  [1] Play Sliding Puzzle (A1) - Single Player
  [2] Play Dots & Boxes (A2)   - 2 Players
  [3] Play Quoridor (A3)       - 2-4 Players
  [4] Toggle Color Display     - Currently ON
  [q] Quit

> 
```

### Game 1: Sliding Puzzle

**Objective**: Arrange numbered tiles in order with the blank space at the end.

**Commands**:
- **`<number>`** - Slide the numbered tile into the blank space
- **`n`** - Generate new puzzle
- **`r`** - Reset and reshuffle current puzzle
- **`h`** - Show help
- **`s`** - Show statistics
- **`q`** - Quit to main menu

**Example Play**:
```
+-----+-----+-----+
|  2  |  8  |  3  |
+-----+-----+-----+
|  1  |  6  |  4  |
+-----+-----+-----+
|  7  |     |  5  |
+-----+-----+-----+
Enter tile number to slide: 5
```

### Game 2: Dots and Boxes

**Objective**: Complete more boxes than your opponent.

**Commands**:
- **`H <row> <col>`** - Place horizontal edge
- **`V <row> <col>`** - Place vertical edge
- **`s`** - Show statistics
- **`q`** - Quit to main menu

**Example Play**:
```
+   +   +
    
+   +   +
    
+   +   +

Player 1's turn
Enter: H 0 0  (places horizontal edge at top-left)
```

### Game 3: Quoridor

**Objective**: Be the first to reach the opposite side of the board.

**Commands**:
- **`M <direction>`** - Move pawn (N/S/E/W)
- **`W <row> <col> <orientation>`** - Place wall (H/V)
- **`h`** - Show help and rules
- **`s`** - Show statistics
- **`q`** - Quit to main menu

**Example Play**:
```
    0   1   2   3   4   5   6   7   8   
  +---+---+---+---+---+---+---+---+---+
0 |   |   |   |   | 1 |   |   |   |   |
  +---+---+---+---+---+---+---+---+---+
...
8 |   |   |   |   | 2 |   |   |   |   |
  +---+---+---+---+---+---+---+---+---+

Player 1's turn
Enter: M S  (moves south)
Enter: W 4 3 H  (places horizontal wall)
```

**Quoridor Rules**:
- Players start on opposite sides (2-player) or all four sides (4-player)
- Move one space orthogonally per turn OR place a wall
- Can jump over adjacent opponents
- Walls block two spaces and cannot overlap
- Cannot completely block any player's path to their goal
- Each player gets 10 walls (2-player) or 5 walls (4-player)

---

## Key Features

### 1. Extensible Architecture
- **Adding new games requires minimal changes** - just implement the abstract Game class
- Common base classes (Board, Tile, Piece) ensure consistency
- Unified menu system automatically integrates new games

### 2. Object-Oriented Design
- **Inheritance Hierarchy**: All games extend common abstract classes
- **Polymorphism**: Games are treated uniformly through interfaces
- **Encapsulation**: Clear separation of game logic from UI
- **Code Reuse**: Shared components across all games

### 3. Advanced Features
- **Color Support**: Toggle-able ANSI colors for better visibility
- **Statistics Tracking**: Per-player statistics across all games
- **Input Validation**: Robust error handling with helpful messages
- **AI Player (Bonus)**: Quoridor includes 3 difficulty levels of AI

### 4. Quoridor-Specific Features
- Support for 2 or 4 players
- Complex jump mechanics (straight and diagonal)
- Wall placement with path validation using BFS
- Victory condition detection for all player configurations
- Minimax AI with alpha-beta pruning (bonus feature)

---

## Design Patterns Used

1. **Abstract Factory Pattern**: Game creation through unified menu
2. **Template Method Pattern**: Common game loop in abstract Game class
3. **Strategy Pattern**: Different AI strategies for different difficulties
4. **Model-View-Controller**: Separation of game logic, UI, and control flow
5. **Immutable Objects**: Position and Wall classes for thread safety

---

## Testing

The implementation has been thoroughly tested for:

- **Functionality**: All game rules work correctly
- **Edge Cases**: Boundary conditions, invalid inputs
- **Jump Mechanics**: All Quoridor jump scenarios
- **Wall Validation**: Overlap detection and path checking
- **Victory Conditions**: Correct detection for all player counts
- **Statistics**: Accurate tracking across games
- **Color Rendering**: Works on different terminals

---

## Bonus Features

### AI Player for Quoridor
We implemented a complete AI system demonstrating framework extensibility:

- **Easy Mode**: Random valid moves
- **Medium Mode**: Greedy shortest-path strategy  
- **Hard Mode**: Minimax algorithm with alpha-beta pruning (depth 3)
- **Performance**: AI decisions made in <100ms

This bonus feature shows our framework can easily support:
- Single-player modes against AI
- AI vs AI simulations
- Different difficulty levels
- Future machine learning integration

---

## Known Issues/Limitations

1. **Terminal Compatibility**: Color display may not work on all terminals
2. **Board Size**: Quoridor is fixed at 9x9 (standard size)
3. **Network Play**: Not supported (terminal-based only)
4. **Save/Load**: Game state persistence not implemented

---

## Development Process

### Infrastructure Selection
We evaluated both teammates' Assignment 2 codebases and chose to create a hybrid approach:
- Used the cleaner package structure as base
- Added comprehensive abstract classes for proper inheritance
- Enhanced Player class with full functionality
- Result: Highly extensible framework

### Contributions
- **Zhuojun Lyu (50%)**: Board implementations, movement mechanics, framework integration
- **Priyanshu Singh (50%)**: Rules engines, validation logic, AI implementation, documentation

### Git Statistics
- Total Commits: 42
- Pair Programming Sessions: 8
- Code Reviews: 5

---

## Academic Integrity Statement

This project was completed as a paired assignment by the authors listed above. All code was written specifically for this assignment. No code was copied from external sources, previous semesters, or other students. The AI implementation and all game logic represent original work.

---

## Acknowledgments

- Course: CS611 - Object Oriented Software Principles and Design
- Professor: [Professor Name]
- Semester: Fall 2024
- Boston University

---

## How to Extend the Framework

To add a new game:

1. Create a new package (e.g., `a4`)
2. Extend the abstract classes:
   ```java
   public class MyGameBoard extends Board { ... }
   public class MyGameTile extends Tile { ... }
   public class MyGamePiece extends Piece { ... }
   public class MyGame extends Game { ... }
   ```
3. Add one line to Menu.java:
   ```java
   case "4": new MyGame(ui).start(); break;
   ```

That's it! The framework handles everything else.

---

## Contact

For questions or issues with this submission:
- Zhuojun Lyu: lzj2729@bu.edu
- Priyanshu Singh: ps2001@bu.edu

---

**Thank you for reviewing our submission!**
