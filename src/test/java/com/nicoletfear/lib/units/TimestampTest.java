package com.nicoletfear.lib.units;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TimestampTest {
    private static final double EPSILON = 1e-9;

    @Test
    void storesValueInSeconds() {
        Timestamp timestamp = Seconds.timestamp(12.5);

        assertEquals(12.5, timestamp.value, EPSILON);
    }

    @Test
    void unitTimestampConvertsToBaseSeconds() {
        TimeUnit doubleSeconds = new TimeUnit(2.0);

        assertEquals(6.0, doubleSeconds.timestamp(3.0).value, EPSILON);
    }

    @Test
    void comparesChronologically() {
        Timestamp earlier = Seconds.timestamp(1.0);
        Timestamp later = Seconds.timestamp(2.0);

        assertTrue(later.after(earlier));
        assertFalse(earlier.after(later));
        assertTrue(earlier.before(later));
        assertFalse(later.before(earlier));
        assertFalse(earlier.after(earlier));
        assertFalse(earlier.before(earlier));
    }

    @Test
    void sinceReturnsElapsedTime() {
        Timestamp start = Seconds.timestamp(4.0);
        Timestamp end = Seconds.timestamp(10.0);

        assertEquals(6.0, end.since(start).in(Seconds), EPSILON);
        assertEquals(-6.0, start.since(end).in(Seconds), EPSILON);
        assertEquals(0.0, start.since(start).in(Seconds), EPSILON);
    }

    @Test
    void loggingIsSafeWithoutAdvantageKitRunning() {
        assertDoesNotThrow(() -> Seconds.timestamp(1.0).log("Test/Timestamp"));
    }
}
