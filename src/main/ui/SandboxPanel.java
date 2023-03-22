package ui;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import persistence.Writeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SandboxPanel extends JPanel
        implements KeyListener, MouseMotionListener, MouseListener, ComponentListener, Writeable {

    private boolean running;

    private Sandbox sandbox;
    private Game game;
    private GuiHandler guiHandler;
    private ActionHandler actionHandler;

    // EFFECTS: Initialze new JPanel for sandbox with corresponding listeners
    public SandboxPanel(Sandbox sandbox) {
        super(new GridBagLayout());

        running = true;

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        this.sandbox = sandbox;
        this.game = new Game(d);
        this.guiHandler = new GuiHandler(this, new GridBagConstraints());
        this.actionHandler = new ActionHandler(this, this.game);

        guiHandler.addPauseButton().addActionListener(e -> togglePause());
        guiHandler.addSaveButton().addActionListener(e -> this.sandbox.saveGame());
        guiHandler.addLoadButton().addActionListener(e -> this.sandbox.loadGame());
        guiHandler.addDeleteButton().addActionListener(e -> game.deleteCircles());
        guiHandler.addRelaunchButton().addActionListener(e -> game.relaunchCircles());
        guiHandler.addNewCircleButton().addActionListener(e -> game.addCircle());
        guiHandler.addSpacing();

        setBackground(Color.BLACK);
        setPreferredSize(d);
    }

    // MODIFIES: this
    // EFFECTS: if panel is currently running, ticks game
    public void tick() {
        if (running) {
            game.tick();
        }
    }

    // MODIFIES: this
    // EFFECTS: pauses/plays this sandbox
    public void togglePause() {
        running = !running;
    }

    // MODIFIES: this
    // EFFECTS: paints this to JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (actionHandler.getMouseDragInit() != null
                && actionHandler.getMouseDragCurr() != null
                && actionHandler.getMouseDragInit().y < actionHandler.getMouseDragCurr().y) {
            if (actionHandler.isMousePressed()) {
                paintMouseDragged(g);
            }
        }

        for (Circle c : game.getCircles()) {
            g.setColor(c.getColor());
            g.fillOval(c.getXpos(), c.getYpos(), c.getDiam(), c.getDiam());
            g.setColor(Color.WHITE);
            g.drawString(c.getId() + "", (int) (c.getXpos() + c.getRad()), (int) (c.getYpos() + c.getRad()));
        }
    }

    // REQUIRES: mousePressed true, mouseDragCurr mouseDragInit mouseColor != null
    // MODIFIES: this
    // EFFECTS: displays preview of circle created by mouse drag
    private void paintMouseDragged(Graphics g) {
        g.setColor(actionHandler.getMouseColor());

        int dim = (actionHandler.getMouseDragCurr().y - actionHandler.getMouseDragInit().y) * 2;

        g.fillOval(actionHandler.getMouseDragInit().x - dim / 2,
                actionHandler.getMouseDragInit().y - dim / 2, dim, dim);

        System.out.println("DRAGGING");
    }

    // MODIFIES: this
    // EFFECTS: shows message dialogue on this with given message and title
    public void displayMessage(String title, String message) {
        guiHandler.displayMessage(title, message);
    }

    // MODIFIES: this
    // EFFECTS: displays new joptionpane for creating new circle
    public String getJOptionPane(MouseEvent e) {
        return guiHandler.getJOptionPane(e);
    }

    // EFFECTS: saves current sandbox
    public void saveGame() {
        sandbox.saveGame();
    }

    // EFFECTS: loads to current sandbox
    public void loadGame() {
        sandbox.loadGame();
    }

    // MODIFIES: this
    // EFFECTS: handles key pressed events on this
    @Override
    public void keyPressed(KeyEvent e) {
        actionHandler.keyPressed(e);
    }

    // MODIFIES: this
    // EFFECTS: handles key typed event
    @Override
    public void keyTyped(KeyEvent e) {
        actionHandler.keyTyped(e);
    }

    // MODIFIES: this
    // EFFECTS: handles key released event
    @Override
    public void keyReleased(KeyEvent e) {
        actionHandler.keyReleased(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse dragged event, sets variables, and if mouse over circle
    //          simulates "dragging" circle
    @Override
    public void mouseDragged(MouseEvent e) {
        actionHandler.mouseDragged(e);
    }

    // MODIFIES: this
    // EFFECTS: sets current position of mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        actionHandler.mouseMoved(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse clicked event, displays new joptionpane if double click
    //          to add new circle
    @Override
    public void mouseClicked(MouseEvent e) {
        actionHandler.mouseClicked(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse pressed event, sets mouse position variables
    @Override
    public void mousePressed(MouseEvent e) {
        actionHandler.mousePressed(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse released event, either adds circle if mouse dragged on background
    //          or drops circle that was picked up with mouse drag
    @Override
    public void mouseReleased(MouseEvent e) {
        actionHandler.mouseReleased(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse entered event
    @Override
    public void mouseEntered(MouseEvent e) {
        actionHandler.mouseEntered(e);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse exited event
    @Override
    public void mouseExited(MouseEvent e) {
        actionHandler.mouseExited(e);
    }

    // MODIFIES: this
    // EFFECTS: handles being externally resized, updates game dimensions
    @Override
    public void componentResized(ComponentEvent e) {
        actionHandler.componentResized(e);
    }

    // MODIFIES: this
    // EFFECTS: handles component being externally moved
    @Override
    public void componentMoved(ComponentEvent e) {
        actionHandler.componentMoved(e);
    }

    // MODIFIES: this
    // EFFECTS: handles component being externally shown
    @Override
    public void componentShown(ComponentEvent e) {
        actionHandler.componentShown(e);
    }

    // MODIFIES: this
    // EFFECTS: handles component being externally hidden
    @Override
    public void componentHidden(ComponentEvent e) {
        actionHandler.componentHidden(e);
    }

    // EFFECTS: returns json object of this
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

    public void setGuiHandler(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }
}
