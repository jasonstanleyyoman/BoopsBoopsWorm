package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.common.WormUtils;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Profession;
import za.co.entelect.challenge.common.*;

import java.util.*;

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

        // Early phase of the game
        Worm enemyAgent = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.AGENT)).findFirst().get();
        if (enemyAgent.health > 0) { // Greedy by banana
            // Select worm paling deket sama banana
            MyWorm wormToBeSelected = WormUtils.targetBanana();
            // Jika wormToBeSelected bisa shoot, shoot aja.
            Worm wormYangBisaDitembak = WormUtils.getFirstWormInRange(wormToBeSelected);
            if (wormYangBisaDitembak.id == enemyAgent.id) {
                return new SelectCommand(wormToBeSelected.id, new ShootCommand(
                        PlaneUtils.resolveDirection(currentWorm.position, wormYangBisaDitembak.position)));
            } else {

                // if (perlu dig){
                // insert dig code here
                // }

                // else if (tidak perlu dig) {
                // PlaneUtils.nextLineGreedy(wormToBeSelected.)
                // return new SelectCommand(wormToBeSelected.id, new MoveCommand(x, y));
                // }
            }
        } else {
            // Late phase of the game (enemy agent uda mati)
        }

        return null;
    }
}
