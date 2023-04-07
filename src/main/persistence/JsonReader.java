package persistence;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import org.json.JSONTokener;
import ui.ActionHandler;
import ui.GraphicsHandler;
import ui.Sandbox;
import ui.SandboxPanel;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// a readers that reads and converts JSON representations of Game to Game objects
public class JsonReader {
    private String source;

    // EFFECTS: initializes new json reader with given source
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads json file at source and returns as Game
    // throwns IOException if an error occurs during reading data
    public SandboxPanel read(Sandbox sandbox) throws IOException {
        InputStream is = new FileInputStream(source);
        JSONTokener tokener = new JSONTokener(is);
        JSONObject json = new JSONObject(tokener);

        SandboxPanel panel = new SandboxPanel(sandbox);

        makeSandboxPanel(panel, json);

        return panel;
    }

    // REQUIRES: given JSONObject must contain JSONArray w/ key circles
    // MODIFIES: p
    // EFFECTS: parses given JSONObject as sandboxpanel
    private void makeSandboxPanel(SandboxPanel p, JSONObject json) {
        p.setRunning(json.getBoolean("running"));

        Game g = makeGame(p, json.getJSONObject("game"));

        p.setGame(g);
        p.setGuiHandler(new GraphicsHandler(p, new GridBagConstraints()));
        p.getActionHandler().removeAllListeners();
        p.setActionHandler(new ActionHandler(p, g));
    }

    // REQUIRES: given JSONObject must represent a game with corresponding variables
    // MODIFIES: p
    // EFFECTS: parses given JSONObject as a game for sandboxpanel
    private Game makeGame(SandboxPanel p, JSONObject json) {
        Dimension d = new Dimension(json.getInt("width"), json.getInt("height"));

        p.setSize(d);

        Game g = new Game(d);

        g.setId(json.getInt("id"));

        for (Object obj : json.getJSONArray("circles")) {
            JSONObject next = (JSONObject) obj;
            addCircle(g, next);
        }

        return g;
    }

    // REQUIRES: given JSONObject must be storing a circle w/
    //           the corresponding keys
    // MODIFIES: g
    // EFFECTS: parses given JSONObject as circle and adds to game
    private void addCircle(Game g, JSONObject json) {
        int xpos = json.getInt("xpos");
        int ypos = json.getInt("ypos");
        int xvel = json.getInt("xvel");
        int yvel = json.getInt("yvel");
        int diam = json.getInt("diam");
        Color color = new Color(json.getInt("color"));
        int id = json.getInt("id");
        boolean accelerating = json.getBoolean("accelerating");

        g.addCircle(new Circle(xpos, ypos, xvel, yvel, diam, color, id, accelerating));
    }
}
