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
    public void setup() {
        game = new Game();

        c1 = new Circle(Game.WIDTH / 2, Game.HEIGHT / 2, 0, 2, 10, Color.RED, 1);
        c2 = new Circle(Game.WIDTH / 3, Game.HEIGHT / 3, 10, -10, 15, Color.BLUE, 2);

        game.addCircle(c1);
        game.addCircle(c2);
    }

    @Test
    public void testConstructor() {
        assertTrue(game.isRunning());
    }

    @Test
    public void testGetOutput() {
        String expected1 = "1 (diameter = 10 color = R:255 G:0 B:0)\n"
                + "xPos: " + Game.WIDTH / 2 + "  yPos: " + Game.HEIGHT / 2 + "  xVel: "
                + 0 + "  yVel: " + 2 + "\n"

                + "2 (diameter = 15 color = R:0 G:0 B:255)\n"
                + "xPos: " + Game.WIDTH / 3 + "  yPos: " + Game.HEIGHT / 3 + "  xVel: "
                + 10 + "  yVel: " + -10 + "\n\n";

        String expected2 = "1 (diameter = 10 color = R:255 G:0 B:0)\n"
                + "xPos: " + (Game.WIDTH / 2) + "  yPos: " + (Game.HEIGHT / 2 + 2) + "  xVel: "
                + 0 + "  yVel: " + 3 + "\n"

                + "2 (diameter = 15 color = R:0 G:0 B:255)\n"
                + "xPos: " + (Game.WIDTH / 3 + 10) + "  yPos: " + (Game.HEIGHT / 3 - 10) + "  xVel: "
                + 10 + "  yVel: " + -9 + "\n\n";

        String expected3 = "1 (diameter = 10 color = R:255 G:0 B:0)\n"
                + "xPos: " + (Game.WIDTH / 2) + "  yPos: " + (Game.HEIGHT / 2 + 2 + 3) + "  xVel: "
                + 0 + "  yVel: " + 4 + "\n"

                + "2 (diameter = 15 color = R:0 G:0 B:255)\n"
                + "xPos: " + (Game.WIDTH / 3 + 10 + 10) + "  yPos: " + (Game.HEIGHT / 3 - 10 - 9) + "  xVel: "
                + 10 + "  yVel: " + -8 + "\n\n";

        String expected4 = "1 (diameter = 10 color = R:255 G:0 B:0)\n"
                + "xPos: " + (Game.WIDTH / 2) + "  yPos: " + (Game.HEIGHT / 2 + 2 + 3 + 4) + "  xVel: "
                + 0 + "  yVel: " + 5 + "\n"

                + "2 (diameter = 15 color = R:0 G:0 B:255)\n"
                + "xPos: " + (Game.WIDTH / 3 + 10 + 10 + 10) + "  yPos: " + (Game.HEIGHT / 3 - 10 - 9 - 8) + "  xVel: "
                + 10 + "  yVel: " + -7 + "\n\n";

        String expected5 = "1 (diameter = 10 color = R:255 G:0 B:0)\n"
                + "xPos: " + (Game.WIDTH / 2) + "  yPos: " + (Game.HEIGHT / 2 + 2 + 3 + 4 + 5) + "  xVel: "
                + 0 + "  yVel: " + 6 + "\n"

                + "2 (diameter = 15 color = R:0 G:0 B:255)\n"
                + "xPos: " + (Game.WIDTH / 3 + 10 + 10 + 10 + 10) + "  yPos: " + (Game.HEIGHT / 3 - 10 - 9 - 8 - 7)
                + "  xVel: " + 10 + "  yVel: " + -6 + "\n\n";

        assertEquals(expected1 + expected2 + expected3 + expected4 + expected5, game.getOutput());
    }

    @Test
    public void testGetOutputNotRunning() {
        game.setRunning(false);

        assertEquals("", game.getOutput());
    }

    @Test
    public void testAddCircle1() {
        game.addCircle();
        assertEquals(3, game.getCircles().size());

        game.addCircle();
        game.addCircle();
        assertEquals(5, game.getCircles().size());
    }

    @Test
    public void testRelaunchCircles() {
        Circle c1Copy = new Circle(Game.WIDTH / 2, Game.HEIGHT / 2,
                0, 2, 10, Color.RED, 1);
        Circle c2Copy = new Circle(Game.WIDTH / 3, Game.HEIGHT / 3,
                10, -10, 15, Color.BLUE, 2);

        game.relaunchCircles();

        assertEquals(c1.getXpos(), c1Copy.getXpos());
        assertEquals(c2.getXpos(), c2Copy.getXpos());

        assertEquals(c1.getYpos(), c1Copy.getYpos());
        assertEquals(c2.getYpos(), c2Copy.getYpos());

        assertNotEquals(c1.getXvel(), c1Copy.getXvel());
        assertNotEquals(c2.getXvel(), c2Copy.getXvel());

        assertNotEquals(c1.getYvel(), c1Copy.getYvel());
        assertNotEquals(c2.getYvel(), c2Copy.getYvel());
    }

    @Test
    public void testDeleteCircles() {
        game.deleteCircles();
        assertEquals(0, game.getCircles().size());
    }

    @Test
    public void testUpdateCircleNormal() {
        Circle c1Copy = new Circle(Game.WIDTH / 2, Game.HEIGHT / 2,
                0, 2, 10, Color.RED, 1);


        game.updateCircle(c1);

        assertEquals(c1.getXpos(), c1Copy.getXpos() + c1Copy.getXvel());
        assertEquals(c1.getYpos(), c1Copy.getYpos() + c1Copy.getYvel());

        assertEquals(c1.getXvel(), c1Copy.getXvel());
        assertEquals(c1.getYvel(), c1Copy.getYvel() + Circle.YACC);
    }

    @Test
    public void testUpdateCircleDown() {
        Circle down = new Circle(Game.WIDTH / 2, Game.HEIGHT - 50, 0, 10, 45,
                Color.RED, 1);
        Circle downCopy = new Circle(Game.WIDTH / 2, Game.HEIGHT - 50, 0, 10, 45,
                Color.RED, 1);

        game.updateCircle(down);

        assertEquals(down.getXpos(), downCopy.getXpos() + downCopy.getXvel());
        assertEquals(down.getYpos(), Game.HEIGHT - downCopy.getDiam());

        assertEquals(down.getXvel(), downCopy.getXvel());
        assertEquals(down.getYvel(), downCopy.getYvel() * -.8 + Circle.YACC);
    }

    @Test
    public void testUpdateCircleDownSlow() {
        Circle down = new Circle(Game.WIDTH / 2, Game.HEIGHT - 47, 0, 3, 45,
                Color.RED, 1);
        Circle downCopy = new Circle(Game.WIDTH / 2, Game.HEIGHT - 47, 0, 3, 45,
                Color.RED, 1);

        game.updateCircle(down);

        assertEquals(down.getXpos(), downCopy.getXpos() + downCopy.getXvel());
        assertEquals(down.getYpos(), Game.HEIGHT - downCopy.getDiam());

        assertEquals(down.getXvel(), downCopy.getXvel());
        assertEquals(down.getYvel(), 1);
    }

    @Test
    public void testUpdateCircleUp() {
        Circle up = new Circle(Game.WIDTH / 2, 15, 0, -20, 10,
                Color.BLUE, 2);
        Circle upCopy = new Circle(Game.WIDTH / 2, 25, 0, -20, 10,
                Color.BLUE, 2);

        game.updateCircle(up);

        assertEquals(up.getXpos(), upCopy.nextLeftXCoord());
        assertEquals(up.getYpos(), 0);

        assertEquals(up.getXvel(), upCopy.getXvel());
        assertEquals(up.getYvel(), upCopy.getYvel() * -1 + Circle.YACC);
    }

    @Test
    public void testUpdateCircleLeft() {
        Circle left = new Circle(3, Game.HEIGHT / 2, -5, 0, 5,
                Color.GREEN, 3);
        Circle leftCopy = new Circle(3, Game.HEIGHT / 2, -5, 0, 5,
                Color.GREEN, 3);

        game.updateCircle(left);

        assertEquals(left.getXpos(), 0);
        assertEquals(left.getYpos(), leftCopy.nextUpYCoord());

        assertEquals(left.getXvel(), leftCopy.getXvel() * -.8 - Circle.XACC);
        assertEquals(left.getYvel(), leftCopy.getYvel() + Circle.YACC);
    }

    @Test
    public void testUpdateCircleRight() {
        Circle right = new Circle(Game.WIDTH - 100, Game.HEIGHT / 2, 15, 0, 90,
                Color.BLACK, 4);
        Circle rightCopy = new Circle(Game.WIDTH - 100, Game.HEIGHT / 2, 15, 0, 90,
                Color.BLACK, 4);

        game.updateCircle(right);

        assertEquals(right.getXpos(), Game.WIDTH - rightCopy.getDiam());
        assertEquals(right.getYpos(), rightCopy.nextUpYCoord());

        assertEquals(right.getXvel(), rightCopy.getXvel() * -.8 + Circle.XACC);
        assertEquals(right.getYvel(), rightCopy.getYvel() + Circle.YACC);
    }

}