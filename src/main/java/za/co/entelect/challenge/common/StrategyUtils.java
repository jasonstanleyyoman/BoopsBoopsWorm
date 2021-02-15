package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.enums.Profession;

public class StrategyUtils {

    public static Worm setTargetWorm() {
        // Initializing player's worms
        Worm commando = Bot.getGameState().myPlayer.getCommando();
        Worm technologist = Bot.getGameState().myPlayer.getTechnologist();
        Worm agent = Bot.getGameState().myPlayer.getAgent();

        // Initializing enemy's worms
        Worm enemyAgent = WormUtils.getEnemy(Profession.AGENT);
        Worm enemyTechno = WormUtils.getEnemy(Profession.TECHNOLOGIST);
        Worm enemyCommando = WormUtils.getEnemy(Profession.COMMANDO);

        // Enemy's technologist distance to all player's worms
        Map<Profession, Double> distanceEnemy = new HashMap<>(3);
        for (Worm enemy : new Worm[] { enemyAgent, enemyTechno, enemyCommando }) {
            double totalDistance = 0D;
            for (Worm self : new Worm[] { commando, technologist, agent }) {
                if (WormUtils.isAlive(enemy) && WormUtils.isAlive(self)) {
                    totalDistance += PlaneUtils.realEuclideanDistance(enemy.position, self.position);
                }
            }
            distanceEnemy.put(enemy.profession, totalDistance);
        }
        double totalEnemyTechnoDistance = distanceEnemy.getOrDefault(Profession.TECHNOLOGIST, 0D);
        double totalEnemyAgentDistance = distanceEnemy.getOrDefault(Profession.AGENT, 0D);
        double totalEnemyCommandoDistance = distanceEnemy.getOrDefault(Profession.COMMANDO, 0D);

        // Finding the 2 minimum values
        if (totalEnemyAgentDistance > totalEnemyCommandoDistance
                && totalEnemyAgentDistance > totalEnemyTechnoDistance) {
            double selisihJarak = Math.abs(totalEnemyCommandoDistance - totalEnemyAgentDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyCommandoDistance >= totalEnemyTechnoDistance) {
                    if (technologist.health > 0) {
                        return enemyTechno;
                    } else {
                        return enemyCommando;
                    }
                }
            }

        } else if (totalEnemyTechnoDistance > totalEnemyAgentDistance
                && totalEnemyTechnoDistance > totalEnemyCommandoDistance) {
            double selisihJarak = Math.abs(totalEnemyAgentDistance - totalEnemyCommandoDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyCommandoDistance >= totalEnemyAgentDistance) {
                    if (agent.health > 0) {
                        return enemyAgent;
                    } else {
                        return enemyCommando;
                    }
                }
            }

        } else if (totalEnemyCommandoDistance > totalEnemyAgentDistance
                && totalEnemyCommandoDistance > totalEnemyTechnoDistance) {
            double selisihJarak = Math.abs(totalEnemyAgentDistance - totalEnemyTechnoDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyTechnoDistance >= totalEnemyAgentDistance) {
                    if (agent.health > 0) {
                        return enemyAgent;
                    } else {
                        return enemyTechno;
                    }
                }
            }
        }

        if (WormUtils.isAlive(enemyAgent)) {
            return enemyAgent;
        } else if (WormUtils.isAlive(enemyTechno)) {
            return enemyTechno;
        } else if (WormUtils.isAlive(enemyCommando)) {
            return enemyCommando;
        }
        return null;
    }

    public static Worm findClosestPlayerWorm(Worm enemyWorm) {
        Worm commando = Bot.getGameState().myPlayer.getCommando();
        Worm technologist = Bot.getGameState().myPlayer.getTechnologist();
        Worm agent = Bot.getGameState().myPlayer.getAgent();

        double distanceToCommando = PlaneUtils.realEuclideanDistance(enemyWorm.position, commando.position);
        double distanceToTechno = PlaneUtils.realEuclideanDistance(enemyWorm.position, technologist.position);
        double distanceToAgent = PlaneUtils.realEuclideanDistance(enemyWorm.position, agent.position);

        if (distanceToAgent <= distanceToCommando && distanceToAgent <= distanceToTechno && WormUtils.isAlive(agent)) {
            if (distanceToAgent < 10) {
                return commando;
            }
        } else if (distanceToTechno < distanceToCommando && distanceToTechno < distanceToAgent
                && WormUtils.isAlive(technologist)) {
            return technologist;
        } else if (distanceToCommando < distanceToTechno && distanceToCommando < distanceToAgent
                && WormUtils.isAlive(commando)) {
            return agent;
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
                if (curEnemyEffected > enemyEffected && curEnemyEffected >= 2 && curFriendEffected < friendEffected) {
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

                for (Worm ourWorm : Bot.getMyWormList()) {
                    if (ourWorm.position.equals(shoot)) {
                        curFriendEffected += 1;
                    }
                }

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

        List<Cell> freezeTarget = new ArrayList<>();
        PlaneUtils.getFreezerRange().forEach(l -> freezeTarget.addAll(l));

        for (Cell freeze : freezeTarget) {
            int curFriendEffected = 0;

        }

        return null;

    }
}