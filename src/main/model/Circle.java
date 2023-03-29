package model;

import org.json.JSONObject;
import ui.Writeable;

import java.awt.*;

// Represents a circle with positions, velocities, diameter, color, and id
public class Circle implements Writeable {

    public static final int XACC = 1;
    public static final int YACC = 1;
    public static final double BOUNCE_COEFFICENT = .8;
    public static final double Y_COEFFICENT = -.8;
    public static final double ROLL_ACC = .02;

    private int xpos;
    private int ypos;

    private int xvel;
    private int yvel;

    private int diam;
    private int id;

    private Color color;
    private boolean accelerating;

    private double rollAngle;

    // EFFECTS: initalizes new circle with all given variables
    public Circle(int xpos, int ypos, int xvel, int yvel, int diam, Color color, int id, boolean accelerating) {
        this.xpos = xpos;
        this.ypos = ypos;

        this.xvel = xvel;
        this.yvel = yvel;

        this.diam = diam;
        this.id = id;

        this.color = color;
        this.accelerating = accelerating;

        this.rollAngle = 0;
    }

    // EFFECTS: returns next x position
    public int nextX() {
        return xpos + xvel;
    }

    // EFFECTS: returns previous x position
    public int prevX() {
        return xpos - xvel;
    }

    // EFFECTS: returns next y position
    public int nextY() {
        return ypos + yvel;
    }

    // EFFECTS: returns previous y position
    public int prevY() {
        return ypos - yvel;
    }

    // MODIFIES: this
    // EFFECTS: sets x and y positions to given variables
    public void setPos(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    // MODIFIES: this
    // EFFECTS: sets x and y positions based on where center should be
    public void setPos(Point center) {
        xpos = (int) (center.x - getRad());
        ypos = (int) (center.y - getRad());
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
    public void tickPos() {
        if (accelerating) {
            this.xpos += this.xvel;
            this.ypos += this.yvel;
        }
    }

    // MODIFIES: this
    // EFFECTS: reverses x and y positions to positions on previous tick
    public void untickPos() {
        if (accelerating) {
            this.xpos -= this.xvel;
            this.ypos -= this.yvel;
        }
    }

    // MODIFIES: this
    // EFFECTS: updates x velocity
    public void tickXVel() {
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
    // EFFECTS: reverses x velocity to vel on previous tick
    public void untickXVel() {
        if (accelerating) {
            if (xvel > 10 || xvel < -10) {
                xvel *= 1 / BOUNCE_COEFFICENT;
            } else {
                if (xvel > 0) {
                    xvel += XACC;
                } else if (xvel < 0) {
                    xvel -= XACC;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates y velocity
    public void tickYVel() {
        if (accelerating) {
            yvel += YACC;
        }
    }

    // MODIFIES: this
    // EFFECTS: revers y velocity to vel on previous tick
    public void untickYVel() {
        if (accelerating) {
            yvel -= YACC;
        }
    }

    // MODIFIES: this
    // EFFECTS: simulates bouncing off side boundaries
    public void bounceX() {
        this.xvel *= -1;
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
    // EFFECTS: simulates bouncing off ground or ceiling in reverse
    public void reverseBounceY() {
        this.yvel *= 1 / Y_COEFFICENT;
    }

    // EFFECTS: returns true if given circle will overlap this circle on next tick
    public boolean willOverlap(Circle c) {
        double dist = this.getNextCenter().distance(c.getNextCenter());

        return this.getRad() - c.getRad() < dist && dist < this.getRad() + c.getRad();
    }

    // EFFECTS: returns true if given circle overlaps this circle
    public boolean overlaps(Circle c) {
        double dist = this.getCenter().distance(c.getCenter());

        return this.getRad() - c.getRad() < dist && dist < this.getRad() + c.getRad();
    }

    // EFFECTS: returns true if given data overlaps with this circle
    public boolean overlaps(int xpos, int ypos, int diam) {
        double dist = this.getCenter().distance(new Point(xpos + (diam / 2), ypos + (diam / 2)));

        return (this.getRad() - diam / 2f) < dist && dist < (this.getRad() + diam / 2f);
    }

    // EFFECTS: returns true if given mouse position is inside this circle
    public boolean overlaps(Point mouseInit) {
        return Math.pow(mouseInit.x - getCenter().x, 2) + Math.pow(mouseInit.y - getCenter().y, 2)
                <= Math.pow(getRad(), 2);
    }

    // EFFECTS: returns center of this on prev next tick
    private Point getNextCenter() {
        int x = nextX() + (diam / 2);
        int y = nextY() + (diam / 2);

        return new Point(x, y);
    }

    // REQUIRES: c0 != null
    // MODIFIES: this, c0
    // EFFECTS: simulates c0 and this circle bouncing off each other
    public void bounceOff(Circle c0) {
        Point thisCenter = this.getCenter();
        Point c0Center = c0.getCenter();

        double angle1 = Math.atan2(c0Center.y - thisCenter.y, c0Center.x - thisCenter.x);
        double angle2 = Math.atan2(thisCenter.y - c0Center.y, thisCenter.x - c0Center.x);

        Vector2D thisVel = new Vector2D(this.xvel, this.yvel);
        Vector2D c0Vel = new Vector2D(c0.xvel, c0.yvel);

        thisVel.rotateClockwise(angle1);
        c0Vel.rotateClockwise(angle1);

        // from 1D elastic collision equation
        double thisNewV1 = ((thisVel.getVx() * (this.diam - c0.diam)) + (2 * c0.diam * c0Vel.getVx()))
                / (this.diam + c0.diam);
        double c0NewV1 = ((c0Vel.getVx() * (c0.diam - this.diam)) + (2 * this.diam * thisVel.getVx()))
                / (this.diam + c0.diam);

        thisVel.setVx(thisNewV1);
        c0Vel.setVx(c0NewV1);

        thisVel.rotateCounterClockwise(angle1);
        c0Vel.rotateCounterClockwise(angle1);

        thisVel.multiplyBy(BOUNCE_COEFFICENT);
        c0Vel.multiplyBy(BOUNCE_COEFFICENT);

        this.setVel(thisVel);
        c0.setVel(c0Vel);

        this.distanceFrom(c0);
        handleRoll(c0, angle1, angle2);
    }

    // REQUIRES: c0 != null
    // MODIFIES: this
    // EFFECTS: handles moving this circle and c0 closer together until overlapping
    public void distanceFrom(Circle c0) {
        if (this.isAboveLeft(c0)) {
            pushTowards(c0, true, true);
        } else if (this.isAboveRight(c0)) {
            pushTowards(c0, false, true);
        } else if (this.isBelowLeft(c0)) {
            pushTowards(c0, true, false);
        } else if (this.isBelowRight(c0)) {
            pushTowards(c0, false, false);
        }
    }

    // REQUIRES: c0 != null
    // MODIFIES: this, c0
    // EFFECTS: moves this circle and c0 closer together until overlapping
    private void pushTowards(Circle c0, boolean sign1, boolean sign2) {
        while (!this.overlaps(c0)) {
            if (sign1) {
                this.xpos += 1;
                c0.xpos -= 1;
            } else {
                this.xpos -= 1;
                c0.xpos += 1;
            }

            if (sign2) {
                this.ypos += 1;
                c0.ypos -= 1;
            } else {
                this.ypos -= 1;
                c0.ypos += 1;
            }
        }
    }

    // REQUIRES: c0 != null
    // MODIFIES: this, c0
    // EFFECTS: simulates c0 and this circle rolling off each other
    private void handleRoll(Circle c0, double angle1, double angle2) {
        if (this.isAboveLeft(c0)) {
            rollAngle = angle2;
            rollAngle -= ROLL_ACC;
            this.roll(c0);
        } else if (this.isAboveRight(c0)) {
            rollAngle = angle2;
            rollAngle += ROLL_ACC;
            roll(c0);
        } else if (c0.isAboveLeft(this)) {
            c0.rollAngle = angle1;
            c0.rollAngle -= ROLL_ACC;
            c0.roll(this);
        } else if (c0.isAboveRight(this)) {
            c0.rollAngle = angle1;
            c0.rollAngle += ROLL_ACC;
            c0.roll(this);
        }
    }

    // REQUIRES: c0 != null
    // MODIFIES: this
    // EFFECTS: sets new position after this circle 'rolls' off c0
    private void roll(Circle c0) {
        Point center = c0.getCenter();

        int tempX = (int) (center.x + (Math.cos(rollAngle) * (c0.getRad() + this.getRad())));
        int tempY = (int) (center.y + (Math.sin(rollAngle) * (c0.getRad() + this.getRad())));

        this.setPos(new Point(tempX, tempY));
    }

    // EFFECTS: returns true if this circle is above and left of c0
    public boolean isAboveLeft(Circle c0) {
        return this.getCenter().y < c0.getCenter().y && this.getCenter().x < c0.getCenter().x;
    }

    // EFFECTS: returns true if this circle is above and right of c0
    public boolean isAboveRight(Circle c0) {
        return this.getCenter().y < c0.getCenter().y && this.getCenter().x > c0.getCenter().x;
    }

    // EFFECTS: returns true if this circle is below and left of c0
    public boolean isBelowLeft(Circle c0) {
        return this.getCenter().y > c0.getCenter().y && this.getCenter().x < c0.getCenter().x;
    }

    // EFFECTS: returns true if this circle is below and right of c0
    public boolean isBelowRight(Circle c0) {
        return this.getCenter().y > c0.getCenter().y && this.getCenter().x > c0.getCenter().x;
    }

    // EFFECTS: returns center of this circle
    public Point getCenter() {
        int x = xpos + (diam / 2);
        int y = ypos + (diam / 2);

        return new Point(x, y);
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
