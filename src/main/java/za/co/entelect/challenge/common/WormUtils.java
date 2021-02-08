package za.co.entelect.challenge.common;

import java.util.Arrays;

import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;

public class WormUtils {

    public static MyWorm getCurrentWorm() {
        GameState gameState = Bot.getGameState();
        return Arrays.stream(gameState.myPlayer.worms).filter(myWorm -> myWorm.id == gameState.currentWormId)
                .findFirst().get();
    }

}
