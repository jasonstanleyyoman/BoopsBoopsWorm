package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.MyWorm;
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
        } else if (WormUtils.isAlive(enemyTechno)) {
            return enemyTechno;
        } else {
            return enemyCommando;
        }
    }

    public static boolean isTheMinimum(double a, double b, double c) {
        return (a <= b) && (a <= c);
    }

    public static MyWorm findClosestPlayerWorm(Worm enemyWorm) {
        MyWorm commando = Bot.getGameState().myPlayer.getCommando();
        MyWorm technologist = Bot.getGameState().myPlayer.getTechnologist();
        MyWorm agent = Bot.getGameState().myPlayer.getAgent();

        double distanceToCommando = PlaneUtils.realEuclideanDistance(enemyWorm.position, commando.position);
        double distanceToTechno = PlaneUtils.realEuclideanDistance(enemyWorm.position, technologist.position);
        double distanceToAgent = PlaneUtils.realEuclideanDistance(enemyWorm.position, agent.position);

        if (isTheMinimum(distanceToAgent, distanceToCommando, distanceToTechno) && WormUtils.isAlive(agent)) {
            if (distanceToAgent < 10) {
                return agent;
            } else if (distanceToTechno <= distanceToCommando) {
                return technologist;
            } else {
                return commando;
            }
        } else if (isTheMinimum(distanceToTechno, distanceToCommando, distanceToAgent)
                && WormUtils.isAlive(technologist)) {
            if (distanceToTechno < 10) {
                return technologist;
            } else if (distanceToAgent <= distanceToCommando) {
                return agent;
            } else {
                return commando;
            }
        } else if (isTheMinimum(distanceToCommando, distanceToTechno, distanceToAgent) && WormUtils.isAlive(commando)) {
            if (distanceToCommando < 10) {
                return commando;
            } else if (distanceToAgent <= distanceToTechno) {
                return agent;
            } else {
                return technologist;
            }
        }
        return null;
    }

    public static Cell agentCanShootTwoWorms() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
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

    public static Cell agentCanShootTargetToDie(Worm target) {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
            return null;
        }
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        boolean canShootTargetToDie = false;
        int friendEffected = 10;
        Cell choosenCell = new Cell(0, 0);

        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            int curFriendEffected = 0;
            for (Cell shoot : shootingRange) {

                curFriendEffected = Bot.getMyWormList().stream().filter(w -> {
                    return w.position.equals(shoot) && WormUtils.isAlive(w);
                }).collect(Collectors.toList()).size();

                if (target.position.equals(shoot) && target.health <= 20 && curFriendEffected < friendEffected) {
                    canShootTargetToDie = true;
                    choosenCell = banana;
                    friendEffected = curFriendEffected;
                }
            }
        }
        if (canShootTargetToDie) {
            return choosenCell;
        }
        return null;
    }

    public static Cell technologistCanFreezeTarget(Worm target) {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getTechnologist())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getTechnologist().snowballs.count <= 0) {
            return null;
        }
        if (target.roundsUntilUnfrozen > 0) {
            return null;
        }
        boolean canFreeze = false;
        int friendEffected = 10;

        Cell selectedCell = new Cell(0, 0);

        List<Cell> freezeTarget = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeTarget.addAll(l));

        for (Cell freeze : freezeTarget) {
            int curFriendEffected = 0;
            boolean kenaMusuh = false;

            List<Cell> shootingRange = WormUtils.getShootingArea(freeze, 1);

            for (Cell shoot : shootingRange) {
                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot) && WormUtils.isAlive(ourWorm)) {
                        curFriendEffected += 1;
                    }
                }

                if (target.position.equals(shoot)) {
                    kenaMusuh = true;
                }

                if (curFriendEffected < friendEffected && kenaMusuh) {
                    friendEffected = curFriendEffected;
                    canFreeze = true;
                    selectedCell = freeze;
                }
            }
        }
        if (canFreeze) {
            return selectedCell;
        }

        return null;

    }

    public Cell getAvailableShoot(MyWorm worm) {
        boolean canHitTarget = false;
        Cell selectedCell;
        List<Cell> shootingArea = new ArrayList<>();
        PlaneUtils.constructFireDirectionLines(worm.position, worm.weapon.range, true)
                .forEach(l -> shootingArea.addAll(l));

        Stream<Cell> sCell = shootingArea.stream()
                .filter(c -> Bot.getOpponentWorms().stream().filter(w -> c.equals(w.position)).findFirst().isPresent());
        canHitTarget = sCell.findFirst().isPresent();
        selectedCell = sCell.findFirst().orElse(new Cell(0, 0));

        if (canHitTarget) {
            return selectedCell;
        }
        return null;
    }

    public static Cell agentCanShootEnemyToDie() {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getAgent())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().bananaBomb.count <= 0) {
            return null;
        }
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        boolean canShootTargetToDie = false;
        int friendEffected = 10;
        Cell choosenCell = new Cell(0, 0);

        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            int curFriendEffected = 0;
            for (Cell shoot : shootingRange) {

                curFriendEffected = Bot.getMyWormList().stream().filter(w -> {
                    return w.position.equals(shoot) && WormUtils.isAlive(w);
                }).collect(Collectors.toList()).size();

                if (target.position.equals(shoot) && target.health <= 20 && curFriendEffected < friendEffected) {
                    canShootTargetToDie = true;
                    choosenCell = banana;
                    friendEffected = curFriendEffected;
                }
            }
        }
        if (canShootTargetToDie) {
            return choosenCell;
        }
        return null;
    }

    public Cell desperateAgent() {
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
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm)) {
                        curEnemyEffected += 1;
                    }
                }
            }

            if (curFriendEffected <= friendEffected && curEnemyEffected >= enemyEffected) {
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

    public Cell desperateTechnologist() {
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
                    if (enemyWorm.position.equals(shoot) && WormUtils.isAlive(enemyWorm)) {
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

    public static Cell bananaCanKill () {
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

            if (curFriendEffected <= friendEffected && curEnemyEffected >= enemyEffected) {
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

    public static Cell bananaHitTarget (Worm target) {
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

    public static Cell freezeHitTarget (Worm target) {
        if (!WormUtils.isAlive(Bot.getGameState().myPlayer.getTechnologist())) {
            return null;
        }
        if (Bot.getGameState().myPlayer.getAgent().snowballs.count <= 0) {
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


}