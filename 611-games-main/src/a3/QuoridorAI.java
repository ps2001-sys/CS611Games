package a3;

import java.util.*;

/**
 * AI Player for Quoridor - Updated to work with refactored board.
 * This demonstrates framework extensibility with AI opponents.
 *
 * Author: Zhuojun Lyu and Priyanshu Singh
 * Date: 2025-10-25
 */
public class QuoridorAI {
    private static final int MAX_DEPTH = 3;
    private static final Random random = new Random();

    public static class Action {
        public enum Type { MOVE, WALL }
        public final Type type;
        public final Position position;
        public final String direction;
        public final WallPiece wall;

        private Action(Type type, Position position, String direction, WallPiece wall) {
            this.type = type;
            this.position = position;
            this.direction = direction;
            this.wall = wall;
        }

        public static Action move(Position to, String direction) {
            return new Action(Type.MOVE, to, direction, null);
        }

        public static Action wall(WallPiece wall) {
            return new Action(Type.WALL, null, null, wall);
        }
    }

    public static Action getBestAction(QuoridorBoard board, QuoridorRules rules,
                                       int playerIndex, int wallsRemaining, int difficulty) {
        if (difficulty == 1) {
            return getRandomAction(board, rules, playerIndex, wallsRemaining);
        } else if (difficulty == 2) {
            return getGreedyAction(board, rules, playerIndex, wallsRemaining);
        } else {
            return getMinimaxAction(board, rules, playerIndex, wallsRemaining);
        }
    }

    private static Action getRandomAction(QuoridorBoard board, QuoridorRules rules,
                                          int playerIndex, int wallsRemaining) {
        List<Action> validActions = getAllValidActions(board, rules, playerIndex, wallsRemaining);
        if (validActions.isEmpty()) {
            return null;
        }
        return validActions.get(random.nextInt(validActions.size()));
    }

    private static Action getGreedyAction(QuoridorBoard board, QuoridorRules rules,
                                          int playerIndex, int wallsRemaining) {
        List<Action> validActions = getAllValidActions(board, rules, playerIndex, wallsRemaining);
        if (validActions.isEmpty()) {
            return null;
        }

        Action bestAction = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Action action : validActions) {
            int distance;
            if (action.type == Action.Type.MOVE) {
                distance = getDistanceToGoal(action.position, playerIndex, board.getSize());
            } else {
                distance = evaluateWallPlacement(board, action.wall, playerIndex);
            }

            if (distance < bestDistance) {
                bestDistance = distance;
                bestAction = action;
            }
        }

        return bestAction;
    }

    private static Action getMinimaxAction(QuoridorBoard board, QuoridorRules rules,
                                           int playerIndex, int wallsRemaining) {
        List<Action> validActions = getAllValidActions(board, rules, playerIndex, wallsRemaining);
        if (validActions.isEmpty()) {
            return null;
        }

        Action bestAction = null;
        int bestScore = Integer.MIN_VALUE;

        for (Action action : validActions) {
            QuoridorBoard newBoard = board.copy();
            applyAction(newBoard, action, playerIndex);

            int score = minimax(newBoard, rules, MAX_DEPTH - 1, false,
                    playerIndex, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (score > bestScore) {
                bestScore = score;
                bestAction = action;
            }
        }

        return bestAction;
    }

    private static int minimax(QuoridorBoard board, QuoridorRules rules, int depth,
                               boolean maximizing, int playerIndex, int alpha, int beta) {
        if (depth == 0 || isGameOver(board, rules)) {
            return evaluateBoard(board, playerIndex);
        }

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            List<Action> actions = getAllValidActions(board, rules, playerIndex, 10);

            for (Action action : actions) {
                QuoridorBoard newBoard = board.copy();
                applyAction(newBoard, action, playerIndex);

                int eval = minimax(newBoard, rules, depth - 1, false,
                        playerIndex, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            int opponentIndex = (playerIndex + 1) % 2;
            List<Action> actions = getAllValidActions(board, rules, opponentIndex, 10);

            for (Action action : actions) {
                QuoridorBoard newBoard = board.copy();
                applyAction(newBoard, action, opponentIndex);

                int eval = minimax(newBoard, rules, depth - 1, true,
                        playerIndex, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    private static List<Action> getAllValidActions(QuoridorBoard board, QuoridorRules rules,
                                                   int playerIndex, int wallsRemaining) {
        List<Action> actions = new ArrayList<>();
        Position currentPos = board.getPlayerPosition(playerIndex);

        // Add all valid moves
        String[] directions = {"N", "S", "E", "W"};
        for (String dir : directions) {
            Position newPos = calculateNewPosition(currentPos, dir, board.getSize());
            if (newPos != null) {
                Position finalPos = rules.validateMove(currentPos, newPos, board, playerIndex);
                if (finalPos != null) {
                    actions.add(Action.move(finalPos, dir));
                }
            }
        }

        // Add valid wall placements (limit for performance)
        if (wallsRemaining > 0 && actions.size() < 20) {
            for (int r = 0; r < Math.min(5, board.getSize() - 1); r++) {
                for (int c = 0; c < Math.min(5, board.getSize() - 1); c++) {
                    WallPiece hWall = new WallPiece(new Position(r, c), 'H');
                    if (rules.canPlaceWall(hWall, board)) {
                        actions.add(Action.wall(hWall));
                        if (actions.size() >= 10) break;
                    }

                    WallPiece vWall = new WallPiece(new Position(r, c), 'V');
                    if (rules.canPlaceWall(vWall, board)) {
                        actions.add(Action.wall(vWall));
                        if (actions.size() >= 10) break;
                    }
                }
            }
        }

        return actions;
    }

    private static Position calculateNewPosition(Position current, String direction, int boardSize) {
        int row = current.row;
        int col = current.col;

        switch (direction) {
            case "N":  row--; break;
            case "S":  row++; break;
            case "E":  col++; break;
            case "W":  col--; break;
            default: return null;
        }

        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            return new Position(row, col);
        }
        return null;
    }

    private static void applyAction(QuoridorBoard board, Action action, int playerIndex) {
        if (action.type == Action.Type.MOVE) {
            board.movePlayer(playerIndex, action.position);
        } else {
            board.placeWall(action.wall);
        }
    }

    private static int evaluateBoard(QuoridorBoard board, int playerIndex) {
        Position playerPos = board.getPlayerPosition(playerIndex);
        Position opponentPos = board.getPlayerPosition((playerIndex + 1) % 2);

        int playerDistance = getDistanceToGoal(playerPos, playerIndex, board.getSize());
        int opponentDistance = getDistanceToGoal(opponentPos, (playerIndex + 1) % 2, board.getSize());

        return opponentDistance - playerDistance;
    }

    private static int getDistanceToGoal(Position pos, int playerIndex, int boardSize) {
        if (playerIndex == 0) {
            return boardSize - 1 - pos.row;
        } else {
            return pos.row;
        }
    }

    private static int evaluateWallPlacement(QuoridorBoard board, WallPiece wall, int playerIndex) {
        Position opponentPos = board.getPlayerPosition((playerIndex + 1) % 2);
        return Math.abs(wall.getPosition().row - opponentPos.row) +
                Math.abs(wall.getPosition().col - opponentPos.col);
    }

    private static boolean isGameOver(QuoridorBoard board, QuoridorRules rules) {
        for (int i = 0; i < 2; i++) {
            Position pos = board.getPlayerPosition(i);
            if (pos != null && rules.hasWon(pos, i, 2)) {
                return true;
            }
        }
        return false;
    }

    public static void demonstrateAI() {
        System.out.println("AI Player is ready for integration!");
        System.out.println("Supports 3 difficulty levels:");
        System.out.println("1. Easy - Random moves");
        System.out.println("2. Medium - Greedy strategy");
        System.out.println("3. Hard - Minimax with alpha-beta pruning");
    }
}