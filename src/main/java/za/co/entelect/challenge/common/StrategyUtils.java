package za.co.entelect.challenge.common;

import java.util.ArrayList;
import java.util.List;

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
        double disToEnemyTtoC, disToEnemyTtoA, disToEnemyTtoT;
        if (enemyTechno.health > 0 && commando.health > 0) {
            disToEnemyTtoC = PlaneUtils.realEuclideanDistance(enemyTechno.position, commando.position);
        } else {
            disToEnemyTtoC = 0;
        }
        if (enemyTechno.health > 0 && agent.health > 0) {
            disToEnemyTtoA = PlaneUtils.realEuclideanDistance(enemyTechno.position, agent.position);
        } else {
            disToEnemyTtoA = 0;
        }
        if (enemyTechno.health > 0 && technologist.health > 0) {
            disToEnemyTtoT = PlaneUtils.realEuclideanDistance(enemyTechno.position, technologist.position);
        } else {
            disToEnemyTtoT = 0;
        }
        double totalEnemyTechnoDistance = disToEnemyTtoA + disToEnemyTtoC + disToEnemyTtoT;

        // Enemy's agent distance to all player's worms
        double disToEnemyAtoA, disToEnemyAtoC, disToEnemyAtoT;
        if (enemyAgent.health > 0 && commando.health > 0) {
            disToEnemyAtoC = PlaneUtils.realEuclideanDistance(enemyAgent.position, commando.position);
        } else {
            disToEnemyAtoC = 0;
        }
        if (enemyAgent.health > 0 && agent.health > 0) {
            disToEnemyAtoA = PlaneUtils.realEuclideanDistance(enemyAgent.position, agent.position);
        } else {
            disToEnemyAtoA = 0;
        }
        if (enemyAgent.health > 0 && technologist.health > 0) {
            disToEnemyAtoT = PlaneUtils.realEuclideanDistance(enemyAgent.position, technologist.position);
        } else {
            disToEnemyAtoT = 0;
        }
        double totalEnemyAgentDistance = disToEnemyAtoA + disToEnemyAtoC + disToEnemyAtoT;

        // Enemy's commando distance to all player's worms
        double disToEnemyCtoA, disToEnemyCtoC, disToEnemyCtoT;
        if (enemyCommando.health > 0 && commando.health > 0) {
            disToEnemyCtoC = PlaneUtils.realEuclideanDistance(enemyCommando.position, commando.position);
        } else {
            disToEnemyCtoC = 0;
        }
        if (enemyCommando.health > 0 && agent.health > 0) {
            disToEnemyCtoA = PlaneUtils.realEuclideanDistance(enemyCommando.position, agent.position);
        } else {
            disToEnemyCtoA = 0;
        }
        if (enemyCommando.health > 0 && technologist.health > 0) {
            disToEnemyCtoT = PlaneUtils.realEuclideanDistance(enemyCommando.position, technologist.position);
        } else {
            disToEnemyCtoT = 0;
        }
        double totalEnemyCommandoDistance = disToEnemyCtoA + disToEnemyCtoC + disToEnemyCtoT;

        // Finding the 2 minimum values
        if (totalEnemyAgentDistance > totalEnemyCommandoDistance
                && totalEnemyAgentDistance > totalEnemyTechnoDistance) {
            double selisihJarak = Math.abs(totalEnemyCommandoDistance - totalEnemyAgentDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyCommandoDistance >= totalEnemyTechnoDistance) {
                    if (technologist.health > 0) {
                        return technologist;
                    } else {
                        return commando;
                    }
                }
            }

        } else if (totalEnemyTechnoDistance > totalEnemyAgentDistance
                && totalEnemyTechnoDistance > totalEnemyCommandoDistance) {
            double selisihJarak = Math.abs(totalEnemyAgentDistance - totalEnemyCommandoDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyCommandoDistance >= totalEnemyAgentDistance) {
                    if (agent.health > 0) {
                        return agent;
                    } else {
                        return commando;
                    }
                }
            }

        } else if (totalEnemyCommandoDistance > totalEnemyAgentDistance
                && totalEnemyCommandoDistance > totalEnemyTechnoDistance) {
            double selisihJarak = Math.abs(totalEnemyAgentDistance - totalEnemyTechnoDistance);
            if (selisihJarak >= 20) {
                if (totalEnemyTechnoDistance >= totalEnemyAgentDistance) {
                    if (agent.health > 0) {
                        return agent;
                    } else {
                        return technologist;
                    }
                }
            }

        } else if (totalEnemyAgentDistance == totalEnemyCommandoDistance
                && totalEnemyAgentDistance == totalEnemyTechnoDistance) {
            if (agent.health > 0) {
                return agent;
            } else if (technologist.health > 0) {
                return technologist;
            } else if (commando.health > 0) {
                return commando;
            }
        }
        return null;
    }

    public static Cell agentCanShootTwoWorms() {
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));
        int enemyEffected = 0;
        Cell cell = new Cell(0, 0);
        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);
            for (Cell shoot : shootingRange) {
                int curEnemyEffected = 0;
                for (Worm enemyWorm : Bot.getOpponentWorms()) {
                    if (enemyWorm.position.equals(shoot)) {
                        curEnemyEffected += 1;
                    }
                }
                if (curEnemyEffected > enemyEffected && curEnemyEffected >= 2) {
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
        List<Cell> bananaRange = new ArrayList<>();
        PlaneUtils.getBananaBombRange().forEach(l -> bananaRange.addAll(l));

        boolean canShootTargetToDie = false;
        Cell choosenCell = new Cell(0, 0);

        for (Cell banana : bananaRange) {
            List<Cell> shootingRange = WormUtils.getShootingArea(banana, 2);

            for (Cell shoot : shootingRange) {
                if (target.position.equals(shoot) && target.health <= 20) {
                    canShootTargetToDie = true;
                    choosenCell = banana;
                }
            }
        }
        if (canShootTargetToDie) {
            return choosenCell;
        }
        return null;
    }
}