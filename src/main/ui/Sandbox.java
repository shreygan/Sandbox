package ui;

import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

// Sandbox application
public class Sandbox {

    private static final int UPDATE_INTERVAL = 1000;

    private static final String JSON_LOCATION = "./data/game.json";
    private JsonReader reader;
    private JsonWriter writer;

    private Game game;
    private Input in;

    // EFFECTS: starts new sandbox instance
    public Sandbox() {
        game = new Game();

        reader = new JsonReader(JSON_LOCATION);
        writer = new JsonWriter(JSON_LOCATION);

        in = new Input();
        in.start();

        runSandbox();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runSandbox() {
        while (true) {
            try {
                Thread.sleep(UPDATE_INTERVAL);

                if (in.hasNewInput()) {
                    handleInput(in.getNextIn());
                } else {
                    if (game.isRunning()) {
                        System.out.println(game.getOutput());
                        System.out.println("-----------------");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void handleInput(String input) {
        if (input.equals("c")) {
            game.addCircle();
        } else if (input.equals("p")) {
            game.setRunning(!game.isRunning());
        } else if (input.equals("r")) {
            game.relaunchCircles();
        } else if (input.equals("d")) {
            game.deleteCircles();
        } else if (input.equals("s")) {
            saveGame();
        } else if (input.equals("l")) {
            loadGame();
        } else if (input.equals("f")) {
            System.exit(0);
        }
    }

    // EFFECTS: saves current running game to json file
    private void saveGame() {
        try {
            writer.open();
            writer.write(game);
            writer.close();
            System.out.println("Saved to " + JSON_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Error in writing to given file: " + JSON_LOCATION);
            e.printStackTrace();
        }
    }


    // EFFECTS: loads new game from currently saved file
    private void loadGame() {
        try {
            game = reader.read();
            System.out.println("Loaded from " + JSON_LOCATION);
        } catch (IOException e) {
            System.out.println("Error in reading from given file: " + JSON_LOCATION);
        }
    }
}
