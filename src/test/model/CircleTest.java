package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CircleTest {

    private Circle c;

    @BeforeEach
    void setup() {
        c = new Circle(500, 500, 1, 1, 10, Color.RED, 1, true);
    }

    @Test
    void testNextCoords() {
        assertEquals(500 + 1, c.nextX());
        assertEquals(500 + 1, c.nextY());
    }

    @Test
    void testSetPosVel() {
        c.setPos(0, 0);
        c.setVel(10, 10);

        assertEquals(0, c.getXpos());
        assertEquals(0, c.getYpos());

        assertEquals(10, c.getXvel());
        assertEquals(10, c.getYvel());
    }

    @Test
    void testUpdatePosAccelerating() {
        c.setAccelerating(true);

        c.updatePos();

        assertEquals(500 + 1, c.getXpos());
        assertEquals(500 + 1, c.getYpos());
    }

    @Test
    void testUpdatePosNotAccelerating() {
        c.setAccelerating(false);

        c.updatePos();

        assertEquals(500, c.getXpos());
        assertEquals(500, c.getYpos());
    }

    @Test
    void testUpdateXVelAcceleratingPosNeg20() {
        c.setAccelerating(true);

        c.setVel(20, 0);
        c.updateXVel();

        assertEquals(20 * Circle.BOUNCE_COEFFICENT, c.getXvel());

        c.setVel(-20, 0);
        c.updateXVel();

        assertEquals(-20 * Circle.BOUNCE_COEFFICENT, c.getXvel());
    }

    @Test
    void testUpdateXVelNotAcceleratingPosNeg20() {
        c.setAccelerating(false);

        c.setVel(20, 0);
        c.updateXVel();

        assertEquals(20, c.getXvel());

        c.setVel(-20, 0);
        c.updateXVel();

        assertEquals(-20, c.getXvel());
    }

    @Test
    void testUpdateXVelPosNeg5() {
        c.setVel(5, 0);
        c.updateXVel();

        assertEquals(5 - Circle.XACC, c.getXvel());

        c.setVel(-5, 0);
        c.updateXVel();

        assertEquals(-5 + Circle.XACC, c.getXvel());
    }

    @Test
    void testUpdateXVel0() {
        c.setVel(0, 0);
        c.updateXVel();

        assertEquals(0, c.getXvel());
    }

    @Test
    void testUpdateYVel() {
        c.setAccelerating(true);
        c.setVel(0, 20);
        c.updateYVel();

        assertEquals(20 + Circle.YACC, c.getYvel());

        c.setAccelerating(false);
        c.setVel(0, 20);
        c.updateYVel();

        assertEquals(20, c.getYvel());
    }

    @Test
    void testBounceYPosNeg10() {
        c.setVel(0, 10);
        c.bounceY();

        assertEquals(10 * Circle.Y_COEFFICENT, c.getYvel());

        c.setVel(0, -10);
        c.bounceY();

        assertEquals(-10 * Circle.Y_COEFFICENT, c.getYvel());
    }

    @Test
    void testBounceY3() {
        c.setVel(0, 3);
        c.bounceY();

        assertEquals(0, c.getYvel());
    }

    @Test
    void testBounceX() {
        c.setVel(-10, 0);
        c.bounceX();

        assertEquals(10, c.getXvel());
    }

    @Test
    void testGetters() {
        assertEquals(Color.RED, c.getColor());
        assertEquals(10 / 2f, c.getRad());
    }
}
