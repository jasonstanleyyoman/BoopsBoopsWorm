package za.co.entelect.challenge;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.common.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.*;

public class Bot {
    private static Bot instance;
    private static boolean isAgentMoving = false;
    private Random random;
    private String state;
    private GameState gameState;

    public Bot(Random newrandom, String newState, GameState newgameState) {
        instance = null;
        
        instance = this;
        this.random = newrandom;
        this.state = newState;
        this.gameState = newgameState;
    }

    public static Bot getBot() {
        return instance;
    }

    public static GameState getGameState() {
        return getBot().gameState;
    }

    public static Random getRandom() {
        return getBot().random;
    }

    public static Opponent getOpponent() {
        return getGameState().opponents[0];
    }

    public static List<Worm> getOpponentWorms() {
        return Arrays.asList(getOpponent().worms);
    }

    public static List<MyWorm> getMyWormList() {
        return Arrays.asList(getGameState().myPlayer.worms);
    }

    public static String getState() {
        return getBot().state;
    }

    public MyWorm getCurrentWorm() {
        return Arrays.stream(gameState.myPlayer.worms).filter(myWorm -> myWorm.id == gameState.currentWormId)
                .findFirst().get();
    }

    public Command run() {
        Worm enemyCommando = WormUtils.getEnemyCommando();
        Worm enemyTechnologist = WormUtils.getEnemyTechnologist();
        Worm enemyAgent = WormUtils.getEnemyAgent();
        Cell targetCell;
        MyWorm currentWorm = getCurrentWorm();

        if (getGameState().myPlayer.remainingWormSelections > 0) {
            if (currentWorm.profession != Profession.AGENT) {
                targetCell = StrategyUtils.agentCanShootTwoWorms();
                Agent agent = getGameState().myPlayer.getAgent();
                if (targetCell != null) {
                    return new SelectCommand(agent, new BananaCommand(targetCell));
                }
            }

            if (currentWorm.profession != Profession.TECHNOLOGIST) {
                targetCell = StrategyUtils.technologistCanShootTwoWorms();

                Technologist tech = getGameState().myPlayer.getTechnologist();
                if (targetCell != null) {
                    return new SelectCommand(tech, new SnowballCommand(targetCell));
                }
            }
        }

        switch (currentWorm.profession) {
            case AGENT:
                targetCell = StrategyUtils.agentCanShootTwoWorms();

                if (!isAgentMoving) {
                    isAgentMoving = StrategyUtils.agentStartsMoving()
                            || StrategyUtils.countMove(currentWorm.position, enemyCommando.position) <= 9
                            || StrategyUtils.countMove(currentWorm.position, enemyTechnologist.position) <= 9;
                    return new DoNothingCommand();
                }
                if (targetCell != null) {
                    return new BananaCommand(targetCell);
                }
                
                targetCell = StrategyUtils.bananaCanKill();
                if (targetCell != null) {
                    return new BananaCommand(targetCell);
                }
                if (currentWorm.health < 30) {
                    targetCell = StrategyUtils.desperateAgent();
                    if (targetCell != null) {
                        return new BananaCommand(targetCell);
                    }
                }
                Worm target = StrategyUtils.setTargetWorm(currentWorm);

                targetCell = StrategyUtils.bananaHitTarget(target);

                if (targetCell != null) {
                    return new BananaCommand(targetCell);
                }
                targetCell = StrategyUtils.getAvailableShoot(currentWorm);
                if (targetCell != null) {
                    Direction direction = PlaneUtils.resolveDirection(currentWorm.position, targetCell);
                    return new ShootCommand(direction);
                }

                Cell nextCell = StrategyUtils.nearHealthPack(currentWorm.position);

                if (nextCell == null) {
                    nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);
                }

                if (nextCell.type == CellType.DIRT) {
                    return new DigCommand(nextCell);
                } else {
                    return new MoveCommand(nextCell);
                }
            case TECHNOLOGIST:

                targetCell = StrategyUtils.technologistCanShootTwoWorms();

                if (targetCell != null) {
                    return new SnowballCommand(targetCell);
                }

                if (currentWorm.health < 30) {
                    targetCell = StrategyUtils.desperateTechnologist();
                    if (targetCell != null) {
                        return new SnowballCommand(targetCell);
                    }
                }
                target = StrategyUtils.setTargetWorm(currentWorm);

                targetCell = StrategyUtils.freezeHitTarget(target);
                if (targetCell != null) {
                    return new SnowballCommand(targetCell);
                }
                targetCell = StrategyUtils.getAvailableShoot(currentWorm);
                if (targetCell != null) {
                    Direction direction = PlaneUtils.resolveDirection(currentWorm.position, targetCell);
                    return new ShootCommand(direction);
                }
                nextCell = StrategyUtils.nearHealthPack(currentWorm.position);
                if (nextCell == null) {
                    nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);
                }

                if (nextCell.type == CellType.DIRT) {
                    return new DigCommand(nextCell);
                } else {
                    return new MoveCommand(nextCell);
                }
            case COMMANDO:
                targetCell = StrategyUtils.getAvailableShoot(currentWorm);
                
                if (targetCell != null && WormUtils.isAlive(enemyAgent) && targetCell != enemyAgent.position){
                    Direction direction = PlaneUtils.resolveDirection(currentWorm.position, targetCell);
                    return new ShootCommand(direction);
                }

                if (targetCell != null && !WormUtils.isAlive(enemyAgent)) {
                    Direction direction = PlaneUtils.resolveDirection(currentWorm.position, targetCell);
                    return new ShootCommand(direction);
                }
                
                if (WormUtils.isAlive(enemyAgent)){
                    target = enemyAgent;    
                } else {
                    target = StrategyUtils.setTargetWorm(currentWorm);
                }

                nextCell = StrategyUtils.nearHealthPack(currentWorm.position);
                if (nextCell == null) {
                    nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);
                }

                if (nextCell.type == CellType.DIRT) {
                    return new DigCommand(nextCell);
                } else {
                    return new MoveCommand(nextCell);
                }
            default:
                return null;
        }
    }

}