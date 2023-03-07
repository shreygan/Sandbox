package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private String destination;
    private PrintWriter writer;

    public JsonWriter(String destination) {
        this.destination = destination;
    }

    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    public void write(Writeable w) {
        writer.print(w.toJson().toString(4));
    }

    public void close() {
        writer.close();
    }
}
