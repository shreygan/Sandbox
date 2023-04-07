package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Represents the sandbox game with all it's circles
public class Game implements Writeable {

    public static final int THROW_SPEED = 200;

    private int width;
    private int height;

    private ArrayList<Circle> circles;
    private int id;

    private Circle circleDragged;

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
            tickCircleBorders(c);
            tickCircleCollisions(c);
        }
    }

    // MODIFIES: this
    // EFFECTS: "ticks" game in reverse
    public void untick() {
        for (Circle c : circles) {
            untickCircleBorders(c);
        }
    }

    // MODIFIES: this, c
    // EFFECTS: updates positions and velocities of all circles in sandbox,
    //          also processes circles hitting the boundaries
    private void tickCircleBorders(Circle c) {
        if (c.nextY() + c.getDiam() > height) {
            c.setPos(c.nextX(), height - c.getDiam());
            c.tickXVel();
            c.bounceY();
        } else if (c.nextY() < 0) {
            c.setPos(c.nextX(), 0);
            c.tickXVel();
            c.bounceY();
        } else if (c.nextX() + c.getDiam() > width) {
            c.setPos(width - c.getDiam(), c.nextY());
            c.tickXVel();
            c.bounceX();
        } else if (c.nextX() < 0) {
            c.setPos(0, c.nextY());
            c.tickXVel();
            c.bounceX();
        } else {
            c.tickPos();
            c.tickYVel();
        }
    }

    // MODIFIES: this, c
    // EFFECTS: reverses positions and velocities of all circles in sandbox,
    //          also processes circles hitting the boundaries
    private void untickCircleBorders(Circle c) {
        if (c.prevY() + c.getDiam() > height) {
            c.setPos(c.prevX(), height - c.getDiam());
            c.untickXVel();
            c.untickYVel();
            c.reverseBounceY();
        } else if (c.prevY() < 0) {
            c.setPos(c.prevX(), 0);
            c.untickXVel();
            c.untickYVel();
            c.reverseBounceY();
        } else if (c.prevX() + c.getDiam() > width) {
            c.setPos(width - c.getDiam(), c.prevY());
            c.untickXVel();
            c.bounceX();
        } else if (c.prevX() < 0) {
            c.setPos(0, c.prevY());
            c.untickXVel();
            c.bounceX();
        } else {
            c.untickPos();
            c.untickYVel();
        }
    }

    // MODIFIES: this, c0
    // EFFECTS: checks if c0 will collide with any other circles in jpanel,
    //          and if they will, makes both bounce off
    private void tickCircleCollisions(Circle c0) {
        if (c0 == circleDragged) {
            return;
        }

        for (Circle c : circles) {
            if (c != c0 && c.willOverlap(c0)) {
                c0.bounceOff(c);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new circle to sandbox with all random values
    public void addCircle() {
        int diam = rand.nextInt(201) + 50;
        int xpos = rand.nextInt(width - diam);
        int ypos = rand.nextInt(height - diam);

        while (overlaps(xpos, ypos, diam)) {
            diam = rand.nextInt(201) + 50;
            xpos = rand.nextInt(width - diam);
            ypos = rand.nextInt(height - diam);
        }

        circles.add(new Circle(xpos, ypos, rand.nextInt(21) - 10, rand.nextInt(21) - 10,
                diam, new Color((int) (Math.random() * 0x1000000)), id, true));

        EventLog.getInstance().logEvent(new Event("circle added v1 (id: " + id + ")"));
        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds given circle to sandbox
    public void addCircle(Circle c) {
        circles.add(c);

        EventLog.getInstance().logEvent(new Event("circle added v2 (id: " + id + ")"));
        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds circle on mouse position with given xvel, yvel, and rad
    public void addCircle(Point mouseCurr, int xvel, int yvel, int rad) {
        circles.add(new Circle(mouseCurr.x, mouseCurr.y, xvel, yvel, rad,
                new Color((int) (Math.random() * 0x1000000)), id, true));

        EventLog.getInstance().logEvent(new Event("circle added v3 (id: " + id + ")"));
        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds circle size of mouse drag with given color
    public void addCircle(Point mouseInit, Point mouseCurr, Color c) {
        int rad = (mouseCurr.y - mouseInit.y) * 2;

        circles.add(new Circle(mouseInit.x - rad / 2, mouseInit.y - rad / 2,
                rand.nextInt(21) - 10, rand.nextInt(21) - 10, rad, c, id, true));

        EventLog.getInstance().logEvent(new Event("circle added v4 (id: " + id + ")"));
        id++;
    }

    // MODIFIES: this
    // EFFECTS: relaunches all circles in sandbox with random velocities
    public void relaunchCircles() {
        for (Circle c : circles) {
            c.setVel(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
        }

        EventLog.getInstance().logEvent(new Event("relaunched circles"));
    }

    // MODIFIES: this
    // EFFECTS: deletes all circles from sandbox
    public void deleteCircles() {
        circles = new ArrayList<>();

        EventLog.getInstance().logEvent(new Event("deleted all circles"));
    }

    // MODIFIES: this
    // EFFECTS: if circle under given mouse position, deletes that
    //          circle, otherwise deletes all circles in game
    public void deleteCircles(Point mousePos) {
        for (int i = circles.size() - 1; i >= 0; i--) {
            if (circles.get(i).overlaps(mousePos)) {
                EventLog.getInstance().logEvent(new Event("deleted one circle (id: "
                        + circles.get(i).getId() + ")"));
                circles.remove(circles.get(i));
                return;
            }
        }

        circles = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("deleted all circles"));
    }

    // EFFECTS: returns true if given data overlaps with any circle
    public boolean overlaps(int xpos, int ypos, int diam) {
        for (Circle c : circles) {
            if (c.overlaps(xpos, ypos, diam)) {
                return true;
            }
        }

        return false;
    }

    // MODIFIES: this
    // EFFECTS: if there is a circle under given mouse position returns it,
    //          otherwise returns null
    public Circle overlaps(Point mouseCurr) {
        for (Circle c : circles) {
            if (c.overlaps(mouseCurr)) {
                return c;
            }
        }
        return null;
    }

    // REQUIRES: circleDragged != null
    // MODIFIES: this
    // EFFECTS: "drags" circle under moseCurr position
    public void moveCircle(Point mouseCurr) {
        circleDragged.setPos(mouseCurr.x - circleDragged.getDiam() / 2,
                    mouseCurr.y - circleDragged.getDiam() / 2);
    }

    // REQUIRES: circleDragged != null
    // MODIFIES: this
    // EFFECTS: "releases" circle being dragged w/ velocities based on time held and mouse positions
    public void releaseCircle(Point mouseInit, Point mouseCurr, double time) {
        circleDragged.setAccelerating(true);

        double angle = Math.atan2(mouseCurr.y - mouseInit.y, mouseCurr.x - mouseInit.x);

        circleDragged.setVel((int) ((Math.cos(angle) * THROW_SPEED / 10) / (time / THROW_SPEED)),
                (int) ((Math.sin(angle) * THROW_SPEED / 10) / (time / THROW_SPEED)));
    }

    // MODIFIES: this
    // EFFECTS: "releases" circle with same x and y velocities
    public void releaseCircle() {
        circleDragged.setAccelerating(true);
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

    // REQUIRES: circles contains circleDragged
    public void setCircleDragged(Circle circleDragged) {
        this.circleDragged = circleDragged;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
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
