package za.co.entelect.challenge;

import java.util.Arrays;
import java.util.List;
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
        MyWorm currentWorm = WormUtils.getCurrentWorm();
        Opponent opponent = getOpponent();
        Technologist technologist = gameState.myPlayer.getTechnologist();
        Commando commando = gameState.myPlayer.getCommando();
        List<MyWorm> myWormsWithProf = Arrays.asList(new MyWorm[] { technologist, commando });

        // Early phase of the game
        Worm enemyAgent = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.AGENT)).findFirst().orElse(null);
        if (enemyAgent.health > 0) { // Greedy by banana
            Worm wormYangBisaDitembak = WormUtils.getFirstWormInRange(technologist);
            if (wormYangBisaDitembak.id == enemyAgent.id) {
                if (enemyAgent.roundsUntilUnfrozen == 0) {
                    return new SelectCommand(technologist.id, new SnowballCommand(enemyAgent.position));
                }
                return new SelectCommand(technologist.id, new ShootCommand(
                        PlaneUtils.resolveDirection(currentWorm.position, wormYangBisaDitembak.position)));
            } else {
                Worm furthestWorm = WormUtils.getFurthestWorm(myWormsWithProf, wormYangBisaDitembak.position);

                Cell nextCell = PlaneUtils.nextLine(technologist.position, enemyAgent.position);

                if (nextCell.type == CellType.DIRT) {
                    return new SelectCommand(furthestWorm.id, new DigCommand(nextCell));
                } else {
                    return new SelectCommand(furthestWorm.id, new MoveCommand(nextCell));
                }
            }
        } else {
            // Late phase of the game (enemy agent uda mati)
        }

        return null;
    }
}
