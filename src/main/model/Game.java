package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Represents the sandbox game with all it's circles
public class Game implements Writeable {

    public static final int STEPS_PER_UPDATE = 5;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    private ArrayList<Circle> circles;
    private int id;

    private boolean isRunning;

    private Random rand;

    // EFFECTS: initalizes new sandbox game
    public Game() {
        circles = new ArrayList<>();
        rand = new Random();

        id = 1;
        isRunning = true;
    }

    // MODIFIES: this
    // EFFECTS: returns a String with updated values of every circle
    //          in the sandbox currently
    public String getOutput() {
        StringBuilder sb = new StringBuilder();

        if (circles.size() > 0 && isRunning) {
            for (int i = 0; i < STEPS_PER_UPDATE; i++) {
                for (Circle c : circles) {
                    String name = c.getId() + " (diameter = " + c.getDiam()
                            + " color = R:" + c.getColor().getRed() + " G:" + c.getColor().getGreen() + " B:"
                            + c.getColor().getBlue() + ")";
                    String data = "xPos: " + c.getXpos() + "  yPos: " + c.getYpos() + "  xVel: "
                            + c.getXvel() + "  yVel: " + c.getYvel();

                    sb.append(name).append("\n").append(data).append("\n");

                    updateCircle(c);
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    // MODIFIES: this
    // EFFECTS: adds new circle to sandbox with all random values
    public void addCircle() {
        int diam = rand.nextInt(51) + 25;

        circles.add(new Circle(rand.nextInt(WIDTH - diam), rand.nextInt(HEIGHT - diam),
                rand.nextInt(21) - 10, rand.nextInt(21) - 10,
                diam, new Color((int)(Math.random() * 0x1000000)), id));

        id++;
    }

    // MODIFIES: this
    // EFFECTS: adds given circle to sandbox
    public void addCircle(Circle c) {
        circles.add(c);

        id++;
    }

    // MODIFIES: this
    // EFFECTS: relaunches all circles in sandbox with random velocities
    public void relaunchCircles() {
        Random r = new Random();

        for (Circle c : circles) {
            c.setVel(r.nextInt(31) - 15, r.nextInt(61) - 30);
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes all circles in sandbox
    public void deleteCircles() {
        circles = new ArrayList<>();
    }

    // MODIFIES: this, c
    // EFFECTS: updates positions and velocities of all circles in
    //          sandbox, also processes circles hitting the boundaries
    public void updateCircle(Circle c) {
        if (c.nextDownYCoord() > HEIGHT) {
            c.setPos(c.nextLeftXCoord(), HEIGHT - c.getDiam());
            c.updateXVel();
            c.bounceUp();
        } else if (c.nextUpYCoord() < 0) {
            c.setPos(c.nextLeftXCoord(), 0);
            c.updateXVel();
            c.bounceDown();
        } else if (c.nextRightXCoord() > WIDTH) {
            c.setPos(WIDTH - c.getDiam(), c.nextUpYCoord());
            c.updateXVel();
            c.bounceSide();
        } else if (c.nextLeftXCoord() < 0) {
            c.setPos(0, c.nextUpYCoord());
            c.updateXVel();
            c.bounceSide();
        } else {
            c.updatePos();
        }
        c.updateYVel();
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public ArrayList<Circle> getCircles() {
        return circles;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("sandbox", 0);
        json.put("circles", circlesToJson());

        return json;
    }

    private JSONArray circlesToJson() {
        JSONArray jsonArr = new JSONArray();

        for (Circle c : circles) {
            jsonArr.put(c.toJson());
        }

        return jsonArr;
    }
}
