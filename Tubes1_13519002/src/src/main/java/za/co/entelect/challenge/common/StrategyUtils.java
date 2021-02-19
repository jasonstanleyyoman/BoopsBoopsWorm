package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Position;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.CellType;
import za.co.entelect.challenge.entities.worm.Agent;

public class StrategyUtils {

    public static Worm setTargetWorm(MyWorm ourWorm) {
        // Initializing enemy's worms
        Worm enemyAgent = WormUtils.getEnemyAgent();
        Worm enemyTechno = WormUtils.getEnemyTechnologist();
        Worm enemyCommando = WormUtils.getEnemyCommando();

        if (countMove(ourWorm.position, enemyAgent.position) <= 4 && WormUtils.isAlive(enemyAgent)) {
            return enemyAgent;
        }

        if (countMove(ourWorm.position, enemyTechno.position) <= 4 && WormUtils.isAlive(enemyTechno)) {
            return enemyTechno;
        }

        if (countMove(ourWorm.position, enemyCommando.position) <= 4 && WormUtils.isAlive(enemyCommando)) {
            return enemyCommando;
        }

        if (WormUtils.isAlive(enemyAgent)) {
            return enemyAgent;
        } else if (WormUtils.isAlive(enemyCommando) && WormUtils.isAlive(enemyTechno)
                && enemyCommando.health >= enemyTechno.health) {
            return enemyTechno;
        } else if (WormUtils.isAlive(enemyCommando) && WormUtils.isAlive(enemyTechno)
                && enemyCommando.health < enemyTechno.health) {
            return enemyCommando;
        } else if (!WormUtils.isAlive(enemyCommando)) {
            return enemyTechno;
        } else {
            return enemyCommando;
        }
    }

    public static Cell agentCanShootTwoWorms() {
        if (!WormUtils.isAlive(WormUtils.getOurAgent())) {
            return null;
        }

        if (WormUtils.getOurAgent().bananaBomb.count <= 0) {
            return null;
        }
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));
        int enemyEffected = 0;
        int friendEffected = 10;
        Cell cell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = PlaneUtils.getBananaArea(banana);
            int curEnemyEffected = 0;
            int curFriendEffected = 0;
            for (Cell shoot : shootingRange) {

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

            }
            if (curEnemyEffected >= enemyEffected && curEnemyEffected >= 2 && curFriendEffected <= friendEffected) {
                friendEffected = curFriendEffected;
                enemyEffected = curEnemyEffected;
                cell = banana;
            }
        }
        if (enemyEffected >= 2) {
            return cell;
        }
        return null;

    }

    public static Cell technologistCanShootTwoWorms() {
        if (!WormUtils.isAlive(WormUtils.getOurTechnologist())) {
            return null;
        }
        if (WormUtils.getOurTechnologist().snowballs.count <= 0) {
            return null;
        }
        List<Cell> freezeRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeRange.addAll(l));
        int enemyEffected = 0;
        int friendEffected = 10;
        Cell cell = new Cell(0, 0);
        for (Cell freeze : freezeRange) {
            List<Cell> shootingRange = PlaneUtils.getShootingArea(freeze, 1);
            int curEnemyEffected = 0;
            int curFriendEffected = 0;
            for (Cell shoot : shootingRange) {

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

            }
            if (curEnemyEffected >= enemyEffected && curEnemyEffected >= 2 && curFriendEffected <= friendEffected) {
                friendEffected = curFriendEffected;
                enemyEffected = curEnemyEffected;
                cell = freeze;
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
        Agent agent = WormUtils.getOurAgent();
        if (agent == null)
            return true;
        if (!WormUtils.isAlive(agent))
            return true;
        Worm enemyCommando = WormUtils.getEnemyCommando();
        Worm enemyTechnologist = WormUtils.getEnemyTechnologist();
        if (!WormUtils.isAlive(enemyCommando) || !WormUtils.isAlive(enemyTechnologist))
            return true;
        int xtengah = (enemyCommando.position.x + enemyTechnologist.position.x) / 2;
        int ytengah = (enemyCommando.position.y + enemyTechnologist.position.y) / 2;

        return (countMove(agent.position, new Position(xtengah, ytengah)) + 9 > countMove(enemyCommando.position,
                enemyTechnologist.position));
    }

    public static int countMove(Position awal, Position akhir) {
        List<Cell> cells = PlaneUtils.generateLine(GameUtils.lookup(awal), GameUtils.lookup(akhir));
        int result = cells.size();
        for (Cell c : cells) {
            if (c.type == CellType.DIRT) {
                result++;
            }
        }
        return result;
    }

    public static Cell desperateAgent() {
        if (!WormUtils.isAlive(WormUtils.getOurAgent())) {
            return null;
        }
        if (WormUtils.getOurAgent().bananaBomb.count <= 0) {
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = PlaneUtils.getBananaArea(banana);
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
        if (!WormUtils.isAlive(WormUtils.getOurTechnologist())) {
            return null;
        }
        if (WormUtils.getOurTechnologist().snowballs.count <= 0) {
            return null;
        }

        List<Cell> freezeRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell freeze : freezeRange) {
            List<Cell> shootingRange = PlaneUtils.getShootingArea(freeze, 1);
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
        if (!WormUtils.isAlive(WormUtils.getOurAgent())) {
            return null;
        }
        if (WormUtils.getOurAgent().bananaBomb.count <= 0) {
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        int enemyEffected = 0;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = PlaneUtils.getBananaArea(banana);
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

    public static int bananasinglecooldown = 0;

    public static Cell bananaHitTarget(Worm target) {
        if (!WormUtils.isAlive(WormUtils.getOurAgent())) {
            return null;
        }
        if (WormUtils.getOurAgent().bananaBomb.count <= 0) {
            return null;
        }
        if (bananasinglecooldown > 0) {
            bananasinglecooldown--;
            return null;
        }

        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        int friendEffected = 10;
        boolean canHitTarget = false;
        Cell choosenCell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = PlaneUtils.getBananaArea(banana);
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
            bananasinglecooldown = 6 + (new Random()).nextInt(2);
            return choosenCell;
        }
        return null;
    }

    public static int freezesinglecooldown = 0;

    public static Cell freezeHitTarget(Worm target) {
        Technologist tech = WormUtils.getOurTechnologist();
        if (!WormUtils.isAlive(tech)) {
            return null;
        }
        if (tech.snowballs.count <= 1) {
            return null;
        }

        if (target.roundsUntilUnfrozen > 0) {
            return null;
        }
        if (countMove(tech.position, target.position) <= 6) {
            if (freezesinglecooldown > 0) {
                freezesinglecooldown--;
                return null;
            }
        }
        List<Cell> freezerRange = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezerRange.addAll(l));

        int friendEffected = 10;
        boolean canHitTarget = false;
        Cell choosenCell = new Cell(0, 0);
        for (Cell freeze : freezerRange) {
            List<Cell> shootingRange = PlaneUtils.getShootingArea(freeze, 1);
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
            freezesinglecooldown = 5 + (new Random()).nextInt(2);
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