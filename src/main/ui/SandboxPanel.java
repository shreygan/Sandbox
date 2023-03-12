package ui;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import persistence.Writeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class SandboxPanel extends JPanel
        implements KeyListener, MouseMotionListener, MouseListener, ComponentListener, Writeable {

    private boolean mousePressed;
    private boolean mouseOnCircle;
    private Point mousePos;
    private Point mouseDragInit;
    private Point mouseDragCurr;
    private Color mouseColor;

    private boolean running;

    private Sandbox sandbox;
    private Game game;

    private GridBagConstraints gbc;

    // EFFECTS: Initialze new JPanel for sandbox with corresponding listeners
    public SandboxPanel(Sandbox sandbox) {
        super(new GridBagLayout());

        this.sandbox = sandbox;

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        game = new Game(d);
//        game = new Game(new Dimension(d.width / 3 * 2, d.height / 3 * 2));
        gbc = new GridBagConstraints();

        setBackground(Color.BLACK);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);

        addPauseButton();
        addSaveButton();
        addLoadButton();
        addDeleteButton();
        addRelaunchButton();
        addNewCircleButton();
        addSpacing();

        mouseOnCircle = false;
        mousePressed = false;
        running = true;

        setPreferredSize(d);
//        setPreferredSize(new Dimension(d.width / 3 * 2, d.height / 3 * 2));
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

        if (mouseDragInit != null && mouseDragCurr != null && mouseDragInit.y < mouseDragCurr.y) {
            if (mousePressed) {
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
        g.setColor(mouseColor);

        int dim = (mouseDragCurr.y - mouseDragInit.y) * 2;

        g.fillOval(mouseDragInit.x - dim / 2, mouseDragInit.y - dim / 2, dim, dim);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds pause button to jpanel
    private void addPauseButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Pause/Play");
        button.setIcon(new ImageIcon("./data/pauseIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds save button to jpanel
    private void addSaveButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Save from JSON file");
        button.setIcon(new ImageIcon("./data/saveIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sandbox.saveGame();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds load button to jpanel
    private void addLoadButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Load from JSON file");
        button.setIcon(new ImageIcon("./data/loadIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sandbox.loadGame();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds delete button to jpanel
    private void addDeleteButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Delete all circles");
        button.setIcon(new ImageIcon("./data/trashIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.deleteCircles();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds relaunch button to jpanel
    private void addRelaunchButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Relaunch all circles");
        button.setIcon(new ImageIcon("./data/launchIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.relaunchCircles();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds relaunch button to jpanel
    private void addNewCircleButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Add new circle");
        button.setIcon(new ImageIcon("./data/circleIcon.png"));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.addCircle();
            }
        });

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        add(button, gbc);
    }

    // REQUIRES: gbc != null
    // MODIFIES: this
    // EFFECTS: adds spacing between buttons to jpanel
    private void addSpacing() {
        gbc.ipadx = 0;
        gbc.ipady = 0;

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(Box.createHorizontalStrut(5), gbc);

        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 7;
        gbc.gridy = 1;
        add(Box.createHorizontalStrut(getWidth()), gbc);

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(Box.createVerticalStrut(10), gbc);

        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(Box.createVerticalStrut(getHeight()), gbc);
    }

    // MODIFIES: this
    // EFFECTS: shows message dialogue on this with given message and title
    public void displayMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.PLAIN_MESSAGE);
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
            game.deleteCircles(mousePos);
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

    // MODIFIES: this
    // EFFECTS: pauses/plays this sandbox
    private void togglePause() {
        running = !running;
    }

    // MODIFIES: this
    // EFFECTS: handles mouse dragged event, sets variables, and if mouse over circle
    //          simulates "dragging" circle
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDragCurr = e.getPoint();

        if (mouseOnCircle) {
            game.moveCircle(mouseDragCurr);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets current position of mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();
    }

    // MODIFIES: this
    // EFFECTS: handles mouse clicked event, displays new joptionpane if double click
    //          to add new circle
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int[] output = new int[3];
            do {
                String result = getJOptionPane(e);

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
    // EFFECTS: displays new joptionpane for creating new circle
    private String getJOptionPane(MouseEvent e) {
        return JOptionPane.showInputDialog(this,
                "Enter \"{x velocity} {y velocity} {radius}\"",
                "New Circle at (" + e.getPoint().x + ", " + e.getPoint().y + ")",
                JOptionPane.PLAIN_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: handles mouse pressed event, sets mouse position variables
    @Override
    public void mousePressed(MouseEvent e) {
        mouseDragInit = e.getPoint();

        mouseColor = new Color((int) (Math.random() * 0x1000000));

        Circle dragged = game.overlaps(mouseDragInit);

        if (dragged != null) {
            dragged.setVel(0, 0);
            dragged.setAccelerating(false);

            game.setCircleDragged(dragged);

            mouseOnCircle = true;
        } else {
            mousePressed = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: handles mouse released event, either adds circle if mouse dragged on background
    //          or drops circle that was picked up with mouse drag
    @Override
    public void mouseReleased(MouseEvent e) {
        if (mousePressed && mouseDragInit != null && mouseDragCurr != null && mouseDragInit.y < mouseDragCurr.y) {
            game.addCircle(mouseDragInit, mouseDragCurr, mouseColor);
        }

        if (mouseOnCircle && mouseDragInit != null && mouseDragCurr != null) {
            game.releaseCircle();
            mouseOnCircle = false;
        }

        mousePressed = false;
        mouseDragInit = null;
        mouseDragCurr = null;
    }

    // MODIFIES: this
    // EFFECTS: handles being externally resized, updates game dimensions
    @Override
    public void componentResized(ComponentEvent e) {
        game.setDimension(getWidth(), getHeight());
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
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // do nothing
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
}
