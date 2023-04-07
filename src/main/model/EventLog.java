package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// EventLog system for Sandbox, using singleton design pattern
// From AlarmSystem Project
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;
    private Collection<Event> events;

    // EFFECTS: creates the singular instance of EventLog
    private EventLog() {
        events = new ArrayList<>();
    }

    // EFFECTS: returns the singular instance of EventLog, creates new
    //          one if it doesn't already exist
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    // MODIFIES: this
    // EFFECTS: adds given event to event log
    public void logEvent(Event e) {
        events.add(e);
    }

    // MODIFIES: this
    // EFFECTS: clears all events from event log
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: returns iterator for events
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
