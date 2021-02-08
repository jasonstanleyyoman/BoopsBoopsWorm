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
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Direction;

public class GameUtils {
    public static boolean isValidCoordinate(int x, int y) {
        GameState gameState = Bot.getGameState();
        return x >= 0 && x < gameState.mapSize && y >= 0 && y < gameState.mapSize;
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

    public static List<List<Cell>> constructFireDirectionLines(int range) {
        GameState gameState = Bot.getGameState();
        MyWorm currentWorm = WormUtils.getCurrentWorm();
        List<List<Cell>> directionLines = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            List<Cell> directionLine = new ArrayList<>();
            for (int directionMultiplier = 1; directionMultiplier <= range; directionMultiplier++) {
                int coordinateX = currentWorm.position.x + (directionMultiplier * direction.x);
                int coordinateY = currentWorm.position.y + (directionMultiplier * direction.y);

                if (!GameUtils.isValidCoordinate(coordinateX, coordinateY)) {
                    break;
                }

                if (PlaneUtils.euclideanDistance(currentWorm.position.x, currentWorm.position.y, coordinateX,
                        coordinateY) > range) {
                    break;
                }

                Cell cell = gameState.map[coordinateY][coordinateX];
                if (cell.type != CellType.AIR) {
                    break;
                }

                directionLine.add(cell);
            }
            directionLines.add(directionLine);
        }

        return directionLines;
    }

    public static Worm getFirstWormInRange(final GameState gameState, final MyWorm currentWorm,
            final Opponent opponent) {
        Set<String> cells = GameUtils.constructFireDirectionLines(gameState, currentWorm, currentWorm.weapon.range)
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

    public static List<Position> generateLine(Position starting, Position end) {
        List<Position> positionList = new ArrayList<>();
        int gradient = 2 * (end.y - starting.y);
        int gradientError = gradient - (end.x - starting.x);
        for (int x = starting.x, y = starting.y; x <= end.x; x++) {
            positionList.add(new Position(x, y));
            gradientError += gradient;

            if ()

        }
    }

}
