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

    private double d;

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
            updateCircleCollisions(c);
        }
    }

    // MODIFIES: this
    // EFFECTS: "ticks" game in reverse
    public void untick() {
        // TODO IMPLEMENT METHOD
    }

    // MODIFIES: this, c
    // EFFECTS: updates positions and velocities of all circles in sandbox,
    //          also processes circles hitting the boundaries
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

    // MODIFIES: this, c0
    // EFFECTS: checks if c0 will collide with any other circles in jpanel,
    //          and if they will, makes both bounce off
    private void updateCircleCollisions(Circle c0) {
        if (c0 == circleDragged) {
            return;
        }

        for (Circle c : circles) {
            if (c != c0) {
                if (c.willOverlap(c0)) {
                    c0.bounceOff(c, circles);
                }

//                if (c0.isStopped()) {
//                    if (c0.isAboveLeft(c)) {
//                        c.setVel(-1, c.getYvel());
//                    } else if (c0.isAboveRight(c)) {
//                        c.setVel(1, c.getYvel());
//                    }
//                }

            }
        }
    }

    public void rollOff() {
        Circle c = circles.get(0);
        Circle c0 = circles.get(1);

//        d += .02;

        c0.roll(c);


//        if (c.isAboveLeft(c0)) {
//            c.rollLeft(c0);
//        }
//        else if (this.isAboveRight(c0)) {
//            System.out.println(id);
//            this.rollRight(c0);
//        } else if (this.isBelowLeft(c0)) {
//            System.out.println(c0.id);
//            c0.rollRight(this);
//        } else if (this.isBelowRight(c0)) {
//            System.out.println(c0.id);
//            c0.rollLeft(this);
//        }
    }

    // EFFECTS: returns true if given data overlaps with any circle
    private boolean checkOverlap(int xpos, int ypos, int diam) {
        for (Circle c : circles) {
            if (c.overlaps(xpos, ypos, diam)) {
                return true;
            }
        }

        return false;
    }

    // MODIFIES: this
    // EFFECTS: adds new circle to sandbox with all random values
    public void addCircle() {
        int diam = rand.nextInt(201) + 50;
        int xpos = rand.nextInt(width - diam);
        int ypos = rand.nextInt(height - diam);

        while (checkOverlap(xpos, ypos, diam)) {
            diam = rand.nextInt(201) + 50;
            xpos = rand.nextInt(width - diam);
            ypos = rand.nextInt(height - diam);
        }

        circles.add(new Circle(xpos, ypos, rand.nextInt(21) - 10, rand.nextInt(21) - 10,
                diam, new Color((int) (Math.random() * 0x1000000)), id, true));

        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds given circle to sandbox
    public void addCircle(Circle c) {
        circles.add(c);

        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds circle on mouse position with given xvel, yvel, and rad
    public void addCircle(Point mouseCurr, int xvel, int yvel, int rad) {
        circles.add(new Circle(mouseCurr.x, mouseCurr.y, xvel, yvel, rad,
                new Color((int) (Math.random() * 0x1000000)), id, true));

        id++;
    }

    // REQUIRES: circle doesn't overlap with existing circles
    // MODIFIES: this
    // EFFECTS: adds circle size of mouse drag with given color
    public void addCircle(Point mouseInit, Point mouseCurr, Color c) {
        int rad = (mouseCurr.y - mouseInit.y) * 2;

        circles.add(new Circle(mouseInit.x - rad / 2, mouseInit.y - rad / 2,
                rand.nextInt(21) - 10, rand.nextInt(21) - 10, rad, c, id, true));

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

    // MODIFIES: this
    // EFFECTS: if circle under given mouse position, deletes that
    //          circle, otherwise deletes all circles in game
    public void deleteCircles(Point mousePos) {
        for (int i = circles.size() - 1; i >= 0; i--) {
            if (circles.get(i).overlaps(mousePos)) {
                circles.remove(circles.get(i));
                return;
            }
        }

        circles = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if circle under given mouse position, returns it,
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

    public ArrayList<Circle> getCircles() {
        return circles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // REQUIRES: circles contains circleDragged
    public void setCircleDragged(Circle circleDragged) {
        this.circleDragged = circleDragged;
    }
}
