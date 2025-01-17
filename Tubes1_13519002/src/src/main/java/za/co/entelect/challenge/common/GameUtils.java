package za.co.entelect.challenge.common;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.Position;

public class GameUtils {

    public static boolean isValidCoordinate(int x, int y) {
        GameState gameState = Bot.getGameState();
        return x >= 0 && x < gameState.mapSize && y >= 0 && y < gameState.mapSize;
    }

    public static Cell lookup(int x, int y) {
        if (!isValidCoordinate(x, y))
            return null;
        GameState gameState = Bot.getGameState();
        return new Cell(gameState.map[y][x]);
    }

    public static Cell lookup(Position cell) {
        if (!isValidCoordinate(cell.x, cell.y))
            return null;
        GameState gameState = Bot.getGameState();
        return new Cell(gameState.map[cell.y][cell.x]);
    }

}
