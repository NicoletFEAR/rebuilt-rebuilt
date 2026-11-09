package frc.lib.units;

public class Distance extends Quantity<Distance, DistanceUnit> {
    Distance(double value) {
        super(value, "m");
    }

    public Distance max(Distance other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Distance min(Distance other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Distance plus(Distance other) {
        return new Distance(value + other.value);
    }

    public Distance minus(Distance other) {
        return new Distance(value - other.value);
    }

    public Distance times(double other) {
        return new Distance(value * other);
    }

    public Distance div(double other) {
        return new Distance(value / other);
    }

    public Distance negative() {
        return new Distance(-value);
    }

    public Distance ceil() {
        return new Distance(Math.ceil(value));
    }

    public Distance floor() {
        return new Distance(Math.floor(value));
    }

    public Velocity div(Time other) {
        return new Velocity(value / other.value);
    }

    public Time div(Velocity other) {
        return new Time(value / other.value);
    }
}
