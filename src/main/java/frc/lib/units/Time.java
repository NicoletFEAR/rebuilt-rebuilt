package frc.lib.units;

public class Time extends Quantity<Time, TimeUnit> {
    Time(double value) {
        super(value, "s");
    }

    public Time max(Time other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Time min(Time other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Time clamp(Time low, Time high) {
        return max(low).min(high);
    }

    public Time plus(Time other) {
        return new Time(value + other.value);
    }

    public Time minus(Time other) {
        return new Time(value - other.value);
    }

    public Time times(double other) {
        return new Time(value * other);
    }

    public Time div(double other) {
        return new Time(value / other);
    }

    public Time negative() {
        return new Time(-value);
    }

    public Time ceil() {
        return new Time(Math.ceil(value));
    }

    public Time floor() {
        return new Time(Math.floor(value));
    }

    public Frequency reciprocal() {
        return new Frequency(1.0 / value);
    }

    public AngularVelocity times(AngularAcceleration other) {
        return new AngularVelocity(value * other.value);
    }

    public Distance times(Velocity other) {
        return new Distance(value * other.value);
    }

    public Angle times(AngularVelocity other) {
        return new Angle(value * other.value);
    }

    public Anglement times(Angle other) {
        return new Anglement(value * other.value);
    }
}
