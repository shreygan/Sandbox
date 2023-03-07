package persistence;

import model.Circle;
import model.Game;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFolderDestination() {
        try {
            JsonWriter writer = new JsonWriter("./data/invalid/invalidFile.json");
            writer.open();
            fail("expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            Game g = new Game();

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGame.json");

            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGame.json");
            g = reader.read();

            assertEquals(0, g.getCircles().size());
        } catch (IOException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testWriterNormalGame() {
        try {
            Game g = new Game();

            Circle c1 = new Circle(1, 1, 1, 1, 1, Color.RED, 1);
            g.addCircle(c1);

            Circle c2 = new Circle(2, 2, 2, 2, 2, Color.RED, 2);
            g.addCircle(c2);

            JsonWriter writer = new JsonWriter("./data/testWriterNormalGame.json");

            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNormalGame.json");
            g = reader.read();

            assertEquals(2, g.getCircles().size());

            assertEquals(1, g.getCircles().get(0).getId());
            assertEquals(2, g.getCircles().get(1).getId());
        } catch (IOException e) {
            fail("no exception expected");
        }
    }
}
