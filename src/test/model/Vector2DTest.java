package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Vector2DTest {

    private Vector2D v1;

    @BeforeEach
    void setup() {
        v1 = new Vector2D(12, 24);
    }

    @Test
    void testConstructor() {
        Vector2D v2 = new Vector2D(v1);

        assertEquals(v1.getSum(), v2.getSum());

        v2.setVx(1);
        v2.setVy(1);

        assertNotEquals(v1.getVx(), v2.getVx());
        assertNotEquals(v1.getVy(), v2.getVy());
    }

    @Test
    void testRotateClockwise() {
        v1.rotateClockwise(23);

        assertEquals(v1.getVx(), 12 * Math.cos(23) + 24 * Math.sin(23));
        assertEquals(v1.getVy(), 12 * - Math.sin(23) + 24 * Math.cos(23));
    }

    @Test
    void testRotateCouterClockwise() {
        v1.rotateCounterClockwise(23);

        assertEquals(v1.getVx(), 12 * Math.cos(23) - 24 * Math.sin(23));
        assertEquals(v1.getVy(), 12 * Math.sin(23) + 24 * Math.cos(23));
    }

    @Test
    void testMultiplyBy() {
        v1.multiplyBy(23);

        assertEquals(v1.getVx(), 12 * 23);
        assertEquals(v1.getVy(), 24 * 23);
    }

    @Test
    void testToString() {
        assertEquals(" vx: " + v1.getVx() + ", vy: " + v1.getVy(), v1.toString());
    }

    @Test
    void testEquals() {
        Vector2D v2 = new Vector2D(12, 24);

        assertEquals(v1, v1);
        assertEquals(v1.hashCode(), v1.hashCode());
        assertNotEquals(v1, "v1");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());

        v2.setVx(1);
        assertNotEquals(v1, v2);
        assertNotEquals(v1.hashCode(), v2.hashCode());

        v2.setVx(12);
        v2.setVy(2345);
        assertNotEquals(v1, v2);
        assertNotEquals(v1.hashCode(), v2.hashCode());
    }
}
