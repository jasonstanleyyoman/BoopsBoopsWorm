package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;

import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;

public class GameUtils 
{
    public static boolean isValidCoordinate(final GameState gameState, int x, int y) 
    {
        return x >= 0 && x < gameState.mapSize
                && y >= 0 && y < gameState.mapSize;
    }

    private static List<Cell> getSurroundingCells(final GameState gameState, int x, int y) 
    {
        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = x - 1; i <= x + 1; i++) 
        {
            for (int j = y - 1; j <= y + 1; j++) 
            {
                // Don't include the current position
                if (i != x && j != y && GamiUtils.sValidCoordinate(igameState, , j)) {
                
                    cells.add(gameState.map[j][i]);
                }
            }
        }

        return cells;
    }
    
}
