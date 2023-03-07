package persistence;

import model.Circle;
import model.Game;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonReader {
    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    public Game read() throws IOException {
//        JSONObject js = parser

        InputStream is = new FileInputStream(source);
        JSONTokener tokener = new JSONTokener(is);
        JSONObject json = new JSONObject(tokener);

        Game g = new Game();

        makeGame(g, json);

        return g;
    }

    private void makeGame(Game g, JSONObject json) {
        for (Object obj : json.getJSONArray("circles")) {
            JSONObject next = (JSONObject) obj;
            addCircle(g, next);
        }
    }

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
