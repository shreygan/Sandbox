package persistence;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import org.json.JSONTokener;

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
    public Game read() throws IOException {
        InputStream is = new FileInputStream(source);
        JSONTokener tokener = new JSONTokener(is);
        JSONObject json = new JSONObject(tokener);

        Game g = new Game();

        makeGame(g, json);

        return g;
    }

    // REQUIRES: given JSONObject must contain JSONArray w/ key circles
    // MODIFIES: g
    // EFFECTS: parses given JSONObject as game
    private void makeGame(Game g, JSONObject json) {
        for (Object obj : json.getJSONArray("circles")) {
            JSONObject next = (JSONObject) obj;
            addCircle(g, next);
        }
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
        Color c = new Color(json.getInt("color"));
        int id = json.getInt("id");

        g.addCircle(new Circle(xpos, ypos, xvel, yvel, diam, c, id));
    }
}
