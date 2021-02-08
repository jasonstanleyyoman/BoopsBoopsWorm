package za.co.entelect.challenge.common;

import java.util.Arrays;
import java.util.List;
import za.co.entelect.challenge.Bot;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;

public class StateUtils {

    public static List<MyWorm> getMyWormList() {
        GameState gameState = Bot.getGameState();
        return Arrays.asList(gameState.myPlayer.worms);
    }
    


}