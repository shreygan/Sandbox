package model;

import org.json.JSONObject;
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
    void testPrevCoords() {
        assertEquals(500 - 1, c.prevX());
        assertEquals(500 - 1, c.prevY());
    }

    @Test
    void testSetPos() {
        c.setPos(0, 0);

        assertEquals(0, c.getXpos());
        assertEquals(0, c.getYpos());
    }

    @Test
    void testSetPosGivenCenter() {
        c.setPos(new Point(30, 30));

        assertEquals(25, c.getXpos());
        assertEquals(25, c.getYpos());
    }

    @Test
    void testSetVel() {
        c.setVel(10, 10);

        assertEquals(10, c.getXvel());
        assertEquals(10, c.getYvel());

        c.setVel(new Vector2D(15, 15));

        assertEquals(15, c.getXvel());
        assertEquals(15, c.getYvel());
    }

    @Test
    void testTickPosAccelerating() {
        c.tickPos();

        assertEquals(500 + 1, c.getXpos());
        assertEquals(500 + 1, c.getYpos());
    }

    @Test
    void testTickPosNotAccelerating() {
        c.setAccelerating(false);

        c.tickPos();

        assertEquals(500, c.getXpos());
        assertEquals(500, c.getYpos());
    }

    @Test
    void testUntickPosAccelerating() {
        c.untickPos();

        assertEquals(500 - 1, c.getXpos());
        assertEquals(500 - 1, c.getYpos());
    }

    @Test
    void testUntickPosNotAccelerating() {
        c.setAccelerating(false);

        c.untickPos();

        assertEquals(500, c.getXpos());
        assertEquals(500, c.getYpos());
    }

    @Test
    void testTickXVelAcceleratingPosNeg20() {
        c.setVel(20, 0);
        c.tickXVel();

        assertEquals(20 * Circle.BOUNCE_COEFFICENT, c.getXvel());

        c.setVel(-20, 0);
        c.tickXVel();

        assertEquals(-20 * Circle.BOUNCE_COEFFICENT, c.getXvel());
    }

    @Test
    void testTickXVelNotAcceleratingPosNeg20() {
        c.setAccelerating(false);

        c.setVel(20, 0);
        c.tickXVel();

        assertEquals(20, c.getXvel());

        c.setVel(-20, 0);
        c.tickXVel();

        assertEquals(-20, c.getXvel());
    }

    @Test
    void testTickXVelPosNeg5() {
        c.setVel(5, 0);
        c.tickXVel();

        assertEquals(5 - Circle.XACC, c.getXvel());

        c.setVel(-5, 0);
        c.tickXVel();

        assertEquals(-5 + Circle.XACC, c.getXvel());
    }

    @Test
    void testTickXVel0() {
        c.setVel(0, 0);
        c.tickXVel();

        assertEquals(0, c.getXvel());
    }

    @Test
    void testUntickXVelAcceleratingPosNeg20() {
        c.setVel(20, 0);
        c.untickXVel();

        assertEquals(20 * (1 / Circle.BOUNCE_COEFFICENT), c.getXvel());

        c.setVel(-20, 0);
        c.untickXVel();

        assertEquals(-20 * (1 / Circle.BOUNCE_COEFFICENT), c.getXvel());
    }

    @Test
    void testUntickXVelNotAcceleratingPosNeg20() {
        c.setAccelerating(false);

        c.setVel(20, 0);
        c.untickXVel();

        assertEquals(20, c.getXvel());

        c.setVel(-20, 0);
        c.untickXVel();

        assertEquals(-20, c.getXvel());
    }

    @Test
    void testUntickXVelPosNeg5() {
        c.setVel(5, 0);
        c.untickXVel();

        assertEquals(5 + Circle.XACC, c.getXvel());

        c.setVel(-5, 0);
        c.untickXVel();

        assertEquals(-5 - Circle.XACC, c.getXvel());
    }

    @Test
    void testUntickXVel0() {
        c.setVel(0, 0);
        c.untickXVel();

        assertEquals(0, c.getXvel());
    }

    @Test
    void testTickYVel() {
        c.setVel(0, 20);
        c.tickYVel();

        assertEquals(20 + Circle.YACC, c.getYvel());

        c.setAccelerating(false);
        c.setVel(0, 20);
        c.tickYVel();

        assertEquals(20, c.getYvel());
    }

    @Test
    void testUntickYVel() {
        c.setVel(0, 20);
        c.untickYVel();

        assertEquals(20 - Circle.YACC, c.getYvel());

        c.setAccelerating(false);
        c.setVel(0, 20);
        c.untickYVel();

        assertEquals(20, c.getYvel());
    }

    @Test
    void testBounceX() {
        c.setVel(-10, 0);
        c.bounceX();

        assertEquals(10, c.getXvel());
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
    void testReverseBounceY() {
        c.setVel(0, 25);
        c.reverseBounceY();

        assertEquals((int) (25 * (1 / Circle.Y_COEFFICENT)), c.getYvel());
    }

    @Test
    void testWillOverlapOverlapping() {
        Circle c2 = new Circle(505, 500, 1, 1, 10, Color.RED, 1, true);

        assertTrue(c.willOverlap(c2));
    }

    @Test
    void testWillOverlapNotOverlapping1() {
        Circle c2 = new Circle(525, 500, 1, 1, 10, Color.RED, 1, true);

        assertFalse(c.willOverlap(c2));
    }

    @Test
    void testWillOverlapNotOverlapping2() {
        Circle c1 = new Circle(500, 500, 0, 0, 10, Color.RED, 1, true);
        Circle c2 = new Circle(505, 500, 0, 0, -15, Color.RED, 1, true);

        assertFalse(c1.willOverlap(c2));
    }

    @Test
    void testWillOverlapNotOverlapping3() {
        Circle c1 = new Circle(505, 500, 0, 0, -15, Color.RED, 1, true);
        Circle c2 = new Circle(505, 500, 0, 0, -15, Color.RED, 1, true);

        assertFalse(c1.willOverlap(c2));
    }

    @Test
    void testOverlapsCircle() {
        Circle c2 = new Circle(505, 500, 1, 1, 10, Color.RED, 1, true);

        assertTrue(c.overlaps(c2));
    }

    @Test
    void testNotOverlapsCircle1() {
        Circle c2 = new Circle(525, 500, 1, 1, 10, Color.RED, 1, true);

        assertFalse(c.overlaps(c2));
    }

    @Test
    void testNotOverlapsCircle2() {
        Circle c1 = new Circle(500, 500, 0, 0, 10, Color.RED, 1, true);
        Circle c2 = new Circle(505, 500, 0, 0, -10, Color.RED, 1, true);

        assertFalse(c1.overlaps(c2));
    }

    @Test
    void testNotOverlapsCircle3() {
        Circle c1 = new Circle(505, 500, 0, 0, -15, Color.RED, 1, true);
        Circle c2 = new Circle(505, 500, 0, 0, -15, Color.RED, 1, true);

        assertFalse(c1.overlaps(c2));
    }

    @Test
    void testOverlapsGivenData() {
        assertTrue(c.overlaps(505, 500, 10));
        assertFalse(c.overlaps(525, 500, 10));
    }

    @Test
    void testOverlapsMouse() {
        assertTrue(c.overlaps(new Point(505, 505)));
    }

    @Test
    void testNotOverlapsMouse() {
        assertFalse(c.overlaps(new Point(510, 510)));
    }

    @Test
    void testBounceOffAboveLeft() {
        c.setVel(10, 0);

        Circle c1 = new Circle(512, 510,
                -10, 0, 10, Color.BLUE, 4, true);

        double angle = Math.atan2(c1.getCenter().y - c.getCenter().y, c1.getCenter().x - c.getCenter().x);

        Vector2D v1 = new Vector2D(10, 0);
        Vector2D v2 = new Vector2D(-10, 0);
        v1.rotateClockwise(angle);
        v2.rotateClockwise(angle);

        double nV1 = ((v1.getVx() * (c.getDiam() - c1.getDiam())) + (2 * c1.getDiam() * v2.getVx()))
                / (c.getDiam() + c1.getDiam());
        double nV2 = ((v2.getVx() * (c1.getDiam() - c.getDiam())) + (2 * c.getDiam() * v1.getVx()))
                / (c.getDiam() + c1.getDiam());

        v1.setVx(nV1);
        v2.setVx(nV2);

        v1.rotateCounterClockwise(angle);
        v2.rotateCounterClockwise(angle);

        c.bounceOff(c1);

        assertEquals(501, c.getXpos());
        assertEquals(500, c.getYpos());
        assertEquals((int) (v1.getVx() * Circle.BOUNCE_COEFFICENT), c.getXvel());
        assertEquals((int) (v1.getVy() * Circle.BOUNCE_COEFFICENT), c.getYvel());


        assertEquals(509, c1.getXpos());
        assertEquals(507, c1.getYpos());
        assertEquals((int) (v2.getVx() * Circle.BOUNCE_COEFFICENT), c1.getXvel());
        assertEquals((int) (v2.getVy() * Circle.BOUNCE_COEFFICENT), c1.getYvel());
    }

    @Test
    void testBounceOffAboveRight() {
        c.setVel(10, 0);

        Circle c1 = new Circle(488, 502,
                -10, 0, 10, Color.BLUE, 4, true);

        double angle = Math.atan2(c1.getCenter().y - c.getCenter().y, c1.getCenter().x - c.getCenter().x);

        Vector2D v1 = new Vector2D(10, 0);
        Vector2D v2 = new Vector2D(-10, 0);
        v1.rotateClockwise(angle);
        v2.rotateClockwise(angle);

        double nV1 = ((v1.getVx() * (c.getDiam() - c1.getDiam())) + (2 * c1.getDiam() * v2.getVx()))
                / (c.getDiam() + c1.getDiam());
        double nV2 = ((v2.getVx() * (c1.getDiam() - c.getDiam())) + (2 * c.getDiam() * v1.getVx()))
                / (c.getDiam() + c1.getDiam());

        v1.setVx(nV1);
        v2.setVx(nV2);

        v1.rotateCounterClockwise(angle);
        v2.rotateCounterClockwise(angle);

        c.bounceOff(c1);

        assertEquals(498, c.getXpos());
        assertEquals(502, c.getYpos());
        assertEquals((int) (v1.getVx() * Circle.BOUNCE_COEFFICENT), c.getXvel());
        assertEquals((int) (v1.getVy() * Circle.BOUNCE_COEFFICENT), c.getYvel());


        assertEquals(488, c1.getXpos());
        assertEquals(503, c1.getYpos());
        assertEquals((int) (v2.getVx() * Circle.BOUNCE_COEFFICENT), c1.getXvel());
        assertEquals((int) (v2.getVy() * Circle.BOUNCE_COEFFICENT), c1.getYvel());
    }

    @Test
    void testBounceOffBelowRight() {
        c.setVel(10, 0);

        Circle c1 = new Circle(488, 498,
                -10, 0, 10, Color.BLUE, 4, true);

        double angle = Math.atan2(c1.getCenter().y - c.getCenter().y, c1.getCenter().x - c.getCenter().x);

        Vector2D v1 = new Vector2D(10, 0);
        Vector2D v2 = new Vector2D(-10, 0);
        v1.rotateClockwise(angle);
        v2.rotateClockwise(angle);

        double nV1 = ((v1.getVx() * (c.getDiam() - c1.getDiam())) + (2 * c1.getDiam() * v2.getVx()))
                / (c.getDiam() + c1.getDiam());
        double nV2 = ((v2.getVx() * (c1.getDiam() - c.getDiam())) + (2 * c.getDiam() * v1.getVx()))
                / (c.getDiam() + c1.getDiam());

        v1.setVx(nV1);
        v2.setVx(nV2);

        v1.rotateCounterClockwise(angle);
        v2.rotateCounterClockwise(angle);

        c.bounceOff(c1);

        assertEquals(499, c.getXpos());
        assertEquals(501, c.getYpos());
        assertEquals((int) (v1.getVx() * Circle.BOUNCE_COEFFICENT), c.getXvel());
        assertEquals((int) (v1.getVy() * Circle.BOUNCE_COEFFICENT), c.getYvel());


        assertEquals(490, c1.getXpos());
        assertEquals(500, c1.getYpos());
        assertEquals((int) (v2.getVx() * Circle.BOUNCE_COEFFICENT), c1.getXvel());
        assertEquals((int) (v2.getVy() * Circle.BOUNCE_COEFFICENT), c1.getYvel());
    }

    @Test
    void testBounceOffBelowLeft() {
        c.setVel(10, 0);

        Circle c1 = new Circle(512, 488,
                -10, 0, 10, Color.BLUE, 4, true);

        double angle = Math.atan2(c1.getCenter().y - c.getCenter().y, c1.getCenter().x - c.getCenter().x);

        Vector2D v1 = new Vector2D(10, 0);
        Vector2D v2 = new Vector2D(-10, 0);
        v1.rotateClockwise(angle);
        v2.rotateClockwise(angle);

        double nV1 = ((v1.getVx() * (c.getDiam() - c1.getDiam())) + (2 * c1.getDiam() * v2.getVx()))
                / (c.getDiam() + c1.getDiam());
        double nV2 = ((v2.getVx() * (c1.getDiam() - c.getDiam())) + (2 * c.getDiam() * v1.getVx()))
                / (c.getDiam() + c1.getDiam());

        v1.setVx(nV1);
        v2.setVx(nV2);

        v1.rotateCounterClockwise(angle);
        v2.rotateCounterClockwise(angle);

        c.bounceOff(c1);

        assertEquals(503, c.getXpos());
        assertEquals(497, c.getYpos());
        assertEquals((int) (v1.getVx() * Circle.BOUNCE_COEFFICENT), c.getXvel());
        assertEquals((int) (v1.getVy() * Circle.BOUNCE_COEFFICENT), c.getYvel());


        assertEquals(510, c1.getXpos());
        assertEquals(490, c1.getYpos());
        assertEquals((int) (v2.getVx() * Circle.BOUNCE_COEFFICENT), c1.getXvel());
        assertEquals((int) (v2.getVy() * Circle.BOUNCE_COEFFICENT), c1.getYvel());
    }

    @Test
    void testIsAboveRight() {
        Circle c1 = new Circle(500, 500, 0, 0, 25, Color.RED, 2, true);
        Circle c2 = new Circle(600, 400, 0, 0, 25, Color.RED, 2, true);
        assertTrue(c2.isAboveRight(c1));

        c2 = new Circle(400, 400, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isAboveRight(c1));

        c2 = new Circle(600, 600, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isAboveRight(c1));

        c2 = new Circle(400, 600, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isAboveRight(c1));
    }

    @Test
    void testIsBelowRight() {
        Circle c1 = new Circle(500, 500, 0, 0, 25, Color.RED, 2, true);
        Circle c2 = new Circle(600, 600, 0, 0, 25, Color.RED, 2, true);
        assertTrue(c2.isBelowRight(c1));

        c2 = new Circle(400, 400, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isBelowRight(c1));

        c2 = new Circle(600, 400, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isBelowRight(c1));

        c2 = new Circle(400, 600, 0, 0, 25, Color.RED, 2, true);
        assertFalse(c2.isBelowRight(c1));
    }

    @Test
    void testGetters() {
        assertEquals(Color.RED, c.getColor());
        assertEquals(10 / 2f, c.getRad());
        assertEquals(1, c.getId());
    }

    @Test
    void testToJson() {
        JSONObject json = c.toJson();

        assertEquals(500, json.getInt("xpos"));
        assertEquals(500, json.getInt("ypos"));
        assertEquals(1, json.getInt("xvel"));
        assertEquals(1, json.getInt("yvel"));
        assertEquals(10, json.getInt("diam"));
        assertEquals(Color.RED.getRGB(), json.getInt("color"));
        assertEquals(1, json.getInt("id"));
        assertTrue(json.getBoolean("accelerating"));
    }
}
