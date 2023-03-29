package ui;

import model.Circle;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Scanner;

public class ActionHandler {

    private boolean mousePressed;
    private boolean mouseOnCircle;
    private Point mousePos;
    private Point mouseDragInit;
    private Point mouseDragCurr;
    private long mouseDragTime;
    private Color mouseColor;

    private SandboxPanel panel;
    private Game game;

    public ActionHandler(SandboxPanel panel, Game game) {
        panel.addKeyListener(panel);
        panel.addMouseListener(panel);
        panel.addMouseMotionListener(panel);
        panel.addComponentListener(panel);

        this.panel = panel;
        this.game = game;

        mouseOnCircle = false;
        mousePressed = false;
    }

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
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            game.rollOff();

//            game.addCircle(new Point(1000, 500), 0, 0, 200);
//            game.addCircle(new Point(2000, 750), -15, 0, 250);
//
//            game.addCircle(new Point(250, 100), 0, 15, 250);
//            game.addCircle(new Point(800, 750), -5, -5, 250);
        }

        if (!panel.isRunning() && e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.tick();
        } else if (!panel.isRunning() && e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.untick();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles mouse dragged event, sets variables, and if mouse over circle
    //          simulates "dragging" circle
    public void mouseDragged(MouseEvent e) {
        mouseDragCurr = e.getPoint();

        if (mouseOnCircle) {
            game.moveCircle(mouseDragCurr);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets current position of mouse
    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();

//        Circle c = game.overlaps(mousePos);
//        if (c != null) {
//            System.out.print("c.getXvel(): " + c.getXvel());
//            System.out.println("  c.getYvel(): " + c.getYvel());
//        }
    }

    // MODIFIES: this
    // EFFECTS: handles mouse clicked event, displays new joptionpane if double click
    //          to add new circle
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (mouseOnCircle) {
                return;
            }

            int[] output = new int[3];
            do {
                String result = panel.getJOptionPane(e);

                if (result == null) {
                    break;
                }

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
                        game.addCircle(e.getPoint(), output[0], output[1], output[2]);
                        break;
                    }
                }
            } while (true);
        }
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
            mouseOnCircle = true;
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
            // TODO make sure new circle can't be dragged into existing circle
        }

        if (mouseOnCircle && mouseDragInit != null && mouseDragCurr != null) {
//            double t = (System.currentTimeMillis() - mouseDragTime) / 1000f;
            game.releaseCircle(mouseDragInit, mouseDragCurr, (System.currentTimeMillis() - mouseDragTime));
            mouseOnCircle = false;
        } else if (mouseOnCircle && mouseDragInit != null) {
            mouseOnCircle = false;
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
