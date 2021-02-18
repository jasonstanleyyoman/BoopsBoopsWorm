package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import java.lang.Math;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.enums.Profession;

public class WormUtils {

    public static MyWorm getCurrentWorm() {
        GameState gameState = Bot.getGameState();
        return Bot.getMyWormList().stream().filter(myWorm -> myWorm.id == gameState.currentWormId).findFirst().get();
    }

    public static List<Cell> getShootingArea(Cell center, int radius) {
        List<Cell> cellShootingArea = new ArrayList<>();
        cellShootingArea.add(center);
        for (int i = center.x - radius; i <= center.x + radius; i++) {
            for (int j = center.y - radius; j <= center.y + radius; j++) {
                Cell now = GameUtils.lookup(i, j);
                if (now == null)
                    continue;
                if (Math.round(PlaneUtils.realEuclideanDistance(i, j, center.x, center.y)) <= radius
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
     * Return worm yang paling deket sama Technologist musuh
     */
    public static MyWorm targetTechnologist() {
        Commando commando = Bot.getGameState().myPlayer.getCommando();
        Technologist technologist = Bot.getGameState().myPlayer.getTechnologist();
        Agent agent = Bot.getGameState().myPlayer.getAgent();
        try {
            Worm enemyTechnologist = Arrays.asList(Bot.getOpponent().worms).stream()
                    .filter(w -> w.profession.equals(Profession.TECHNOLOGIST)).findFirst().get();
            // disini
            double disCtoT = PlaneUtils.realEuclideanDistance(commando.position, enemyTechnologist.position);
            double disAtoT = PlaneUtils.realEuclideanDistance(agent.position, enemyTechnologist.position);
            double disTtoT = PlaneUtils.realEuclideanDistance(technologist.position, enemyTechnologist.position);
            if (disCtoT >= disAtoT & disCtoT >= disTtoT) {
                return commando;
            } else if (disTtoT >= disCtoT & disTtoT >= disAtoT) {
                return technologist;
            } else {
                return agent;
            }

        } catch (NoSuchElementException | NullPointerException ignored) {
        }
        return null;
    }

    public static Worm getEnemy(Profession pr) {
        return Arrays.asList(Bot.getOpponent().worms).stream().filter(w -> w.profession.equals(pr)).findFirst()
                .orElse(new Worm(pr));
    }

    public static boolean isAlive(Worm w) {
        if (w == null)
            return false;
        if (w.id == -999)
            return false;

        return (w.health > 0);
    }
}
