package model;

import org.json.JSONObject;
import persistence.Writeable;

import java.awt.*;
import java.util.ArrayList;

// Represents a circle with positions, velocities, diameter, color, and id
public class Circle implements Writeable {

    public static final int XACC = 1;
    public static final int YACC = 1;
    public static final double BOUNCE_COEFFICENT = .8;
    public static final double Y_COEFFICENT = -.8;

    private int xpos;
    private int ypos;

    private int xvel;
    private int yvel;

    private int diam;

    private Color color;
    private int id;
    private boolean accelerating;

    // EFFECTS: initalizes new circle with all given variables
    public Circle(int xpos, int ypos, int xvel, int yvel, int diam, Color color, int id, boolean accelerating) {
        this.xpos = xpos;
        this.ypos = ypos;

        this.xvel = xvel;
        this.yvel = yvel;

        this.diam = diam;

        this.color = color;
        this.id = id;
        this.accelerating = accelerating;
    }

    // EFFECTS: returns next x position
    public int nextX() {
        return xpos + xvel;
    }

    // EFFECTS: returns next y position
    public int nextY() {
        return ypos + yvel;
    }

    // MODIFIES: this
    // EFFECTS: sets x and y positions to given variables
    public void setPos(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    // MODIFIES: this
    // EFFECTS: sets x and y velocities to given variables
    public void setVel(int xvel, int yvel) {
        this.xvel = xvel;
        this.yvel = yvel;
    }

    // MODIFIES: this
    // EFFECTS: sets x and y velocities on given Vector2D
    public void setVel(Vector2D vel) {
        this.xvel = (int) vel.getVx();
        this.yvel = (int) vel.getVy();
    }

    // MODIFIES: this
    // EFFECTS: updates x and y positions based on x and y velocities
    public void updatePos() {
        if (accelerating) {
            this.xpos += this.xvel;
            this.ypos += this.yvel;
        }
    }

    // MODIFIES: this
    // EFFECTS: updates x velocity
    public void updateXVel() {
        if (accelerating) {
            if (xvel > 10 || xvel < -10) {
                xvel *= BOUNCE_COEFFICENT;
            } else {
                if (xvel > 0) {
                    xvel -= XACC;
                } else if (xvel < 0) {
                    xvel += XACC;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates y velocity
    public void updateYVel() {
        if (accelerating) {
            yvel += YACC;
        }
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off ground or ceiling
    public void bounceY() {
        if (yvel > 5 || yvel < -5) {
            this.yvel *= Y_COEFFICENT;
        } else {
            this.yvel = 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off side boundaries
    public void bounceX() {
        this.xvel *= -1;
    }

    // EFFECTS: returns true if given circles xpos and ypos on next tick will
    //          overlap with this circle's next xpos and ypos
    public boolean willOverlap(Circle c) {
        double dist = this.getNextCenter().distance(c.getNextCenter());

        return this.getRad() - c.getRad() < dist && dist < this.getRad() + c.getRad();
    }

    // EFFECTS: returns center of this on next tick
    private Point getNextCenter() {
        int x = nextX() + (diam / 2);
        int y = nextY() + (diam / 2);

        return new Point(x, y);
    }

    // REQUIRES: c0 != null
    // MODIFIES: this, c0
    // EFFECTS: simulates c0 and this circle bouncing off each other
    public void bounceOff(Circle c0, ArrayList<Circle> circles) {
        Point thisCenter = this.getCenter();
        Point c0Center = c0.getCenter();

        double angle = Math.atan2(c0Center.y - thisCenter.y, c0Center.x - thisCenter.x);

        Vector2D thisVel = new Vector2D(this.xvel, this.yvel);
        Vector2D c0Vel = new Vector2D(c0.xvel, c0.yvel);

        Vector2D thisVelCopy = new Vector2D(thisVel);
        Vector2D c0VelCopy = new Vector2D(c0Vel);

//        int count = 0;
//        for (Circle c : circles) {
//            if (this.willOverlap(c)) {
//                count++;
//            }
//        }

//        if (count >= 2) {
//            System.out.println("count = " + count);
//            return;
//        }

//        if (thisVel.getSum() <= 1 && c0Vel.getSum() <= 1) {
//            if (this.isAbove(c0)) {
//                if (this.isLeftOf(c0)) {
//                    this.xvel = -1;
//                    this.yvel = 0;
//                } else if (this.isRightOf(c0)) {
//                    this.xvel = 1;
//                    this.yvel = 0;
//                }
//            } else if (this.isBelow(c0)) {
//                if (c0.isLeftOf(this)) {
//                    c0.xvel = -1;
//                    c0.yvel = 0;
//                } else if (c0.isRightOf(this)) {
//                    c0.xvel = 1;
//                    c0.yvel = 0;
//                }
//            }
//        }
        thisVel.rotateClockwise(angle);
        c0Vel.rotateClockwise(angle);

        // from 1D elastic collision equation
        double thisNewV1 = ((thisVel.getVx() * (this.diam - c0.diam)) + (2 * c0.diam * c0Vel.getVx()))
                / (this.diam + c0.diam);
        double c0NewV1 = ((c0Vel.getVx() * (c0.diam - this.diam)) + (2 * this.diam * thisVel.getVx()))
                / (this.diam + c0.diam);

        thisVel.setVx(thisNewV1);
        c0Vel.setVx(c0NewV1);

        thisVel.rotateCounterClockwise(angle);
        c0Vel.rotateCounterClockwise(angle);

        thisVel.multiplyBy(BOUNCE_COEFFICENT);
        c0Vel.multiplyBy(BOUNCE_COEFFICENT);

        this.setVel(thisVel);
        c0.setVel(c0Vel);

//        for (Circle c : circles) {
//            if (c != c0 && this.willOverlap(c)) {
//                this.setVel(thisVelCopy);
//                c0.setVel(c0VelCopy);
//                System.out.println("TRUE");
//                return;
//            }
//        }
    }

    private boolean isAbove(Circle c0) {
        return this.ypos <= c0.ypos;
    }

    private boolean isBelow(Circle c0) {
        return this.ypos >= c0.ypos;
    }

    private boolean isLeftOf(Circle c0) {
        return this.xpos <= c0.xpos;
    }

    private boolean isRightOf(Circle c0) {
        return this.xpos >= c0.xpos;
    }

    // EFFECTS: returns true if given mouse position is inside this circle
    public boolean overlaps(Point mouseInit) {
        return Math.pow(mouseInit.x - getCenter().x, 2) + Math.pow(mouseInit.y - getCenter().y, 2)
                <= Math.pow(getRad(), 2);
    }

    // EFFECTS: returns JSONObject representing all data of this
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("xpos", xpos);
        json.put("ypos", ypos);
        json.put("xvel", xvel);
        json.put("yvel", yvel);
        json.put("diam", diam);
        json.put("color", color.getRGB());
        json.put("id", id);
        json.put("accelerating", accelerating);

        return json;
    }

    // EFFECTS: returns center of this circle
    private Point getCenter() {
        int x = xpos + (diam / 2);
        int y = ypos + (diam / 2);

        return new Point(x, y);
    }

    // EFFECTS: returns radius of this circle
    public double getRad() {
        return diam / 2f;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public int getXvel() {
        return xvel;
    }

    public int getYvel() {
        return yvel;
    }

    public int getDiam() {
        return diam;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }
}
