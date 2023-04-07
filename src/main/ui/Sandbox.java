package ui;

import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Sandbox application
public class Sandbox extends JFrame {

    private static final int UPDATE_INTERVAL = 10;

    private static final String JSON_LOCATION = "./data/game.json";
    private JsonReader reader;
    private JsonWriter writer;

    private SandboxPanel panel;

    // EFFECTS: starts new sandbox instance
    public Sandbox(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);

        panel = new SandboxPanel(this);
        panel.setFocusable(true);
        add(panel);

        pack();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (model.Event event : EventLog.getInstance()) {
                    System.out.println(event);
                    System.out.println();
                }
            }
        });

        reader = new JsonReader(JSON_LOCATION);
        writer = new JsonWriter(JSON_LOCATION);

        startTimer();
    }

    // MODIFIES: this
    // EFFECTS: starts timer that updates JFrame every UPDATE_INTERVAL
    private void startTimer() {
        Timer timer = new Timer(UPDATE_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
                panel.tick();
            }
        });

        timer.start();
    }

    // EFFECTS: saves current running panel to json file
    public void saveGame() {
        try {
            writer.open();
            writer.write(panel);
            writer.close();
            panel.displayMessage("Save Sandbox", "Saved successfully!");
        } catch (FileNotFoundException e) {
            panel.displayMessage("Save Sandbox", "Error in saving to file!");
        }
    }

    // EFFECTS: loads new panel from currently saved file
    public void loadGame() {
        try {
            SandboxPanel panel = reader.read(this);

            getContentPane().removeAll();

            this.panel = panel;
            getContentPane().add(panel);
            panel.setFocusable(true);
            panel.requestFocus();
            validate();
            setVisible(true);
        } catch (IOException e) {
            panel.displayMessage("Load Sandbox", "Error in loading from file!");
        }
    }
}
