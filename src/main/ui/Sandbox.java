package ui;

import model.Game;

// Sandbox application
public class Sandbox {

    private static final int UPDATE_INTERVAL = 1000;

    private Game game;
    private Input in;

    // EFFECTS: starts new sandbox instance
    public Sandbox() {
        game = new Game();

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
        } else if (input.equals("f")) {
            System.exit(0);
        }
    }

}
