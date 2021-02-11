package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Profession;

public class WormUtils {

    public static MyWorm getCurrentWorm() {
        GameState gameState = Bot.getGameState();
        return Bot.getMyWormList().stream().filter(myWorm -> myWorm.id == gameState.currentWormId).findFirst().get();
    }

    public static List<Cell> getShootingArea(Cell center, int radius) {
        GameState gameState = Bot.getGameState();
        List<Cell> cellShootingArea = new ArrayList<>();
        for (int i = center.x - radius; i <= center.x + radius; i++) {
            for (int j = center.y - radius; j <= center.y + radius; j++) {
                if (PlaneUtils.euclideanDistance(i, j, center.x, center.y) > radius
                        && gameState.map[i][j].type != CellType.AIR) {
                    cellShootingArea.add(GameUtils.lookup(i, j));
                }
            }
        }

        return cellShootingArea;
    }

    public static int getWormInRange(Cell center, int radius) {
        List<Cell> possibleCell = getShootingArea(center, radius);
        int opponentWormCount = 0;
        for (int i = 0; i < possibleCell.size(); i++) {
            final int k = i;
            opponentWormCount += Bot.getOpponentWorms().stream().filter(w -> w.position.equals(possibleCell.get(k)))
                    .collect(Collectors.toList()).size();
        }
        return opponentWormCount;
    }

    public static Worm getFirstWormInRange(MyWorm currentWorm) {
        Set<String> cells = PlaneUtils
                .constructFireDirectionLines(GameUtils.lookup(currentWorm.position.x, currentWorm.position.y),
                        currentWorm.weapon.range)
                .stream().flatMap(Collection::stream).map(cell -> String.format("%d_%d", cell.x, cell.y))
                .collect(Collectors.toSet());

        for (Worm enemyWorm : Bot.getOpponentWorms()) {
            String enemyPosition = String.format("%d_%d", enemyWorm.position.x, enemyWorm.position.y);
            if (cells.contains(enemyPosition)) {
                return enemyWorm;
            }
        }

        return null;
    }

    /**
     * Return worm (commando atau technologist) yang paling deket sama agent musuh
     */
    public static MyWorm targetBanana() {
        Commando commando = Bot.getGameState().myPlayer.getCommando();
        Technologist technologist = Bot.getGameState().myPlayer.getTechnologist();
        try {
            Worm enemyAgent = Arrays.asList(Bot.getOpponent().worms).stream()
                    .filter(w -> w.profession.equals(Profession.AGENT)).findFirst().get();
            // disini
            double distanceFromCommandoToAgent = PlaneUtils.realEuclideanDistance(commando.position,
                    enemyAgent.position);
            double distanceFromTechnologistToAgent = PlaneUtils.realEuclideanDistance(technologist.position,
                    enemyAgent.position);
            if (distanceFromCommandoToAgent >= distanceFromTechnologistToAgent) {
                return commando;
            } else {
                return technologist;
            }

        } catch (NoSuchElementException | NullPointerException ignored) {
        }
        return null;
    }

    public static Worm getFurthestWorm(List<MyWorm> wormList, Position position) {
        double distance = PlaneUtils.realEuclideanDistance(wormList.get(0).position, position);
        Worm currentWorm = wormList.remove(0);
        for (Worm w : wormList) {
            if (PlaneUtils.realEuclideanDistance(w.position, position) > distance) {
                currentWorm = w;
                distance = PlaneUtils.realEuclideanDistance(w.position, position);
            }
        }
        return currentWorm;
    }
}
