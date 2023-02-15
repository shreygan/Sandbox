package model;

import java.awt.*;

// Represents a circle with positions, velocities, diameter, color, and id
public class Circle {

    public static final int XACC = 1;
    public static final int YACC = 1;
    public static final double X_COEFFICENT = .5;
    public static final double BOUNCE_COEFFICENT = -.8;
    public static final double BOUNCE_DOWN_COEFFICENT = -1;

    private int xpos;
    private int ypos;

    private int xvel;
    private int yvel;

    private int diam;

    private Color color;
    private int id;

    // EFFECTS: initalizes new circle with all given variables
    public Circle(int xpos, int ypos, int xvel, int yvel, int diam, Color color, int id) {
        this.xpos = xpos;
        this.ypos = ypos;

        this.xvel = xvel;
        this.yvel = yvel;

        this.diam = diam;

        this.color = color;
        this.id = id;
    }

    // EFFECTS: returns next leftmost x position
    public int nextLeftXCoord() {
        return xpos + xvel;
    }

    // EFFECTS: returns next rightmost x position
    public int nextRightXCoord() {
        return nextLeftXCoord() + diam;
    }

    // EFFECTS: returns next upmost y position
    public int nextUpYCoord() {
        return ypos + yvel;
    }

    // EFFECTS: returns next downmost y position
    public int nextDownYCoord() {
        return nextUpYCoord() + diam;
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
    // EFFECTS: updates x and y positions based on x and y velocities
    public void updatePos() {
        this.xpos += this.xvel;
        this.ypos += this.yvel;
    }

    // MODIFIES: this
    // EFFECTS: updates x velocity
    public void updateXVel() {
        if (this.xvel > 0) {
            this.xvel -= XACC * X_COEFFICENT;
        } else if (this.xvel < 0) {
            this.xvel += XACC * X_COEFFICENT;
        }
    }

    // MODIFIES: this
    // EFFECTS: updates y velocity
    public void updateYVel() {
        this.yvel += YACC;
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off top boundary
    public void bounceUp() {
        if (yvel > 5) {
            this.yvel *= BOUNCE_COEFFICENT;
        } else {
            this.yvel = 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off ground
    public void bounceDown() {
        this.yvel *= BOUNCE_DOWN_COEFFICENT;
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off side boundaries
    public void bounceSide() {
        this.xvel *= BOUNCE_COEFFICENT;
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
}
