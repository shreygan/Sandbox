package ui;

import model.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

// Handles all graphical elements for SandboxPanel
public class GraphicsHandler {

    private SandboxPanel panel;
    private GridBagConstraints gbc;

    // REQUIRES: panel has grid bag layout
    // EFFECTS: initalizes new graphics handler for given panel and gbc
    public GraphicsHandler(SandboxPanel panel, GridBagConstraints gbc) {
        this.panel = panel;
        this.gbc = gbc;
    }

    // REQUIRES: g != null and is graphics object for panel
    // MODIFIES: this, g
    // EFFECTS:
    public void paintComponent(Graphics g) {
        ActionHandler actionHandler = panel.getActionHandler();

        if (actionHandler.getMouseDragInit() != null
                && actionHandler.getMouseDragCurr() != null
                && actionHandler.getMouseDragInit().y < actionHandler.getMouseDragCurr().y
                && actionHandler.isMousePressed()) {
            paintMouseDragged(g, actionHandler);
        }

        for (Circle c : panel.getGameCircles()) {
            g.setColor(c.getColor());
            g.fillOval(c.getXpos(), c.getYpos(), c.getDiam(), c.getDiam());
            g.setColor(Color.WHITE);
            g.drawString(c.getId() + "", (int) (c.getXpos() + c.getRad()), (int) (c.getYpos() + c.getRad()));
        }
    }

    // REQUIRES: mousePressed true, mouseDragCurr mouseDragInit mouseColor != null
    // MODIFIES: this
    // EFFECTS: displays preview of circle created by mouse drag
    private void paintMouseDragged(Graphics g, ActionHandler actionHandler) {
        g.setColor(actionHandler.getMouseColor());

        int diam = (actionHandler.getMouseDragCurr().y - actionHandler.getMouseDragInit().y) * 2;

        g.fillOval(actionHandler.getMouseDragInit().x - diam / 2,
                actionHandler.getMouseDragInit().y - diam / 2, diam, diam);
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds pause button to panel and returns it
    public JButton addPauseButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Pause/Play");
        button.setIcon(new ImageIcon("./data/pauseIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds save button to panel and returns it
    public JButton addSaveButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Save from JSON file");
        button.setIcon(new ImageIcon("./data/saveIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds load button to panel and returns it
    public JButton addLoadButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Load from JSON file");
        button.setIcon(new ImageIcon("./data/loadIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds delete button to panel and returns it
    public JButton addDeleteButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Delete all circles");
        button.setIcon(new ImageIcon("./data/trashIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds relaunch button to panel and returns it
    public JButton addRelaunchButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Relaunch all circles");
        button.setIcon(new ImageIcon("./data/launchIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds relaunch button to panel and returns it
    public JButton addNewCircleButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setToolTipText("Add new circle");
        button.setIcon(new ImageIcon("./data/circleIcon.png"));

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        panel.add(button, gbc);

        return button;
    }

    // REQUIRES: panel != null and gbc != null
    // MODIFIES: this
    // EFFECTS: adds spacing between buttons to panel
    public void addSpacing() {
        gbc.ipadx = 0;
        gbc.ipady = 0;

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(Box.createHorizontalStrut(5), gbc);

        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 7;
        gbc.gridy = 1;
        panel.add(Box.createHorizontalStrut(panel.getWidth()), gbc);

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(Box.createVerticalStrut(10), gbc);

        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(panel.getHeight()), gbc);
    }

    // REQUIRES: panel != null
    // MODIFIES: this
    // EFFECTS: shows message dialogue on panel with given message and title
    public void displayMessage(String title, String message) {
        JOptionPane.showMessageDialog(panel, message, title,
                JOptionPane.PLAIN_MESSAGE);
    }

    // REQUIRES: panel != null
    // MODIFIES: this
    // EFFECTS: displays new joptionpane for creating new circle
    public String getJOptionPane(MouseEvent e) {
        return JOptionPane.showInputDialog(panel,
                "Enter \"{x velocity} {y velocity} {radius}\"",
                "New Circle at (" + e.getPoint().x + ", " + e.getPoint().y + ")",
                JOptionPane.PLAIN_MESSAGE);
    }
}
