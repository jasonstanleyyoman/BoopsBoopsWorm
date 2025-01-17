package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Technologist;
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
                if (!(i == x && y == j)) {
                    if (GameUtils.isValidCoordinate(i, j)) {
                        cells.add(gameState.map[j][i]);
                    }
                }
            }
        }

        return cells;
    }

    public static List<List<Cell>> constructFireDirectionLines(Position cell, int range) {
        return constructFireDirectionLines(cell, range, false);
    }

    public static List<List<Cell>> constructFireDirectionLines(Position cell, int range, boolean considerDirt) {
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
                if (targetCell.type == CellType.DEEP_SPACE) {
                    break;
                }

                if (considerDirt && targetCell.type == CellType.DIRT) {
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

    public static Cell nextLine(Position now, Position end) {
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

    public static List<List<Cell>> getBananaBombRange() {
        GameState gameState = Bot.getGameState();
        Agent ourAgent = gameState.myPlayer.getAgent();

        return constructFireDirectionLines(new Cell(ourAgent.position.x, ourAgent.position.y),
                ourAgent.bananaBomb.range);

    }

    public static List<List<Cell>> getFreezerRange() {
        GameState gameState = Bot.getGameState();
        Technologist ourTechnologist = gameState.myPlayer.getTechnologist();

        return constructFireDirectionLines(new Cell(ourTechnologist.position.x, ourTechnologist.position.y),
                ourTechnologist.snowballs.range);

    }

	public static List<Cell> getShootingArea(Cell center, int radius) {
	    List<Cell> cellShootingArea = new ArrayList<>();
	    cellShootingArea.add(center);
	    for (int i = center.x - radius; i <= center.x + radius; i++) {
	        for (int j = center.y - radius; j <= center.y + radius; j++) {
	            Cell now = GameUtils.lookup(i, j);
	            if (now == null)
	                continue;
	            if (Math.round(realEuclideanDistance(i, j, center.x, center.y)) <= radius
	                    && now.type != CellType.DEEP_SPACE && !center.equals(now)) {
	                cellShootingArea.add(now);
	            }
	        }
	    }
	
	    return cellShootingArea;
	}

	public static List<Cell> getBananaArea(Cell center) {
	    List<Cell> shootingArea = getShootingArea(center, 1);
	    Cell northCell = GameUtils.lookup(center.x, center.y - 2);
	    if (northCell != null && northCell.type != CellType.DEEP_SPACE) {
	        shootingArea.add(northCell);
	    }
	
	    Cell southCell = GameUtils.lookup(center.x, center.y + 2);
	    if (southCell != null && southCell.type != CellType.DEEP_SPACE) {
	        shootingArea.add(southCell);
	    }
	
	    Cell westCell = GameUtils.lookup(center.x - 2, center.y);
	    if (westCell != null && westCell.type != CellType.DEEP_SPACE) {
	        shootingArea.add(westCell);
	    }
	
	    Cell eastCell = GameUtils.lookup(center.x + 2, center.y);
	    if (eastCell != null && eastCell.type != CellType.DEEP_SPACE) {
	        shootingArea.add(eastCell);
	    }
	    return shootingArea;
	}

}