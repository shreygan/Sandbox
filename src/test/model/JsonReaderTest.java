package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import ui.Sandbox;
import ui.SandboxPanel;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    private Sandbox sandbox;

    @BeforeEach
    void setup() {
        sandbox = new Sandbox("Test");
    }

    @Test
    void testReaderInvalidFile() {
        JsonReader reader = new JsonReader("./data/invalidFile.json");

        try {
            SandboxPanel panel = reader.read(sandbox);
            fail("expected IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyPanel() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPanel.json");

        try {
            SandboxPanel panel = reader.read(sandbox);


            assertTrue(panel.isRunning());
            assertEquals(0, panel.getGame().getCircles().size());
        } catch (IOException e) {
            fail("couldn't read file");
        }
    }

    @Test
    void testReaderNormalPanel() {
        JsonReader reader = new JsonReader("./data/testReaderNormalPanel.json");

        try {
            SandboxPanel panel = reader.read(sandbox);

            assertTrue(panel.isRunning());

            Game g = panel.getGame();

            assertEquals(5, g.getId());
            assertEquals(2, g.getCircles().size());
        } catch (IOException e) {
            fail("couldn't read file");
        }
    }
}
