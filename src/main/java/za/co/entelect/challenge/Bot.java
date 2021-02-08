package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;

import java.util.*;

public class Bot {
    private static Random random;
    private static GameState gameState;

    public Bot(Random newrandom, GameState newgameState) {
        random = newrandom;
        gameState = newgameState;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static Random getRandom() {
        return random;
    }

    public Command run() {
        // TODO bot here
        return null;
    }

}
