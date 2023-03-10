package persistence;

import model.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Sandbox;
import ui.SandboxPanel;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    private Sandbox sandbox;

    @BeforeEach
    void setup() {
        this.sandbox = new Sandbox("Tests");
    }
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
    void testWriterEmptyPanel() {
        try {
            SandboxPanel panel = new SandboxPanel(sandbox);

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPanel.json");

            writer.open();
            writer.write(panel);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPanel.json");
            panel = reader.read(sandbox);

            assertTrue(panel.isRunning());
            assertEquals(0, panel.getGame().getCircles().size());
            assertEquals(1, panel.getGame().getId());
        } catch (IOException e) {
            fail("No exception expected");
        }
    }

    @Test
    void testWriterNormalGame() {
        try {
            SandboxPanel panel = new SandboxPanel(sandbox);

            Circle c1 = new Circle(1, 1, 1, 1, 1, Color.RED, 1, true);

            Circle c2 = new Circle(2, 2, 2, 2, 2, Color.RED, 2, true);

            panel.getGame().addCircle(c1);
            panel.getGame().addCircle(c2);

            JsonWriter writer = new JsonWriter("./data/testWriterNormalPanel.json");

            writer.open();
            writer.write(panel);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNormalPanel.json");
            panel = reader.read(sandbox);

            assertTrue(panel.isRunning());
            assertEquals(2, panel.getGame().getCircles().size());

            assertEquals(1, panel.getGame().getCircles().get(0).getId());
            assertEquals(2, panel.getGame().getCircles().get(1).getId());
        } catch (IOException e) {
            fail("no exception expected");
        }
    }
}
