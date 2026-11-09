package frc.lib.units;

import static frc.lib.units.TimeUnit.Seconds;

public class Timestamp {
    double value;

    Timestamp(double value) {
        this.value = value;
    }

    public Time since(Timestamp other) {
        return Seconds.of(value - other.value);
    }
}
