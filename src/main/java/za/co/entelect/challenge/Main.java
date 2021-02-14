package za.co.entelect.challenge;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;

import za.co.entelect.challenge.command.Command;
import za.co.entelect.challenge.entities.GameState;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.common.*;

import java.util.List;

public class Main {
    private static final String ROUNDS_DIRECTORY = "rounds";
    private static final String STATE_FILE_NAME = "state.json";

    /**
     * Read the current state, feed it to the bot, get the output and print it to
     * stdout
     *
     * @param args the args
     **/
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        Random random = new Random(System.nanoTime());

        // while (true) {
        // try {
        // int roundNumber = sc.nextInt();

        // String statePath = String.format("./%s/%d/%s", ROUNDS_DIRECTORY, roundNumber,
        // STATE_FILE_NAME);
        // String state = new String(Files.readAllBytes(Paths.get(statePath)));

        // GameState gameState = gson.fromJson(state, GameState.class);
        // Command command = new Bot(random, state, gameState).run();

        // System.out.println(String.format("C;%d;%s", roundNumber, command.render()));

        // break;
        // } catch (Exception e) {
        // e.printStackTrace();
        // sc.close();
        // break;
        // }
        // }
        try {
            String statePath = String.format("./%s/%d/%s", ROUNDS_DIRECTORY, 1, STATE_FILE_NAME);
            String state = new String(Files.readAllBytes(Paths.get(statePath)));

            GameState gameState = gson.fromJson(state, GameState.class);
            // Command command = new Bot(random, state, gameState).run();
            Bot bot = new Bot(random, state, gameState);
            // List<List<Cell>> testRange = PlaneUtils.constructFireDirectionLines(new
            // Cell(10, 10), 4);
            // int size = 0;
            // for (int i = 0; i < testRange.size(); i++) {
            // for (int j = 0; j < testRange.get(i).size(); j++) {
            // System.out.println(testRange.get(i).get(j).x + " " +
            // testRange.get(i).get(j).y);
            // size += 1;
            // }
            // }
            // System.out.println(size);

            // List<Cell> shootingArea = WormUtils.getShootingArea(new Cell(10, 10), 2);
            // System.out.println(shootingArea.size());
            // for (Cell cell : shootingArea) {
            // System.out.println(cell.x + " " + cell.y);
            // }

        } catch (Exception e) {
            e.printStackTrace();
            sc.close();
        }
    }
}
