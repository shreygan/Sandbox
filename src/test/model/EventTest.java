package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

// Tests from AlarmSystem Project
public class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Sensor open at door");
        d = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d);

        Calendar c2 = Calendar.getInstance();
        c1.setTime(e.getDate());

        assertEquals(c1.get(Calendar.DATE), c2.get(Calendar.DATE));
        assertEquals(c1.get(Calendar.HOUR), c2.get(Calendar.HOUR));
        assertEquals(c1.get(Calendar.MINUTE), c2.get(Calendar.MINUTE));
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    void testEqualsAndHash() {
        Event e2 = new Event("Sensor open at door");
        Event e3 = new Event("test event");

        assertEquals(e, e2);
        assertEquals(e.hashCode(), e2.hashCode());

        assertNotEquals(e, null);
        assertNotEquals(e, "test");

        assertNotEquals(e, e3);
        assertNotEquals(e.hashCode(), e3.hashCode());
    }
}
