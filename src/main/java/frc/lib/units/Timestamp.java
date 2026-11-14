package frc.lib.units;

import static frc.lib.units.TimeUnit.Seconds;

public class Timestamp {
    double value;

    Timestamp(double value) {
        this.value = value;
    }

    public boolean after(Timestamp other) {
        return this.value > other.value;
    }

    public boolean before(Timestamp other) {
        return this.value < other.value;
    }

    public Time since(Timestamp other) {
        return Seconds.of(value - other.value);
    }
}
