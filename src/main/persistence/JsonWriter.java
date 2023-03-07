package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// a writer that writes JSON representations of Writeables to a file
public class JsonWriter {
    private String destination;
    private PrintWriter writer;

    // EFFECTS: initalizes new writer to write at given distination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MDOFIES: this
    // EFFECTS: opens writer
    // throws FileNotFoundException if given destination is invalid
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of given writeable
    public void write(Writeable w) {
        writer.print(w.toJson().toString(4));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
