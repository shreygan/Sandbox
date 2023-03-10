package ui;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import persistence.Writeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SandboxPanel extends JPanel implements KeyListener, Writeable {

    private boolean running;

    private Sandbox sandbox;
    private Game game;

    // EFFECTS: Initialze new JPanel for sandbox with corresponding listeners
    public SandboxPanel(Sandbox sandbox) {
        super(new GridBagLayout());

        this.sandbox = sandbox;

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        game = new Game(new Dimension(d.width / 3 * 2, d.height / 3 * 2));

        setBackground(Color.BLACK);

        addKeyListener(this);
        running = true;

        setPreferredSize(new Dimension(d.width / 3 * 2, d.height / 3 * 2));
    }

    // MODIFIES: this
    // EFFECTS: if panel is currently running, ticks game
    public void tick() {
        if (running) {
            game.tick();
        }
    }

    // MODIFIES: this
    // EFFECTS: paints this to JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Circle c : game.getCircles()) {
            g.setColor(c.getColor());
            g.fillOval(c.getXpos(), c.getYpos(), c.getDiam(), c.getDiam());
            g.setColor(Color.WHITE);
            g.drawString(c.getId() + "", (int) (c.getXpos() + c.getRad()), (int) (c.getYpos() + c.getRad()));
        }
    }

    // MODIFIES: this
    // EFFECTS: handles key pressed events on this
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            game.addCircle();
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            game.relaunchCircles();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            game.deleteCircles();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            sandbox.saveGame();
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            sandbox.loadGame();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            togglePause();
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            game.addCircle(new Circle(game.getWidth() / 2,
                    5, 0, -5, 25, Color.WHITE, 3, true));
        }

        if (!running && e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.tick();
        } else if (!running && e.getKeyCode() == KeyEvent.VK_LEFT) {
            // TODO MAKE GAME GO IN REVERSE
        }
    }

    private void togglePause() {
        running = !running;
    }

    public void displayMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("running", running);
        json.put("game", game.toJson());

        return json;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }


}
