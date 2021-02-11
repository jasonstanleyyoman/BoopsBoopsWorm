package za.co.entelect.challenge;

import com.google.gson.Gson;
import za.co.entelect.challenge.entities.GameState;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

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
        // Scanner sc = new Scanner(System.in);
        Gson gson = new Gson();
        Random random = new Random(System.nanoTime());

        while (true) {
            try {
                // int roundNumber = sc.nextInt();

                // String statePath = String.format("./%s/%d/%s", ROUNDS_DIRECTORY, roundNumber,
                // STATE_FILE_NAME);
                String statePath = "../data.json";
                String state = new String(Files.readAllBytes(Paths.get(statePath)));

                GameState gameState = gson.fromJson(state, GameState.class);
                new Bot(random, state, gameState);

                System.out.println(state.length());
                // List<Cell> lines = PlaneUtils.generateLine(new Cell(1, 2), new Cell(3, 7));

                // for (int i = 0; i < lines.size(); i++) {
                // System.out.println(String.format("(%d, %d)", lines.get(i).x,
                // lines.get(i).y));
                // }

                break;
                // Command command = new Bot(random, state, gameState).run();

                // System.out.println(String.format("C;%d;%s", roundNumber, command.render()));
            } catch (Exception e) {
                e.printStackTrace();
                break;
                // sc.close();
            }
        }
    }
}
