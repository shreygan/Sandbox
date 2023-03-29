package ui;

import model.Circle;
import model.Game;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Scanner;

// Handles all user interactions for SandboxPanel
public class ActionHandler {

    private boolean mousePressed;
    private boolean mouseOverCircle;
    private Point mousePos;
    private Point mouseDragInit;
    private Point mouseDragCurr;
    private long mouseDragTime;
    private Color mouseColor;

    private SandboxPanel panel;
    private Game game;

    // REQUIRES: panel.game == game
    // EFFECTS: initalizes new action handler for given panel and game
    public ActionHandler(SandboxPanel panel, Game game) {
        panel.addKeyListener(panel);
        panel.addMouseListener(panel);
        panel.addMouseMotionListener(panel);
        panel.addComponentListener(panel);

        this.panel = panel;
        this.game = game;

        mouseOverCircle = false;
        mousePressed = false;
    }

    // MODIFIES: this
    // EFFECTS: removes all listeners from panel
    public void removeAllListeners() {
        panel.removeKeyListener(panel);
        panel.removeMouseListener(panel);
        panel.removeMouseMotionListener(panel);
        panel.removeComponentListener(panel);
    }

    // MODIFIES: this
    // EFFECTS: handles key pressed events on this
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            game.addCircle();
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            game.relaunchCircles();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            if (mousePos != null) {
                game.deleteCircles(mousePos);
            } else {
                game.deleteCircles();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            panel.saveGame();
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            panel.loadGame();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            panel.togglePause();
        } else if (!panel.isRunning() && e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.tick();
        } else if (!panel.isRunning() && e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.untick();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles mouse dragged event, sets variables, and if mouse over circle
    //          simulates "dragging" circle
    public void mouseDragged(MouseEvent e) {
        Point mouseDragCurrTemp = e.getPoint();

        int diam = (mouseDragCurrTemp.y - mouseDragInit.y) * 2;
        if (!game.overlaps(mouseDragInit.x - diam / 2, mouseDragInit.y - diam / 2, diam)) {
            mouseDragCurr = mouseDragCurrTemp;
        }

        if (mouseOverCircle) {
            game.moveCircle(mouseDragCurr);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets current position of mouse
    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();
    }

    // MODIFIES: this
    // EFFECTS: handles mouse clicked event, displays new joptionpane if double click
    //          to add new circle
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (mouseOverCircle) {
                return;
            }

            do {
                String result = panel.getJOptionPane(e);
                if (!handleJOptionPane(result, e.getPoint())) {
                    break;
                }
            } while (true);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes and handles joptionpane input
    private boolean handleJOptionPane(String result, Point p) {
        if (result == null || result.equals("")) {
            return false;
        }

        int[] output = new int[3];
        if (!result.matches("[^0-9]*")) {
            Scanner sc = new Scanner(result).useDelimiter("[^0-9^-]+");

            int i;
            for (i = 0; i < 3; i++) {
                if (!sc.hasNextInt()) {
                    break;
                }
                output[i] = sc.nextInt();
            }

            if (i == 3) {
                game.addCircle(p, output[0], output[1], output[2]);
                return false;
            }
        }

        return true;
    }

    // MODIFIES: this
    // EFFECTS: handles mouse pressed event, sets mouse position variables
    public void mousePressed(MouseEvent e) {
        mouseDragInit = e.getPoint();

        mouseColor = new Color((int) (Math.random() * 0x1000000));

        Circle dragged = game.overlaps(mouseDragInit);

        if (dragged != null) {
            dragged.setVel(0, 0);
            dragged.setAccelerating(false);

            game.setCircleDragged(dragged);

            mouseDragTime = System.currentTimeMillis();
            mouseOverCircle = true;
        } else {
            game.setCircleDragged(null);

            mousePressed = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: handles mouse released event, either adds circle if mouse dragged on background
    //          or drops circle that was picked up with mouse drag
    public void mouseReleased(MouseEvent e) {
        if (mousePressed && mouseDragInit != null && mouseDragCurr != null && mouseDragInit.y < mouseDragCurr.y) {
            game.addCircle(mouseDragInit, mouseDragCurr, mouseColor);
        }

        if (mouseOverCircle && mouseDragInit != null && mouseDragCurr != null) {
            game.releaseCircle(mouseDragInit, mouseDragCurr, (System.currentTimeMillis() - mouseDragTime));
            mouseOverCircle = false;
        } else if (mouseOverCircle && mouseDragInit != null) {
            mouseOverCircle = false;
            game.releaseCircle();
        }

        mousePressed = false;
        mouseDragInit = null;
        mouseDragCurr = null;
    }

    // MODIFIES: this
    // EFFECTS: handles being externally resized, updates game dimensions
    public void componentResized(ComponentEvent e) {
        game.setDimension(panel.getWidth(), panel.getHeight());
    }

    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    public void componentHidden(ComponentEvent e) {
        // do nothing
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public Point getMouseDragInit() {
        return mouseDragInit;
    }

    public Point getMouseDragCurr() {
        return mouseDragCurr;
    }

    public Color getMouseColor() {
        return mouseColor;
    }
}
