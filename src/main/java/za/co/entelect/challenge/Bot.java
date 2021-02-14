package za.co.entelect.challenge;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import za.co.entelect.challenge.command.Command;
import za.co.entelect.challenge.command.MoveCommand;
import za.co.entelect.challenge.command.SelectCommand;
import za.co.entelect.challenge.common.PlaneUtils;
import za.co.entelect.challenge.entities.Cell;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.MyWorm;
import za.co.entelect.challenge.entities.Opponent;
import za.co.entelect.challenge.entities.Worm;
import za.co.entelect.challenge.entities.worm.Agent;
import za.co.entelect.challenge.entities.worm.Commando;
import za.co.entelect.challenge.entities.worm.Technologist;
import za.co.entelect.challenge.enums.Profession;

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
        // Initializing player's worms
        Technologist technologist = gameState.myPlayer.getTechnologist();
        Commando commando = gameState.myPlayer.getCommando();
        Agent agent = gameState.myPlayer.getAgent();

        // Initializing a list of player's worms with worm's professions
        List<MyWorm> myWormsWithProf = Arrays.asList(new MyWorm[] { technologist, commando });

        // Initializing enemy's worms
        Worm enemyAgent = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.AGENT)).findFirst().orElse(new Worm(Profession.AGENT));
        Worm enemyTechnologist = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.TECHNOLOGIST)).findFirst()
                .orElse(new Worm(Profession.TECHNOLOGIST));
        Worm enemyCommando = Arrays.asList(Bot.getOpponent().worms).stream()
                .filter(w -> w.profession.equals(Profession.COMMANDO)).findFirst()
                .orElse(new Worm(Profession.COMMANDO));
        return null;
        // Strategi
        // Buat fungsi yang akan menentukan worm mana yang akan ditarget (Namanya fungsi
        // : setTargetWorm) returnnya Worm

        // Fungsi setTargetWorm
        // cari total jarak technologist musuh ke semua worm kita yang masih hidup
        // carng masihi total jarak agent musuh ke semua worm kita ya hidup
        // cari total jarak commando musuh ke semua worm kita yang masih hidup
        // Ambil 2 jarak terdekat
        // Jika selisih jarak 2 terdekat >= 20 (misalnya) return target yang jaraknya
        // paling dekat
        // Kalau engga return yang prioritasnya seperti dibawah
        // Agent > Technologist > Commando (Kalau agent musuh udh mati, return
        // technologist, kalau technologist udh mati return Commando)

        // Sekarang section nentuin mana yang nembak
        // Kalau agent kita bisa nembak banana bomb dan kena dua langsung (walaupun itu
        // bukan target awal) maka gas aja (buat fungsi untuk nyari titik ini)
        // Kalau kondisi di atas tidak dipenuhi, lanjut ke kondisi bawah
        // Kalau agent kita bisa nembak target kita sampai mati, langsung gas nembak aja
        // walaupun itu kena friend kita. Tapi sebisa mungkin menghindari friend.
        // Kalau kondisi di atas tidak dipenuhi, lanjut ke kondisi bawah
        // Kalau technologist kita yang bisa freeze target, freeze aja sambil
        // menghindari kena friend kita. Sekaligus ngecek apakah target udah di freeze.
        // Kalau udh, skip aja
        // Kalau kondisi di atas tidak dipenuhi, lanjut ke kondisi bawah
        // Kalau ada yang bisa nembak target kita, tembak aja
        // Kalau kondisi di atas tidak dipenuhi, lanjut ke kondisi bawah
        // Sekarang section mana yang jalan
        // Kalau worm kita ada yang jaraknya < 10 (misalnya) dari target, worm ini aja
        // yang digerakkan
        // Kalau kondisi di atas tidak dipenuhi, lanjut ke kondisi bawah
        // Gerakkan worm kedua terdekat dari target.
    }
}

// Worm enemyWormBisaDitembakTechnologist =
// Optional.ofNullable(WormUtils.getFirstWormInRange(technologist))
// .orElse(new Worm(Profession.TECHNOLOGIST));
// Worm enemyWormBisaDitembakCommando =
// Optional.ofNullable(WormUtils.getFirstWormInRange(commando))
// .orElse(new Worm(Profession.COMMANDO));
// Worm enemyWormBisaDitembakAgent =
// Optional.ofNullable(WormUtils.getFirstWormInRange(agent))
// .orElse(new Worm(Profession.AGENT));

// if (enemyAgent.health > 0) { // Greedy by banana
// Worm wormYangBisaDitembak = WormUtils.getFirstWormInRange(technologist);

// if (wormYangBisaDitembak != null) {
// if (wormYangBisaDitembak.id == enemyAgent.id) {
// if (enemyAgent.roundsUntilUnfrozen == 0) {
// return new SelectCommand(technologist.id, new
// SnowballCommand(enemyAgent.position));
// }
// return new SelectCommand(technologist.id, new ShootCommand(
// PlaneUtils.resolveDirection(technologist.position,
// wormYangBisaDitembak.position)));
// }
// }

// Cell nextCell = PlaneUtils.nextLine(technologist.position,
// enemyAgent.position);

// if (nextCell.type == CellType.DIRT) {
// return new SelectCommand(technologist.id, new DigCommand(nextCell));
// }
// return new SelectCommand(technologist.id, new MoveCommand(nextCell));
// } else if (enemyTechnologist.health > 0) { // Greedy by technologist (after
// the death of enemy's agent)
// if (enemyWormBisaDitembakTechnologist.id == enemyTechnologist.id &
// technologist.health > 0) {
// if (enemyWormBisaDitembakTechnologist.roundsUntilUnfrozen == 0 &
// technologist.snowballs.count > 0) {
// return new SelectCommand(technologist.id, new
// SnowballCommand(enemyTechnologist.position));
// } else {
// return new SelectCommand(technologist.id, new ShootCommand(PlaneUtils
// .resolveDirection(technologist.position,
// enemyWormBisaDitembakTechnologist.position)));
// }
// } else if (enemyWormBisaDitembakCommando.id == enemyTechnologist.id &
// commando.health > 0) {
// return new SelectCommand(commando.id, new ShootCommand(
// PlaneUtils.resolveDirection(commando.position,
// enemyWormBisaDitembakCommando.position)));
// } else if (enemyWormBisaDitembakAgent.id == enemyTechnologist.id) {
// return new SelectCommand(agent.id, new ShootCommand(
// PlaneUtils.resolveDirection(agent.position,
// enemyWormBisaDitembakAgent.position)));
// }
// Worm wormToBeSelected = WormUtils.targetTechnologist();

// Cell nextCell = PlaneUtils.nextLine(wormToBeSelected.position,
// enemyTechnologist.position);
// if (nextCell.type == CellType.DIRT) {
// return new SelectCommand(wormToBeSelected.id, new DigCommand(nextCell));
// } else {
// return new SelectCommand(wormToBeSelected.id, new MoveCommand(nextCell));
// }
// } else if (enemyCommando.health > 0) { // Greedy by Commando (last worm)
// if (enemyWormBisaDitembakCommando.id == enemyCommando.id) {
// return new SelectCommand(commando.id, new ShootCommand(
// PlaneUtils.resolveDirection(commando.position,
// enemyWormBisaDitembakCommando.position)));
// } else if (enemyWormBisaDitembakAgent.id == enemyCommando.id) {
// return new SelectCommand(agent.id, new ShootCommand(
// PlaneUtils.resolveDirection(agent.position,
// enemyWormBisaDitembakAgent.position)));
// } else if (enemyWormBisaDitembakTechnologist.id == enemyCommando.id) {
// return new SelectCommand(technologist.id, new ShootCommand(PlaneUtils
// .resolveDirection(technologist.position,
// enemyWormBisaDitembakTechnologist.position)));
// }
// Worm furthestWorm = WormUtils.getFurthestWorm(myWormsWithProf,
// enemyCommando.position);

// Cell nextCell = PlaneUtils.nextLine(furthestWorm.position,
// enemyCommando.position);
// if (nextCell.type == CellType.DIRT) {
// return new SelectCommand(furthestWorm.id, new DigCommand(nextCell));
// } else {
// return new SelectCommand(furthestWorm.id, new MoveCommand(nextCell));
// }
// }

// return null;