package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Represents the sandbox game with all it's circles
public class Game implements Writeable {

    private int width;
    private int height;

    private ArrayList<Circle> circles;
    private int id;

    private Random rand;

    // EFFECTS: initalizes new sandbox game
    public Game(Dimension dim) {
        width = dim.width;
        height = dim.height;

        circles = new ArrayList<>();
        rand = new Random();

        id = 1;
    }

    // MODIFIES: this
    // EFFECTS: updates positions of all circles in sandbox
    public void tick() {
        for (Circle c : circles) {
            updateCircleBorders(c);
        }
    }

    // MODIFIES: this, c
    // EFFECTS: updates positions and velocities of all circles in
    //          sandbox, also processes circles hitting the boundaries
    private void updateCircleBorders(Circle c) {
        if (c.nextY() + c.getDiam() > height) {
            c.setPos(c.nextX(), height - c.getDiam());
            c.updateXVel();
            c.bounceY();
        } else if (c.nextY() < 0) {
            c.setPos(c.nextX(), 0);
            c.updateXVel();
            c.bounceY();
        } else if (c.nextX() + c.getDiam() > width) {
            c.setPos(width - c.getDiam(), c.nextY());
            c.updateXVel();
            c.bounceX();
        } else if (c.nextX() < 0) {
            c.setPos(0, c.nextY());
            c.updateXVel();
            c.bounceX();
        } else {
            c.updatePos();
        }
        c.updateYVel();
    }

    // MODIFIES: this
    // EFFECTS: adds new circle to sandbox with all random values
    public void addCircle() {
        int diam = rand.nextInt(201) + 50;

        circles.add(new Circle(rand.nextInt(width - diam), rand.nextInt(height - diam),
                rand.nextInt(21) - 10, rand.nextInt(21) - 10,
                diam, new Color((int) (Math.random() * 0x1000000)), id, true));

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
        for (Circle c : circles) {
            c.setVel(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes all circles from sandbox
    public void deleteCircles() {
        circles = new ArrayList<>();
    }

    // EFFECTS: returns JSONObject representing all data of this
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("width", width);
        json.put("height", height);
        json.put("id", id);
        json.put("circles", circlesToJson());

        return json;
    }

    // EFFECTS: returns JSONArray of all circles in this
    private JSONArray circlesToJson() {
        JSONArray jsonArr = new JSONArray();

        for (Circle c : circles) {
            jsonArr.put(c.toJson());
        }

        return jsonArr;
    }

    public ArrayList<Circle> getCircles() {
        return circles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
