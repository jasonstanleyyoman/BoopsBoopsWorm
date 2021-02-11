package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Direction;

public class PlaneUtils {
    public static int euclideanDistance(Position A, Position B) {
        return (int) (Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2)));
    }

    public static double realEuclideanDistance(Position A, Position B) {
        return (Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2)));
    }

    public static int euclideanDistance(int aX, int aY, int bX, int bY) {
        return (int) (Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));
    }

    public static double realEuclideanDistance(int aX, int aY, int bX, int bY) {
        return (Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2)));
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
        Cell now = new Cell(starting);
        List<Cell> positionList = new ArrayList<>();
        while (!now.equals(end)) {
            Cell next = new Cell(nextLine(now, end));
            positionList.add(next);
            now = next;

        }
        return positionList;
    }

    public static Cell nextLine(Cell now, Cell end) {
        List<Cell> sur = getSurroundingCells(now.x, now.y);
        Cell targetMove = sur.remove(0);
        double dist = targetMove.distance(end);
        for (Cell c : sur) {
            double oth = c.distance(end);
            if (oth < dist) {
                dist = oth;
                targetMove = c;
            }
        }
        return targetMove;
    }

    public static int lineLength(Cell starting, Cell end) {
        return PlaneUtils.generateLine(starting, end).size();
    }

}