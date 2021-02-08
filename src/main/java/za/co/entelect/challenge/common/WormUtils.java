package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Opponent;
import za.co.entelect.challenge.enums.CellType;

public class WormUtils {

    public static MyWorm getCurrentWorm() {
        GameState gameState = Bot.getGameState();
        return Arrays.stream(gameState.myPlayer.worms).filter(myWorm -> myWorm.id == gameState.currentWormId)
                .findFirst().get();
    }

    public static List<Cell> getShootingArea(Cell center, int radius) {
        GameState gameState = Bot.getGameState();
        List<Cell> cellShootingArea = new ArrayList<>();
        for (int i = center.x - radius; i <= center.x + radius; i++) {
            for (int j = center.y - radius; j <= center.y + radius; j++) {
                if (PlaneUtils.euclideanDistance(i, j, center.x, center.y) > radius
                        && gameState.map[i][j].type != CellType.AIR) {
                    cellShootingArea.add(new Cell(i, j));
                }
            }
        }

        return cellShootingArea;
    }

    public static int getWormInRange(Cell center, int radius) {
        List<Cell> possibleCell = getShootingArea(center, radius);
        int opponentWormCount = 0;
        Opponent opponent = Bot.getOpponent();
        for (int i = 0; i < possibleCell.size(); i++) {
            for (int j = 0; j <= 3; j++) {
                if (opponent.worms[j].position.equals(possibleCell.get(i))) {
                    opponentWormCount += 1;
                }
            }
        }
        return opponentWormCount;
    }
}
