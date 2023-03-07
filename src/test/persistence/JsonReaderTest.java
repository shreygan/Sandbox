package persistence;

import model.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderInvalidFile() {
        JsonReader reader = new JsonReader("./data/invalidFile.json");

        try {
            Game g = reader.read();
            fail("expected IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGame() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGame.json");

        try {
            Game g = reader.read();

            assertEquals(0, g.getCircles().size());
        } catch (IOException e) {
            fail("couldn't read file");
        }
    }

    @Test
    void testReaderNormalGame() {
        JsonReader reader = new JsonReader("./data/testReaderNormalGame.json");

        try {
            Game g = reader.read();

            assertEquals(3, g.getCircles().size());

            assertEquals(1, g.getCircles().get(0).getId());
            assertEquals(2, g.getCircles().get(1).getId());
            assertEquals(3, g.getCircles().get(2).getId());
        } catch (IOException e) {
            fail("couldn't read file");
        }
    }
}
