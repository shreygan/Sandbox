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
    void testAddCircleRandom() {
        game.addCircle();
        game.addCircle();

        assertEquals(4, game.getCircles().size());
    }

    @Test
    void testAddCircleUntilOverlaps() {
        int i;
        for (i = 0; i < 200; i++) {
            game.addCircle();
        }

        assertEquals(i + 2, game.getCircles().size());
    }

    @Test
    void testAddCircleGivenCircle() {
        Circle c = new Circle(1, 1, 1, 1, 1, Color.RED, 1, true);
        game.addCircle(c);

        assertEquals(3, game.getCircles().size());
        assertTrue(game.getCircles().contains(c));
    }

    @Test
    void testAddCircleOnMousePos() {
        Point p = new Point(20, 435);
        game.addCircle(p, 23, 23, 23);

        assertEquals(3, game.getCircles().size());
        assertTrue(game.getCircles().get(2).overlaps(new Point(p.x + 12, p.y + 12)));
    }

    @Test
    void testAddCircleGivenMouseDrag() {
        Point mouseInit = new Point(10, 10);
        Point mouseCurr = new Point(20, 20);

        int rad = (mouseCurr.y - mouseInit.y) * 2;
        Circle c1 = new Circle(mouseInit.x - rad / 2, mouseInit.y - rad / 2,
                12, 13, rad, Color.RED, 4, true);

        game.addCircle(mouseInit, mouseCurr, Color.RED);

        assertEquals(3, game.getCircles().size());
        Circle c2 = game.getCircles().get(2);

        assertEquals(c1.getXpos(), c2.getXpos());
        assertEquals(c1.getYpos(), c2.getYpos());
        assertEquals(c1.getRad(), c2.getRad());
    }

    @Test
    void testDeleteCircles() {
        assertEquals(2, game.getCircles().size());

        game.deleteCircles();

        assertEquals(0, game.getCircles().size());
    }

    @Test
    void testDeleteCirclesGivenOverlappingMousePos() {
        assertEquals(2, game.getCircles().size());

        Point p = new Point((int) (c1.getXpos() + c1.getRad()), (int) (c1.getYpos() + c1.getRad()));
        game.deleteCircles(p);

        assertEquals(1, game.getCircles().size());
        assertTrue(game.getCircles().contains(c2));
        assertFalse(game.getCircles().contains(c1));
    }

    @Test
    void testDeleteCirclesGivenNormalMousePos() {
        assertEquals(2, game.getCircles().size());

        Point p = new Point(1, 1);
        game.deleteCircles(p);

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
    void testOverlaps() {
        Point p = new Point((int) (c1.getXpos() + c1.getRad()), (int) (c1.getYpos() + c1.getRad()));
        assertEquals(c1, game.overlaps(p));
    }

    @Test
    void testNotOverlaps() {
        Point p = new Point(1, 1);
        assertNull(game.overlaps(p));
    }

    @Test
    void testMoveCircle() {
        game.setCircleDragged(c1);
        game.moveCircle(new Point(100, 100));

        assertEquals(c1.getXpos(), 100 - c1.getDiam() / 2);
        assertEquals(c1.getYpos(), 100 - c1.getDiam() / 2);
    }

    @Test
    void testReleaseCircleThrow() {
        game.setCircleDragged(c1);

        Point mouseInit = new Point(c1.getXpos(), c1.getYpos());
        Point mouseCurr = new Point(c1.getXpos() + 200, c1.getYpos() - 200);

        game.releaseCircle(mouseInit, mouseCurr, 500);

        double angle = Math.atan2(mouseCurr.y - mouseInit.y, mouseCurr.x - mouseInit.x);

        assertEquals(c1.getXvel(), (int) ((Math.cos(angle) * Game.THROW_SPEED / 10) / (500f / Game.THROW_SPEED)));
        assertEquals(c1.getYvel(), (int) ((Math.sin(angle) * Game.THROW_SPEED / 10) / (500f / Game.THROW_SPEED)));
    }

    @Test
    void testReleaseCircleNormal() {
        game.setCircleDragged(c1);

        int xvel = c1.getXvel();
        int yvel = c1.getYvel();

        game.releaseCircle();

        assertEquals(xvel, c1.getXvel());
        assertEquals(yvel, c1.getYvel());
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
        assertEquals((int) (-20 * Circle.Y_COEFFICENT), c.getYvel());
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
        assertEquals((int) (-6 * Circle.Y_COEFFICENT), c.getYvel());
    }

    @Test
    void testTickLeftCollision() {
        game.deleteCircles();

        Circle c = new Circle(5, game.getHeight() / 2,
                -20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(0, c.getXpos());
        assertEquals((int) (-20 * Circle.BOUNCE_COEFFICENT * -1), c.getXvel());
        assertEquals(0, c.getYvel());
    }

    @Test
    void testTickRightCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() - 5, game.getHeight() / 2,
                20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.tick();

        assertEquals(game.getWidth() - 25, c.getXpos());
        assertEquals((int) (20 * Circle.BOUNCE_COEFFICENT * -1), c.getXvel());
        assertEquals(0, c.getYvel());
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
        assertEquals((int) (20 * Circle.Y_COEFFICENT), c.getYvel());
    }

    @Test
    void testTickCircleCollision() {
        game.deleteCircles();

        Circle c1 = new Circle(game.getWidth() / 2 - 30, game.getHeight() - 30,
                10, 0, 25, Color.RED, 3, true);

        Circle c2 = new Circle(game.getWidth() / 2 + 30, game.getHeight() - 30,
                -10, 0, 25, Color.BLUE, 4, true);

        game.addCircle(c1);
        game.addCircle(c2);
        game.tick();

        assertEquals(game.getHeight() - 30, c1.getYpos());
        assertEquals(-8, c1.getXvel());
        assertEquals(0, c1.getYvel());

        assertEquals(game.getHeight() - 30, c2.getYpos());
        assertEquals(8, c2.getXvel());
        assertEquals(0, c2.getYvel());
    }

    @Test
    void testUntickNoCollisions() {
        int c1X = c1.prevX();
        int c1Y = c1.prevY();

        int c2X = c2.prevX();
        int c2Y = c2.prevY();

        game.untick();

        assertEquals(c1X, c1.getXpos());
        assertEquals(c1Y, c1.getYpos());

        assertEquals(c2X, c2.getXpos());
        assertEquals(c2Y, c2.getYpos());
    }

    @Test
    void testUntickRoofCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, 5,
                0, 30, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.untick();

        assertEquals(0, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals((int) (30 * (1 / Circle.Y_COEFFICENT) + Circle.YACC), c.getYvel());
    }

    @Test
    void testUntickLeftCollision() {
        game.deleteCircles();

        Circle c = new Circle(5, game.getHeight() / 2,
                20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.untick();

        assertEquals(0, c.getXpos());
        assertEquals((int) (20 * (1 / Circle.BOUNCE_COEFFICENT) * -1), c.getXvel());
        assertEquals(0, c.getYvel());
    }

    @Test
    void testUntickRightCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() - 5, game.getHeight() / 2,
                -20, 0, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.untick();

        assertEquals(game.getWidth() - 25, c.getXpos());
        assertEquals((int) (-20 * (1 / Circle.BOUNCE_COEFFICENT) * -1), c.getXvel());
        assertEquals(0, c.getYvel());
    }

    @Test
    void testUntickGroundCollision() {
        game.deleteCircles();

        Circle c = new Circle(game.getWidth() / 2, game.getHeight() - 30,
                0, -20, 25, Color.WHITE, 3, true);

        game.addCircle(c);
        game.untick();

        assertEquals(game.getHeight() - 25, c.getYpos());
        assertEquals(0, c.getXvel());
        assertEquals((int) (-20 * (1 / Circle.Y_COEFFICENT) + Circle.YACC), c.getYvel());
    }
}