package frc.lib.units;

import static frc.lib.units.TimeUnit.Seconds;

import org.littletonrobotics.junction.Logger;

public class Timestamp {
    double value;

    Timestamp(double value) {
        this.value = value;
    }

    public void log(String key) {
        Logger.recordOutput(key + "/Value", value);
        Logger.recordOutput(key + "/Unit", "s");
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
