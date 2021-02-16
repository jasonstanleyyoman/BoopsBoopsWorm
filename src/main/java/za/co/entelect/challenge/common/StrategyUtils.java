package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.enums.Profession;

public class StrategyUtils {

    public static Worm setTargetWorm() {
        // Initializing enemy's worms
        Worm enemyAgent = WormUtils.getEnemy(Profession.AGENT);
        Worm enemyTechno = WormUtils.getEnemy(Profession.TECHNOLOGIST);
        Worm enemyCommando = WormUtils.getEnemy(Profession.COMMANDO);

        if (WormUtils.isAlive(enemyAgent)) {
            return enemyAgent;
        } else if (WormUtils.isAlive(enemyCommando)) {
            return enemyCommando;
        } else {
            return enemyTechno;
        }
    }

    public static Cell agentCanShootTwoWorms() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }

        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 2) {
            return null;
        }
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));
        int enemyEffected = 0;
        int friendEffected = 10;
        Cell cell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            for (Cell shoot : shootingRange) {
                int curEnemyEffected = 0;
                int curFriendEffected = 0;
                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm)) {
                        curEnemyEffected += 1;
                    }
                }
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }
                if (curEnemyEffected > enemyEffected && curEnemyEffected > 2 && curFriendEffected <= friendEffected) {
                    friendEffected = curFriendEffected;
                    enemyEffected = curEnemyEffected;
                    cell = banana;
                }
            }
        }
        if (enemyEffected >= 2) {
            return cell;
        }
        return null;
    }

    public static Cell technologistCanShootTwoWorms() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getTechnologist())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getTechnologist().snowballs.count <= 2) {
            return null;
        }
        List<Cell> freezeRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeRange.addAll(l));
        int enemyEffected = 0;
        int friendEffected = 10;
        Cell cell = new Cell(0, 0);
        for (Cell freeze : freezeRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(freeze, 1);
            for (Cell shoot : shootingRange) {
                int curEnemyEffected = 0;
                int curFriendEffected = 0;
                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot)) {
                        curEnemyEffected += 1;
                    }
                }
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot)) {
                        curFriendEffected += 1;
                    }
                }
                if (curEnemyEffected >= enemyEffected && curEnemyEffected >= 2 && curFriendEffected <= friendEffected) {
                    friendEffected = curFriendEffected;
                    enemyEffected = curEnemyEffected;
                    cell = freeze;
                }
            }
        }
        if (enemyEffected >= 2) {
            return cell;
        }
        return null;
    }

    public static Cell getAvailableShoot(MyWorm worm) {
        boolean canHitTarget = false;
        Cell selectedCell;
        List<Cell> shootingArea = new ArrayList<>();
        PlaneUtils.constructFireDirectionLines(worm.position, worm.weapon.range, true)
                .forEach(l -> shootingArea.addAll(l));
        canHitTarget = shootingArea
                .stream().filter(c -> Bot.getOpponentWorms().stream()
                        .filter(w -> c.equals(w.position) && WormUtils.isAlive(w)).findFirst().isPresent())
                .findFirst().isPresent();
        selectedCell = shootingArea
                .stream().filter(c -> Bot.getOpponentWorms().stream()
                        .filter(w -> c.equals(w.position) && WormUtils.isAlive(w)).findFirst().isPresent())
                .findFirst().orElse(new Cell(0, 0));

        if (canHitTarget) {
            return selectedCell;
        }
        return null;
    }

    public static boolean agentStartsMoving() {
        Worm agent = Bot.getMyWormList().stream().filter(w -> w.profession == Profession.AGENT).findFirst()
                .orElse(null);
        if (agent == null)
            return true;
        if (!WormUtils.isAlive(agent))
            return true;
        Worm enemyCommando = Bot.getOpponentWorms().stream().filter(w -> w.profession == Profession.COMMANDO)
                .findFirst().orElse(new Worm(Profession.COMMANDO));
        Worm enemyTechnologist = Bot.getOpponentWorms().stream().filter(w -> w.profession == Profession.TECHNOLOGIST)
                .findFirst().orElse(new Worm(Profession.TECHNOLOGIST));
        if (!WormUtils.isAlive(enemyCommando) || !WormUtils.isAlive(enemyTechnologist))
            return true;
        int xtengah = (enemyCommando.position.x + enemyTechnologist.position.x) / 2;
        int ytengah = (enemyCommando.position.y + enemyTechnologist.position.y) / 2;

        return (PlaneUtils.realEuclideanDistance(agent.position, new Position(xtengah, ytengah)) + 5 > PlaneUtils
                .realEuclideanDistance(enemyCommando.position, enemyTechnologist.position));
    }

    // public static Cell agentCanShootEnemyToDie() {
    // if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
    // return null;

    // public static Cell agentCanShootEnemyToDie() {
    // if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
    // return null;

    // public static Cell agentCanShootEnemyToDie() {
    // if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
    // return null;
    // }
    // if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
    // return null;
    // }
    // List<Cell> bananaRange = new ArrayList<>();
    // PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

    // boolean canShootTargetToDie = false;
    // int friendEffected = 10;
    // Cell choosenCell = new Cell(0, 0);

    // for (Cell banana : bananaRange) {
    // List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
    // int curFriendEffected = 0;
    // for (Cell shoot : shootingRange) {

    // curFriendEffected = Bot.getMyWormList().stream().filter(w -> {
    // return w.position.equals(shoot) && WormUtils.isAlive(w);
    // }).collect(Collectors.toList()).size();

    // if (target.position.equals(shoot) && target.health <= 20 && curFriendEffected
    // < friendEffected) {
    // canShootTargetToDie = true;
    // choosenCell = banana;
    // friendEffected = curFriendEffected;
    // }
    // }
    // }
    // if (canShootTargetToDie) {
    // return choosenCell;
    // }
    // return null;
    // }

    public static Cell desperateAgent() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 1);
            int curFriendEffected = 0;
            int curEnemyEffected = 0;

            for (Cell shoot : shootingRange) {
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm)) {
                        curEnemyEffected += 1;
                    }
                }
            }

            if (curFriendEffected <= friendEffected && curEnemyEffected > enemyEffected) {
                choosenCell = banana;
                friendEffected = curFriendEffected;
                enemyEffected = curEnemyEffected;
            }

        }
        if (enemyEffected > 0) {
            return choosenCell;
        }
        return null;

    }

    public static Cell desperateTechnologist() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getTechnologist())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getTechnologist().snowballs.count <= 0) {
            return null;
        }

        List<Cell> freezeRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell freeze : freezeRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(freeze, 1);
            int curFriendEffected = 0;
            int curEnemyEffected = 0;

            for (Cell shoot : shootingRange) {
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm)
                            && enemyWorm.roundsUntilUnfrozen <= 0) {
                        curEnemyEffected += 1;
                    }
                }
            }

            if (curFriendEffected <= friendEffected && curEnemyEffected >= enemyEffected) {
                choosenCell = freeze;
                friendEffected = curFriendEffected;
                enemyEffected = curEnemyEffected;
            }

        }
        if (enemyEffected > 0) {
            return choosenCell;
        }
        return null;

    }

    public static Cell bananaCanKill() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            int curFriendEffected = 0;
            int curEnemyEffected = 0;

            for (Cell shoot : shootingRange) {
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm) && enemyWorm.health <= 21) {
                        curEnemyEffected += 1;
                    }
                }
            }

            if (curFriendEffected <= friendEffected && curEnemyEffected > enemyEffected) {
                choosenCell = banana;
                friendEffected = curFriendEffected;
                enemyEffected = curEnemyEffected;
            }

        }
        if (enemyEffected > 0) {
            return choosenCell;
        }
        return null;

    }

    public static Cell bananaHitTarget(Worm target) {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        boolean canHitTarget = false;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            int curFriendEffected = 0;
            boolean targetHitted = false;
            for (Cell shoot : shootingRange) {
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                if (target.position.equals(shoot)) {
                    targetHitted = true;
                }
            }

            if (curFriendEffected <= friendEffected && targetHitted) {
                choosenCell = banana;
                friendEffected = curFriendEffected;
                canHitTarget = true;
            }

        }
        if (canHitTarget) {
            return choosenCell;
        }
        return null;
    }

    public static Cell freezeHitTarget(Worm target) {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getTechnologist())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getTechnologist().snowballs.count <= 0) {
            return null;
        }

        if (target.roundsUntilUnfrozen > 0) {
            return null;
        }

        List<Cell> freezerRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezerRange.addAll(l));

        int friendEffected = 10;
        boolean canHitTarget = false;
        Cell choosenCell = new Cell(0, 0);
        for (Cell freeze : freezerRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(freeze, 1);
            int curFriendEffected = 0;
            boolean targetHitted = false;
            for (Cell shoot : shootingRange) {
                boolean canBeHitted = false;
                for (MyWorm ourWorm : Bot.getMyWormList()) {
                    List<Cell> shootRange = new ArrayList<>();
                    PlaneUtils.constructFireDirectionLines(ourWorm.position, ourWorm.weapon.range, true)
                            .forEach(l -> shootRange.addAll(l));
                    for (Cell range : shootRange) {
                        if (range.equals(target.position)) {
                            canBeHitted = true;
                        }
                    }
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                if (target.position.equals(shoot) && canBeHitted) {
                    targetHitted = true;
                }

            }

            if (curFriendEffected <= friendEffected && targetHitted) {
                choosenCell = freeze;
                friendEffected = curFriendEffected;
                canHitTarget = true;
            }

        }
        if (canHitTarget) {
            return choosenCell;
        }
        return null;
    }

    public static Cell nearHealthPack(Position center) {
        List<Cell> surroundingCell = PlaneUtils.getSurroundingCells(center.x, center.y);
        for (Cell cell : surroundingCell) {
            if (GameUtils.lookup(cell).powerUp != null) {
                return cell;
            }
        }
        return null;
    }

}