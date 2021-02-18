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
        Worm enemyCommando = Bot.getOpponentWorms().stream().filter(w -> w.profession == Profession.COMMANDO)
                .findFirst().orElse(new Worm(Profession.COMMANDO));
        Worm enemyTechnologist = Bot.getOpponentWorms().stream().filter(w -> w.profession == Profession.TECHNOLOGIST)
                .findFirst().orElse(new Worm(Profession.TECHNOLOGIST));
        // Set worm target and set player worm to be selected
        // Worm targetWorm = StrategyUtils.setTargetWorm();

        // List<Cell> fdLines = new ArrayList<>(8 * selectedWorm.weapon.range);
        // PlaneUtils.constructFireDirectionLines(GameUtils.lookup(selectedWorm.position),
        // selectedWorm.weapon.range)
        // .forEach(l -> fdLines.addAll(l));

        // boolean canTarget = fdLines.stream().filter(c ->
        // c.equals(targetWorm.position)).findAny().isPresent();

        // Cell nextCell = PlaneUtils.nextLine(selectedWorm.position,
        // targetWorm.position);

        // if (nextCell.type == CellType.DIRT) {
        // return new SelectCommand(selectedWorm, new DigCommand(nextCell));
        // }
        // return new SelectCommand(selectedWorm, new MoveCommand(nextCell));
        // }
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
                // Strategi individu agent
                // Kalau ada yang bisa di banana bomb 2 langsung, gas aja
                // Kalau ada yang bisa dibanana bomb mati, gas aja
                // Cari target (Agent > Technologist > Commando)
                // Kalau target bisa di banana, gas aja
                // Kalau ada yang bisa ditembak, gas aja
                // Gerak ke target
                targetCell = StrategyUtils.agentCanShootTwoWorms();

                if (!isAgentMoving) {
                    isAgentMoving = StrategyUtils.agentStartsMoving()
                            || StrategyUtils.countMove(currentWorm.position, enemyCommando.position) <= 7
                            || StrategyUtils.countMove(currentWorm.position, enemyTechnologist.position) <= 7;
                    return new DoNothingCommand();
                }
                if (targetCell != null) {
                    return new BananaCommand(targetCell);
                }
                // target cell = ambil cell yang bisa banana musuh sampai mati (kalau ada yang
                // ga kena teman,
                // pilih cell itu aja, kalau terpaksa kena teman, yaudah gas aja)
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

                // targetCell = getArea(target); (cari cell yang bisa nembak target dan
                // diusahakan ga kena teman)

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
                // Gerak ke target
                if (nextCell == null) {
                    nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);
                }

                if (nextCell.type == CellType.DIRT) {
                    return new DigCommand(nextCell);
                } else {
                    return new MoveCommand(nextCell);
                }
            case TECHNOLOGIST:
                // Strategi individu technologist
                // Kalau ada yang bisa di freeze 2 langsung, gas aja
                // Cari target (Agent > Technologist > Commando)
                // Kalau target bisa di freeze, freeze aja
                // Kalau target bisa di tembak, gas aja
                // Kalau ada yang bisa ditembak (selain target), gas aja
                // Gerak ke target
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

                // targetCell = getArea(ta+rget); (cari cell yang bisa snowball target dan
                // diusahakan ga kena teman)
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
                // Strategi individu commando
                // Kalau ada yang bisa ditembak, tembak aja
                // Cari target (Agent > Technologist > Commando)
                // Gerak ke target.
                targetCell = StrategyUtils.getAvailableShoot(currentWorm);
                if (targetCell != null) {
                    Direction direction = PlaneUtils.resolveDirection(currentWorm.position, targetCell);
                    return new ShootCommand(direction);
                }

                target = StrategyUtils.setTargetWorm(currentWorm);

                // Gerak ke target;
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