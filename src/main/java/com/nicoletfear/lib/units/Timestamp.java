package com.nicoletfear.lib.units;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;

import org.littletonrobotics.junction.Logger;

/** Represents a specific moment in time, as opposed to a duration of time. */
public class Timestamp {
    private double value;

    /** Initializes a timestamp with a value, in seconds. */
    Timestamp(double value) {
        this.value = value;
    }

    /**
     * Logs the timestamp's value in seconds to {@code key}/Value, and the unit (always seconds) to
     * {@code key}/Unit.
     *
     * @param key the key at which to log the timestamp
     */
    public void log(String key) {
        Logger.recordOutput(key + "/Value", value);
        Logger.recordOutput(key + "/Unit", "s");
    }

    /**
     * Whether this timestamp came after another.
     *
     * @param other the timestamp being compared
     */
    public boolean after(Timestamp other) {
        return this.value > other.value;
    }

    /**
     * Whether this timestamp came before another.
     *
     * @param other the timestamp being compared
     */
    public boolean before(Timestamp other) {
        return this.value < other.value;
    }

    /**
     * The duration of time that passed between this timestamp and another.
     *
     * @param other the prior timestamp (or later, for a negative result)
     */
    public Time since(Timestamp other) {
        return Seconds.of(value - other.value);
    }
}
