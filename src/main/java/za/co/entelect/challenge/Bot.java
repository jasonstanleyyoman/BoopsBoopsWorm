package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;

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

    public static String getState() {
        return state;
    }

    public Command run() {
        // TODO bot here
        return null;
    }

}
