package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CircleTest {

    private Circle c;

    @BeforeEach
    public void setup() {
        c = new Circle(Game.WIDTH / 2, Game.HEIGHT / 2, 1, 1, 10, Color.RED, 1);
    }

    @Test
    public void testConstructor() {
        assertEquals(Game.WIDTH / 2, c.getXpos());
        assertEquals(Game.HEIGHT / 2, c.getYpos());
        assertEquals(1, c.getXvel());
        assertEquals(1, c.getYvel());
        assertEquals(10, c.getDiam());
        assertEquals(Color.RED, c.getColor());
        assertEquals(1, c.getId());
    }

    @Test
    public void testNextLeftXCoord() {
        assertEquals(c.getXpos() + c.getXvel(), c.nextLeftXCoord());
    }

    @Test
    public void testNextRightXCoord() {
        assertEquals(c.getXpos() + c.getXvel() + c.getDiam(), c.nextRightXCoord());
    }

    @Test
    public void testNextDownYCoord() {
        assertEquals(c.getYpos() + c.getYvel() + c.getDiam(), c.nextDownYCoord());
    }

    @Test
    public void testNextUpYCoord() {
        assertEquals(c.getYpos() + c.getYvel(), c.nextUpYCoord());
    }

    @Test
    public void testSetPos() {
        c.setPos(100, 100);
        assertEquals(100, c.getXpos());
        assertEquals(100, c.getYpos());
    }

    @Test
    public void testSetVel() {
        c.setVel(100, 100);
        assertEquals(100, c.getXvel());
        assertEquals(100, c.getYvel());
    }

    @Test
    public void testUpdatePos() {
        int xpos = c.getXpos();
        int ypos = c.getYpos();
        c.updatePos();
        assertEquals(xpos + c.getXvel(), c.getXpos());
        assertEquals(ypos + c.getYvel(), c.getYpos());
    }

    @Test
    public void testUpdateXVelPos() {
        c.setVel(100, 0);
        c.updateXVel();
        assertEquals((int) (100 - (Circle.XACC * Circle.X_COEFFICENT)), c.getXvel());
    }

    @Test
    public void testUpdateXVelNeg() {
        c.setVel(-100, 0);
        c.updateXVel();
        assertEquals((int) (-100 + (Circle.XACC * Circle.X_COEFFICENT)), c.getXvel());
    }

    @Test
    public void testUpdateYVel() {
        c.setVel(0, 100);
        c.updateYVel();
        assertEquals(100 + Circle.YACC, c.getYvel());
    }

    @Test
    public void testBounceUp100() {
        c.setVel(0, 100);
        c.bounceUp();
        assertEquals(100 * Circle.BOUNCE_COEFFICENT, c.getYvel());
    }

    @Test
    public void testBounceUp1() {
        c.bounceUp();
        assertEquals(0, c.getYvel());
    }

    @Test
    public void testBounceDown() {
        c.setVel(0, 100);
        c.bounceDown();
        assertEquals(100 * Circle.BOUNCE_DOWN_COEFFICENT, c.getYvel());
    }

    @Test
    public void testBounceSide() {
        c.setVel(100, 0);
        c.bounceSide();
        assertEquals(100 * Circle.BOUNCE_COEFFICENT, c.getXvel());
    }
}
