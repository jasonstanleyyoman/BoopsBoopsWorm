package za.co.entelect.challenge;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.common.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.entities.worm.*;
import za.co.entelect.challenge.enums.Profession;
import za.co.entelect.challenge.enums.CellType;

public class Bot {
    private static Random random;
    private static String state;
    private static GameState gameState;

    public Bot(Random newrandom, String newState, GameState newgameState) {
        random = newrandom;
        state = newState;
        gameState = newgameState;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static Random getRandom() {
        return random;
    }

    public static Opponent getOpponent() {
        return gameState.opponents[0];
    }

    public static List<Worm> getOpponentWorms() {
        return Arrays.asList(getOpponent().worms);
    }

    public static List<MyWorm> getMyWormList() {
        return Arrays.asList(gameState.myPlayer.worms);
    }

    public static String getState() {
        return state;
    }

    public Command run() {
        Technologist technologist = gameState.myPlayer.getTechnologist();
        Commando commando = gameState.myPlayer.getCommando();
        Agent agent = gameState.myPlayer.getAgent();
        List<MyWorm> myWormsWithProf = Arrays.asList(new MyWorm[] { technologist, commando });

        // Initializing all enemy worms
        Worm enemyAgent = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.AGENT)).findFirst().orElse(new Worm(Profession.AGENT));
        Worm enemyTechnologist = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.TECHNOLOGIST)).findFirst()
                .orElse(new Worm(Profession.TECHNOLOGIST));
        Worm enemyCommando = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.COMMANDO)).findFirst()
                .orElse(new Worm(Profession.COMMANDO));

        Worm enemyWormBisaDitembakTechnologist = Optional.ofNullable(WormUtils.getFirstWormInRange(technologist))
                .orElse(new Worm(Profession.TECHNOLOGIST));
        Worm enemyWormBisaDitembakCommando = Optional.ofNullable(WormUtils.getFirstWormInRange(commando))
                .orElse(new Worm(Profession.COMMANDO));
        Worm enemyWormBisaDitembakAgent = Optional.ofNullable(WormUtils.getFirstWormInRange(agent))
                .orElse(new Worm(Profession.AGENT));
        if (enemyAgent.health > 0) { // Greedy by banana
            Worm wormYangBisaDitembak = WormUtils.getFirstWormInRange(technologist);

            if (wormYangBisaDitembak != null) {
                if (wormYangBisaDitembak.id == enemyAgent.id) {
                    if (enemyAgent.roundsUntilUnfrozen == 0) {
                        return new SelectCommand(technologist.id, new SnowballCommand(enemyAgent.position));
                    }
                    return new SelectCommand(technologist.id, new ShootCommand(
                            PlaneUtils.resolveDirection(technologist.position, wormYangBisaDitembak.position)));
                }
            }

            Cell nextCell = PlaneUtils.nextLine(technologist.position, enemyAgent.position);

            if (nextCell.type == CellType.DIRT) {
                return new SelectCommand(technologist.id, new DigCommand(nextCell));
            }
            return new SelectCommand(technologist.id, new MoveCommand(nextCell));
        } else if (enemyTechnologist.health > 0) { // Greedy by technologist (after the death of enemy's agent)
            if (enemyWormBisaDitembakTechnologist.id == enemyTechnologist.id & technologist.health > 0) {
                if (enemyWormBisaDitembakTechnologist.roundsUntilUnfrozen == 0 & technologist.snowballs.count > 0) {
                    return new SelectCommand(technologist.id, new SnowballCommand(enemyTechnologist.position));
                } else {
                    return new SelectCommand(technologist.id, new ShootCommand(PlaneUtils
                            .resolveDirection(technologist.position, enemyWormBisaDitembakTechnologist.position)));
                }
            } else if (enemyWormBisaDitembakCommando.id == enemyTechnologist.id & commando.health > 0) {
                return new SelectCommand(commando.id, new ShootCommand(
                        PlaneUtils.resolveDirection(commando.position, enemyWormBisaDitembakCommando.position)));
            } else if (enemyWormBisaDitembakAgent.id == enemyTechnologist.id) {
                return new SelectCommand(agent.id, new ShootCommand(
                        PlaneUtils.resolveDirection(agent.position, enemyWormBisaDitembakAgent.position)));
            }
            Worm wormToBeSelected = WormUtils.targetTechnologist();

            Cell nextCell = PlaneUtils.nextLine(wormToBeSelected.position, enemyTechnologist.position);
            if (nextCell.type == CellType.DIRT) {
                return new SelectCommand(wormToBeSelected.id, new DigCommand(nextCell));
            } else {
                return new SelectCommand(wormToBeSelected.id, new MoveCommand(nextCell));
            }
        } else if (enemyCommando.health > 0) { // Greedy by Commando (last worm)
            if (enemyWormBisaDitembakCommando.id == enemyCommando.id) {
                return new SelectCommand(commando.id, new ShootCommand(
                        PlaneUtils.resolveDirection(commando.position, enemyWormBisaDitembakCommando.position)));
            } else if (enemyWormBisaDitembakAgent.id == enemyCommando.id) {
                return new SelectCommand(agent.id, new ShootCommand(
                        PlaneUtils.resolveDirection(agent.position, enemyWormBisaDitembakAgent.position)));
            } else if (enemyWormBisaDitembakTechnologist.id == enemyCommando.id) {
                return new SelectCommand(technologist.id, new ShootCommand(PlaneUtils
                        .resolveDirection(technologist.position, enemyWormBisaDitembakTechnologist.position)));
            }
            Worm furthestWorm = WormUtils.getFurthestWorm(myWormsWithProf, enemyCommando.position);

            Cell nextCell = PlaneUtils.nextLine(furthestWorm.position, enemyCommando.position);
            if (nextCell.type == CellType.DIRT) {
                return new SelectCommand(furthestWorm.id, new DigCommand(nextCell));
            } else {
                return new SelectCommand(furthestWorm.id, new MoveCommand(nextCell));
            }
        }

        return null;
    }
}
