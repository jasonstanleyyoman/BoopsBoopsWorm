package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Opponent;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Direction;

public class PlaneUtils {
    public static int euclideanDistance(int aX, int aY, int bX, int bY) {
        return (int) (Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));
    }

    public static Direction resolveDirection(Position a, Position b) {
        StringBuilder builder = new StringBuilder();

        int verticalComponent = b.y - a.y;
        int horizontalComponent = b.x - a.x;

        if (verticalComponent < 0) {
            builder.append('N');
        } else if (verticalComponent > 0) {
            builder.append('S');
        }

        if (horizontalComponent < 0) {
            builder.append('W');
        } else if (horizontalComponent > 0) {
            builder.append('E');
        }

        return Direction.valueOf(builder.toString());
    }

    public static Worm getFirstWormInRange(final Opponent opponent) {
        MyWorm currentWorm = WormUtils.getCurrentWorm();
        Set<String> cells = PlaneUtils
                .constructFireDirectionLines(new Cell(currentWorm.position.x, currentWorm.position.y),
                        currentWorm.weapon.range)
                .stream().flatMap(Collection::stream).map(cell -> String.format("%d_%d", cell.x, cell.y))
                .collect(Collectors.toSet());

        for (Worm enemyWorm : opponent.worms) {
            String enemyPosition = String.format("%d_%d", enemyWorm.position.x, enemyWorm.position.y);
            if (cells.contains(enemyPosition)) {
                return enemyWorm;
            }
        }

        return null;
    }

    public static List<Cell> getSurroundingCells(int x, int y) {
        GameState gameState = Bot.getGameState();
        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // Don't include the current position
                if (i != x && j != y && GameUtils.isValidCoordinate(i, j)) {
                    cells.add(gameState.map[j][i]);
                }
            }
        }

        return cells;
    }

    public static List<List<Cell>> constructFireDirectionLines(Cell cell, int range) {
        GameState gameState = Bot.getGameState();
        List<List<Cell>> directionLines = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            List<Cell> directionLine = new ArrayList<>();
            for (int directionMultiplier = 1; directionMultiplier <= range; directionMultiplier++) {
                int coordinateX = cell.x + (directionMultiplier * direction.x);
                int coordinateY = cell.y + (directionMultiplier * direction.y);

                if (!GameUtils.isValidCoordinate(coordinateX, coordinateY)) {
                    break;
                }

                if (PlaneUtils.euclideanDistance(cell.x, cell.y, coordinateX, coordinateY) > range) {
                    break;
                }

                Cell targetCell = gameState.map[coordinateY][coordinateX];
                if (targetCell.type != CellType.AIR) {
                    break;
                }

                directionLine.add(targetCell);
            }
            directionLines.add(directionLine);
        }

        return directionLines;
    }

    public static List<Cell> generateLine(Cell starting, Cell end) {
        List<Cell> positionList = new ArrayList<>();
        int gradient = 2 * (end.y - starting.y);
        int gradientError = gradient - (end.x - starting.x);
        for (int x = starting.x, y = starting.y; x <= end.x; x++) {
            positionList.add(new Cell(x, y));
            gradientError += gradient;

            if (gradientError >= 0) {
                y += 1;
                gradientError -= 2 * (end.x - starting.x);
            }

        }
        return positionList;
    }

    public static int lineLength(Cell starting, Cell end) {
        return PlaneUtils.generateLine(starting, end).size();
    }

}
