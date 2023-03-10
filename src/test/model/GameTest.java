package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    private Circle c1;
    private Circle c2;

    @BeforeEach
    void setup() {
        game = new Game(new Dimension(1000, 1000));

        c1 = new Circle(game.getWidth() / 2, game.getHeight() / 2,
                0, 2, 10, Color.RED, 1, true);
        c2 = new Circle(game.getWidth() / 3, game.getHeight() / 3,
                10, -10, 15, Color.BLUE, 2, true);

        game.addCircle(c1);
        game.addCircle(c2);
    }

    @Test
    void testConstructor() {
        assertEquals(1000, game.getWidth());
        assertEquals(1000, game.getHeight());
    }

    @Test
    void testId() {
        assertEquals(3, game.getId());
        game.setId(100);
        assertEquals(100, game.getId());
    }

    @Test
    void testAddCircle() {
        game.addCircle();
        game.addCircle();

        assertEquals(4, game.getCircles().size());
    }

    @Test
    void testDeleteCircles() {
        assertEquals(2, game.getCircles().size());

        game.deleteCircles();

        assertEquals(0, game.getCircles().size());
    }

    @Test
    void testRelaunchCircles() {
        Circle c1Copy = new Circle(game.getWidth() / 2, game.getHeight() / 2,
                0, 2, 10, Color.RED, 1, true);
        Circle c2Copy = new Circle(game.getWidth() / 3, game.getHeight() / 3,
                10, -10, 15, Color.BLUE, 2, true);

        game.relaunchCircles();

        assertNotEquals(c1Copy.getXvel(), c1.getXvel());
        assertNotEquals(c1Copy.getYvel(), c1.getYvel());

        assertNotEquals(c2Copy.getXvel(), c2.getXvel());
        assertNotEquals(c2Copy.getYvel(), c2.getYvel());
    }

    @Test
    void test1TickNoCollisions() {
        int c1X = c1.nextX();
        int c1Y = c1.nextY();

        int c2X = c2.nextX();
        int c2Y = c2.nextY();

        game.tick();

        assertEquals(c1X, c1.getXpos());
        assertEquals(c1Y, c1.getYpos());

        assertEquals(c2X, c2.getXpos());
        assertEquals(c2Y, c2.getYpos());
    }

    @Test
    void test2TickNoCollisions() {
        int c1X = c1.nextX();
        int c1Y = c1.nextY();

        int c2X = c2.nextX();
        int c2Y = c2.nextY();

        game.tick();

        assertEquals(c1X, game.getCircles().get(0).getXpos());
        assertEquals(c1Y, game.getCircles().get(0).getYpos());

        assertEquals(c2X, game.getCircles().get(1).getXpos());
        assertEquals(c2Y, game.getCircles().get(1).getYpos());

        c1X = c1.nextX();
        c1Y = c1.nextY();

        c2X = c2.nextX();
        c2Y = c2.nextY();

        game.tick();

        assertEquals(c1X, game.getCircles().get(0).getXpos());
        assertEquals(c1Y, game.getCircles().get(0).getYpos());

        assertEquals(c2X, game.getCircles().get(1).getXpos());
        assertEquals(c2Y, game.getCircles().get(1).getYpos());
    }

    @Test
    void testTickRoofCollisionBorder() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, 5,
                0, -5, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(0, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals(-5 + Circle.YACC, c.getYvel());
    }

    @Test
    void testTickRoofCollision20Past() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, 5,
                0, -20, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(0, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals((int) (-20 * Circle.BOUNCE_COEFFICENT + Circle.YACC), c.getYvel());
    }

    @Test
    void testTickRoofCollision1Past() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, 5,
                0, -6, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(0, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals((int) (-6 * Circle.BOUNCE_COEFFICENT + Circle.YACC), c.getYvel());
    }

    @Test
    void testTickLeftCollision() {
        game.deleteCircles();

        Circle c = new Circle(5, game.getHeight() / 2,
                -20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(0, c.getXpos());
        assertEquals((int) (-20 * Circle.X_COEFFICENT * -1), c.getXvel());
        assertEquals(Circle.YACC, c.getYvel());
    }

    @Test
    void testTickRightCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() - 5, game.getHeight() / 2,
                20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(game.getWidth() - 25, c.getXpos());
        assertEquals((int) (20 * Circle.X_COEFFICENT * -1), c.getXvel());
        assertEquals(Circle.YACC, c.getYvel());
    }

    @Test
    void testTickGroundCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, game.getHeight() - 30,
                0, 20, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(game.getHeight() - 25, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals((int) (20 * Circle.BOUNCE_COEFFICENT + Circle.YACC), c.getYvel());
    }
}