package za.co.entelect.challenge.common;

import java.util.Arrays;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.Profession;

public class WormUtils {

    public static MyWorm getCurrentWorm() {
        GameState gameState = Bot.getGameState();
        return Bot.getMyWormList().stream().filter(myWorm -> myWorm.id == gameState.currentWormId).findFirst().get();
    }

    private static Worm getEnemy(Profession pr) {
        return Arrays.asList(Bot.getOpponent().worms).stream().filter(w -> w.profession.equals(pr)).findFirst()
                .orElse(new Worm(pr));
    }

    public static Worm getEnemyTechnologist() {
        return WormUtils.getEnemy(Profession.TECHNOLOGIST);
    }

    public static Worm getEnemyAgent() {
        return WormUtils.getEnemy(Profession.AGENT);
    }

    public static Worm getEnemyCommando() {
        return WormUtils.getEnemy(Profession.COMMANDO);
    }

    public static Technologist getOurTechnologist() {
        return Bot.getGameState().myPlayer.getTechnologist();
    }

    public static Agent getOurAgent() {
        return Bot.getGameState().myPlayer.getAgent();
    }

    public static Commando getOurCommando() {
        return Bot.getGameState().myPlayer.getCommando();
    }

    public static boolean isAlive(Worm w) {
        if (w == null)
            return false;
        if (w.id == -999)
            return false;

        return (w.health > 0);
    }
}
