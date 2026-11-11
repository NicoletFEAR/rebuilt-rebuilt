package frc.lib.units;

public class Velocity extends Quantity<Velocity, VelocityUnit> {
    Velocity(double value) {
        super(value, "m/s");
    }

    public Velocity max(Velocity other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Velocity min(Velocity other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Velocity clamp(Velocity low, Velocity high) {
        return max(low).min(high);
    }

    public Velocity plus(Angle other) {
        return new Velocity(value + other.value);
    }

    public Velocity minus(Angle other) {
        return new Velocity(value - other.value);
    }

    public Velocity times(double other) {
        return new Velocity(value * other);
    }

    public Velocity div(double other) {
        return new Velocity(value / other);
    }

    public Velocity negative() {
        return new Velocity(-value);
    }

    public Velocity ceil() {
        return new Velocity(Math.ceil(value));
    }

    public Velocity floor() {
        return new Velocity(Math.floor(value));
    }

    public Distance times(Time other) {
        return new Distance(value * other.value);
    }
}
