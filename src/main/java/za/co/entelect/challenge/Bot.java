package za.co.entelect.challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import za.co.entelect.challenge.command.Command;
import za.co.entelect.challenge.command.DigCommand;
import za.co.entelect.challenge.command.MoveCommand;
import za.co.entelect.challenge.command.SelectCommand;
import za.co.entelect.challenge.command.ShootCommand;
import za.co.entelect.challenge.common.GameUtils;
import za.co.entelect.challenge.common.PlaneUtils;
import za.co.entelect.challenge.common.StrategyUtils;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Opponent;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.enums.CellType;

public class Bot {
    private static Bot instance;
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

    private MyWorm getCurrentWorm() {
        return Arrays.stream(gameState.myPlayer.worms).filter(myWorm -> myWorm.id == gameState.currentWormId)
                .findFirst().get();
    }

    public Command run() {
        // Set worm target and set player worm to be selected
        Worm targetWorm = StrategyUtils.setTargetWorm();

        // List<Cell> fdLines = new ArrayList<>(8 * selectedWorm.weapon.range);
        // PlaneUtils.constructFireDirectionLines(GameUtils.lookup(selectedWorm.position),
        // selectedWorm.weapon.range)
        // .forEach(l -> fdLines.addAll(l));

        // boolean canTarget = fdLines.stream().filter(c ->
        // c.equals(targetWorm.position)).findAny().isPresent();

        Cell nextCell = PlaneUtils.nextLine(selectedWorm.position, targetWorm.position);

        if (nextCell.type == CellType.DIRT) {
            return new SelectCommand(selectedWorm, new DigCommand(nextCell));
        }
        return new SelectCommand(selectedWorm, new MoveCommand(nextCell));
        // }

        // Strategi sekarang
        // Jika ini bukan roundnya agent dan agent bisa nembak 2 target langsung, pake
        // select aja (sekalian cek masih bisa select atau engga)
        // Jika ini bukan roundnya technologist dan technologist bisa nembak 2 target
        // langsung, pake select aja (sekalian cek masih bisa select atau engga)

        // Strategi individu agent
        // Kalau ada yang bisa di banana bomb 2 langsung, gas aja
        // Kalau ada yang bisa dibanana bomb mati, gas aja
        // Kalau ada yang bisa ditembak, gas aja
        // Cari target (Agent > Technologist > Commando)
        // Gerak ke target

        // Strategi individu technologist
        // Kalau ada yang bisa di freeze 2 langsung, gas aja
        // Kalau ada yang bisa ditembak, tembak aja
        // Cari target (Agent > Technologist > Commando)
        // Kalau target bisa di freeze, freeze aja
        // Kalau ada yang bisa ditembak, gas aja
        // Gerak ke target

        // Strategi individu commando
        // Kalau ada yang bisa ditembak, tembak aja
        // Cari target (Agent > Technologist > Commando)
        // Gerak ke target.

    }
}
